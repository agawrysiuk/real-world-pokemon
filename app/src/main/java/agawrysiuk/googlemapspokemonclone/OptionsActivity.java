package agawrysiuk.googlemapspokemonclone;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.suke.widget.SwitchButton;

import agawrysiuk.googlemapspokemonclone.model.Database;
import agawrysiuk.googlemapspokemonclone.model.Settings;

import static agawrysiuk.googlemapspokemonclone.utils.UtilRequestCode.REQUEST_CODE_REFRESH_VIEW;

public class OptionsActivity extends AppCompatActivity {

    SwitchButton switchDark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Settings.getInstance().getStyle());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        switchDark = findViewById(R.id.switchDark);
        switchDark.setChecked(Settings.getInstance().isDarkTheme());
        switchDark.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                Settings.getInstance().setDarkTheme(isChecked);
                refresh();
            }
        });
    }

    private void refresh() {
        this.recreate();
    }

    @Override
    public void onBackPressed() {
        Database.getInstance().saveSettings();
        finish();
        startActivityForResult(new Intent(this, MapsActivity.class), REQUEST_CODE_REFRESH_VIEW);
    }
}
