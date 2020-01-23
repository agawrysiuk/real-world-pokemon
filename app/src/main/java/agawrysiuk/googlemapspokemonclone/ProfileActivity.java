package agawrysiuk.googlemapspokemonclone;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;

import agawrysiuk.googlemapspokemonclone.model.Database;
import agawrysiuk.googlemapspokemonclone.model.Settings;

public class ProfileActivity extends AppCompatActivity {

    TextView mTxtName,mTxtEmail,mTxtSeenNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Settings.getInstance().getStyle());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mTxtName = findViewById(R.id.txtName);
        mTxtName.setText(ParseUser.getCurrentUser().getUsername());

        mTxtEmail = findViewById(R.id.txtEmail);
        mTxtEmail.setText(ParseUser.getCurrentUser().getEmail());

        mTxtSeenNumber = findViewById(R.id.txtSeenNumber);
        mTxtSeenNumber.setText(String.format("%03d", Database.getInstance().getCollection().size()));
    }

    public void finishOnClick(View view) {
        finish();
    }
}
