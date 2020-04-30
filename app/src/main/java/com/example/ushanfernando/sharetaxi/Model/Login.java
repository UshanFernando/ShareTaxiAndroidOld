package com.example.ushanfernando.sharetaxi.Model;

import android.content.Context;
import android.util.Log;

import com.example.ushanfernando.sharetaxi.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login implements STApiRequest.OnRequestComplete {

    private static final String TAG = "Login";
//    private static final String loginUrl = "http://10.0.2.2:8080/server/login.php";

    //public server
    private static final String loginUrl = "http://ushan.comze.com/api/login.php";

    SessionManager sessionManager;


    Context mContext;

    private final OnLoginRequestComplete mOnLoginRequestComplete;

    public interface OnLoginRequestComplete {
        public void onLoginRequestComplete(int status, String msg);
    }

    public Login(Context context,  OnLoginRequestComplete onLoginRequestComplete) {
        mOnLoginRequestComplete = onLoginRequestComplete;
        mContext = context;
        sessionManager = new SessionManager(context);
    }

    public void login(String email, String password) {

        Map<String, String> loginRequestTags = new HashMap<String, String>();
        loginRequestTags.put("email", email);
        loginRequestTags.put("password", password);

        STApiRequest login = new STApiRequest(Login.this, loginUrl, "login");
        login.newRequest(mContext, loginRequestTags);
        Log.d(TAG, "login: req made");

    }

    @Override
    public void onSuccessRequest(JSONObject result, String instanceName) {
        Log.d(TAG, "onSuccessRequest: " + result);
        int status;
        String msg;
        if (instanceName.equals("login")) {
            try {
                status = result.getInt("status");
                msg = result.getString("message");
                mOnLoginRequestComplete.onLoginRequestComplete(status, msg);

            }catch (JSONException e) {
                Log.e(TAG, "onSuccessRequest: Cant parse Json"+e );
            }
        }
    }
}
