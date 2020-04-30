package com.example.ushanfernando.sharetaxi;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ushanfernando.sharetaxi.Model.Login;
import com.example.ushanfernando.sharetaxi.Model.STApiRequest;
import com.example.ushanfernando.sharetaxi.Util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements STApiRequest.OnRequestComplete
        , Login.OnLoginRequestComplete {
    Button register;
    Button login;
    EditText email;
    EditText password;
    TextView errorMsg;
    ProgressBar mProgressBar;

    private static final String TAG = "SignUpActivity";
//    private static final String signUpUrl = "http://10.0.2.2:8080/server/signup.php";
//    private static final String loginUrl = "http://10.0.2.2:8080/server/login.php";

    //public server
    private static final String signUpUrl =  Constants.Domain+"/api/signup.php";

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        sessionManager = new SessionManager(this);

        if (sessionManager.getIncompleteRegistrationStatus()){
            Log.d(TAG, "onCreate: Signup Activity: found Incomplete Registretion !");
            Intent intent = new Intent(this,CompleteRegistration.class);
            startActivity(intent);
        }

        ConstraintLayout layout = findViewById(R.id.signup_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) layout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        register = (Button) findViewById(R.id.buttonRegister);
        login = (Button) findViewById(R.id.buttonLogin);
        password = (EditText) findViewById(R.id.passwordField);
        email = (EditText) findViewById(R.id.emailField);
        errorMsg = (TextView) findViewById(R.id.errorMsg);

        mProgressBar = findViewById(R.id.loading);
        mProgressBar.setVisibility(View.INVISIBLE);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick register: works");
                if (validatePasswordEmail(password, email)) {
                    register(email.getText().toString(), password.getText().toString());
//                  register("balla@pusa.caom", "sdsadsadsa");
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });


    }

    private boolean validatePasswordEmail(EditText pass, EditText email) {
        boolean validation = false;
        if (email.getText() == null || email.getText().toString().length() == 0) {
            errorMsg.setText(R.string.empty_email);
            email.setError("email field is empty");
        } else {
            if (!validEmail(email.getText().toString())) {
                errorMsg.setText(R.string.invalid_email);
                email.setError("Please enter a valid email");
                Log.e(TAG, "validatePasswordEmail: invalid mail");
            } else {
                if (pass.getText() == null || pass.getText().toString().length() == 0) {
                    errorMsg.setText(R.string.empty_password);
                    pass.setError("Password field is empty");
                } else {
                    if (pass.getText().toString().length() < 6) {
                        errorMsg.setText(R.string.invalid_password);
                        pass.setError("Password is too short");
                    } else {
                        validation = true;
                        errorMsg.setText("Connecting!");
                    }
                }

            }
        }
        return validation;
    }

    private boolean validEmail(String email) {

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.matches(emailPattern)) {
            return true;
        } else {
            return false;

        }
    }

    private void register(String email, String password) {
//        Register register = new Register(this);
//        register.registerUser(this, email, password);
        mProgressBar.setVisibility(View.VISIBLE);
        Map<String, String> requestTags = new HashMap<String, String>();
        requestTags.put("email", email);
        requestTags.put("password", password);

        STApiRequest register = new STApiRequest(SignUpActivity.this, signUpUrl, "register");
        register.newRequest(this, requestTags);
        Log.d(TAG, "register: req made");


    }

    private void login() {
        if (validatePasswordEmail(password, email)) {
            String passwordString = password.getText().toString();
            String emailString = email.getText().toString();
//
//            Map<String, String> loginRequestTags = new HashMap<String, String>();
//            loginRequestTags.put("email", emailString);
//            loginRequestTags.put("password",passwordString);
//
//            STApiRequest login = new STApiRequest(SignUpActivity.this, loginUrl, "login");
//            login.newRequest(this, loginRequestTags);
//            Log.d(TAG, "register: req made");
            mProgressBar.setVisibility(View.VISIBLE);
            Login login = new Login(this, SignUpActivity.this);
            login.login(emailString, passwordString);

        }
    }


    @Override
    public void onSuccessRequest(JSONObject result, String instanceName) {
        Log.d(TAG, "onSuccessRequest: " + result);
        int status;
        String msg;

        if (instanceName.equals("register")) {
            try {
                status = result.getInt("status");
                msg = result.getString("message");

                switch (status) {
                    case 0:
                        errorMsg.setText("Connecting!");
                        sessionManager.setIncompleteRegistrationStatus(true);
                        Intent intent = new Intent(this,CompleteRegistration.class);
                        startActivity(intent);
                        break;
                    case 1:
                        errorMsg.setText(msg);
                        break;
                    case 2:
                        errorMsg.setText(msg);
                        break;
                    case 3:
                        errorMsg.setText(msg);
                        break;
                }

            } catch (JSONException e) {
                Log.e(TAG, "onSuccessRequest: " + e);
            }
            mProgressBar.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onLoginRequestComplete(int status, String msg) {
        Log.i(TAG, "onLoginRequestComplete: " + status + msg);

        switch (status) {
            case 0:
                errorMsg.setText("loading Data!");
                Log.d(TAG, "onSuccessRequest: Login Success Redirecting !");
                sessionManager.setLogin(email.getText().toString(), password.getText().toString());
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case 1:
                Log.d(TAG, "onSuccessRequest: empty input");
                errorMsg.setText(msg);
                break;
            case 2:
                Log.d(TAG, "onSuccessRequest: Invalid pass");
                errorMsg.setText(msg);
                break;
            case 3:
                errorMsg.setText(msg);
                Intent intent1 = new Intent(this, CompleteRegistration.class);
                startActivity(intent1);
                break;
            case 4:
                errorMsg.setText(msg);
                break;
        }
        mProgressBar.setVisibility(View.INVISIBLE);
    }

}
