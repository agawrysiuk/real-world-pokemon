package agawrysiuk.googlemapspokemonclone;

import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import agawrysiuk.googlemapspokemonclone.model.Database;

public class MainActivity extends AppCompatActivity {

    TextView mWelcometxt, mIncorrectTxt, mNewUserTxt;
    private EditText mUsernameTxt, mPasswordTxt, mEmailTxt;
    private Button mLoginBtn;
    private ConstraintLayout mConstraintLayout;

    private boolean isLoggingIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

//        ParseUser.logOut();

        Database.getInstance().downloadDatabase();
        Database.getInstance().downloadYourCollection();

        // == sending information about installation ==
        ParseInstallation.getCurrentInstallation().saveInBackground();

        // == if there is user, we move to the map ==
        if (ParseUser.getCurrentUser() != null) {
            transitionToMap();
        }

        // == on startup, we want people to log in ==
        isLoggingIn = true;

        // == finding views ==
        mWelcometxt = findViewById(R.id.welcomeTV);
        mIncorrectTxt = findViewById(R.id.incorrectTxtView);
        mNewUserTxt = findViewById(R.id.newUserTxtView);
        mUsernameTxt = findViewById(R.id.usernameEdtTxt);
        mPasswordTxt = findViewById(R.id.passwordEdtTxt);
        mEmailTxt = findViewById(R.id.emailEdtTxt);
        mLoginBtn = findViewById(R.id.loginBtn);
        mConstraintLayout = findViewById(R.id.welcomeLayout);

        // == set up ENTER to automatically log in ==
        mPasswordTxt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    hideKeyboard(v);
                    mLoginBtn.callOnClick();
                }
                return false;
            }
        });


        // == clicking login / signup button ==
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoggingIn) {
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
                // == we hide error text just in case ==
                mIncorrectTxt.setVisibility(View.INVISIBLE);

                // == transition manager animates the layout ==
                TransitionManager.beginDelayedTransition(mConstraintLayout);

                // == creating two different layouts to apply ==
                ConstraintSet constraintGoingToSignup = new ConstraintSet();
                constraintGoingToSignup.clone(MainActivity.this, R.layout.activity_main_signup);
                ConstraintSet constraintGoingToLogin = new ConstraintSet();
                constraintGoingToLogin.clone(MainActivity.this, R.layout.activity_main_login);

                // == setting the layout to the root ==
                ConstraintSet constraint = isLoggingIn ? constraintGoingToSignup : constraintGoingToLogin;
                constraint.applyTo(mConstraintLayout);

                // == changing texts ==
                mLoginBtn.setText(isLoggingIn ? R.string.welcome_signup_button : R.string.welcome_login_button);
                mNewUserTxt.setText(isLoggingIn ? R.string.welcome_existing_user_text : R.string.welcome_new_user_text);
                mIncorrectTxt.setText(isLoggingIn ? R.string.welcome_incorrect_signup_text : R.string.welcome_incorrect_login_text);

                isLoggingIn = !isLoggingIn;
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
        final ParseUser user = new ParseUser();
        user.setUsername(mUsernameTxt.getText().toString());
        user.setPassword(mPasswordTxt.getText().toString());
        user.setEmail(mEmailTxt.getText().toString());
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    transitionToMap();
                } else {
                    mIncorrectTxt.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    // == hiding keyboard when root layout is tapped
    public void hideKeyboard(View view) {
        try {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e) {
            //do nothing?
        }
    }

    private void transitionToMap() {
        startActivity(new Intent(MainActivity.this, MapsActivity.class));
        finish();
    }
}
