package agawrysiuk.googlemapspokemonclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;
import com.suke.widget.SwitchButton;

import agawrysiuk.googlemapspokemonclone.model.Database;
import agawrysiuk.googlemapspokemonclone.model.Settings;
import libs.mjn.prettydialog.PrettyDialog;

import static agawrysiuk.googlemapspokemonclone.utils.UtilRequestCode.REQUEST_CODE_REFRESH_VIEW;

public class OptionsActivity extends AppCompatActivity {

    SwitchButton switchDark;
    Button btnChangePlayer,btnChangePassword,btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Settings.getInstance().getStyle());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        // == dark theme switch ==
        switchDark = findViewById(R.id.switchDark);
        switchDark.setChecked(Settings.getInstance().isDarkTheme());
        switchDark.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                Settings.getInstance().setDarkTheme(isChecked);
                refresh();
            }
        });

        // == to be implemented in the future, avatar change
        btnChangePlayer = findViewById(R.id.btnChangePlayer);
        btnChangePlayer.setOnClickListener(yetToComeMessage());

        // == changing password ==
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(yetToComeMessage());

        // == logout button ==
        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                finish();
                startActivity(new Intent(OptionsActivity.this,MainActivity.class));
            }
        });


    }

    private View.OnClickListener yetToComeMessage() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PrettyDialog(OptionsActivity.this)
                        .setTitle("Ooops!")
                        .setMessage("This feature is yet to come!")
                        .setIcon(R.drawable.info_24dp)
                        .setIconTint(R.color.colorPrimaryLight)
                        .show();
            }
        };
    }

    private void refresh() {
        this.recreate();
    }

    @Override
    public void onBackPressed() {
        // == saving settings ==
        Database.getInstance().saveSettings();
        finish();
        // == we want to recreate the previous activity when clicked ==
        startActivityForResult(new Intent(this, MapsActivity.class), REQUEST_CODE_REFRESH_VIEW);
    }
}
