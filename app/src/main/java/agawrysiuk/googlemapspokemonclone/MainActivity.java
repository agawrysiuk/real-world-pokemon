package agawrysiuk.googlemapspokemonclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    TextView mIncorrectTxt, mNewUserTxt;
    private EditText mUsernameTxt, mPasswordTxt;
    private Button mLoginBtn;

    private boolean isLoggingIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // == sending information about installation ==
        ParseInstallation.getCurrentInstallation().saveInBackground();

        // == if there is user, we move to the map ==
        if (ParseUser.getCurrentUser() != null) {
            transitionToMap();
        }

        // == on startup, we want people to log in ==
        isLoggingIn = true;

        // == finding views ==
        mIncorrectTxt = findViewById(R.id.incorrectTxtView);
        mNewUserTxt = findViewById(R.id.newUserTxtView);
        mUsernameTxt = findViewById(R.id.usernameEdtTxt);
        mPasswordTxt = findViewById(R.id.passwordEdtTxt);
        mLoginBtn = findViewById(R.id.loginBtn);

        // == clicking login / signup button ==
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLoggingIn) {
                    // == existing user ==
                    wantsToLogin();
                } else {
                    // == new user ==
                    wantsToSignup();
                }
            }
        });
        mNewUserTxt.setOnClickListener(newUserTxtClicked());
    }

    private View.OnClickListener newUserTxtClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoggingIn) {
                    // == login view, we move to sign up view ==
                    mIncorrectTxt.setVisibility(View.INVISIBLE);

                } else {
                    // == signup view, we move to login view ==
                }
            }
        };
    }

    private void wantsToLogin() {
        ParseUser.logInInBackground(mUsernameTxt.getText().toString(), mPasswordTxt.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    transitionToMap();
                } else {
                    mIncorrectTxt.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void wantsToSignup() {

    }

    private void transitionToMap() {
        //we move to new intent here
        finish();
    }
}
