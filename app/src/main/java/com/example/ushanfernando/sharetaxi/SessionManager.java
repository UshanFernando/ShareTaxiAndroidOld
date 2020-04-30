package com.example.ushanfernando.sharetaxi;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SessionManager {

    private static final String EMAIL_KEY = "email";
    private static final String PASSWORD_KEY = "password";
    private static final String COMP_REG_REQ_KEY = "regCompleteRequired";

    private SharedPreferences prefs;

    public SessionManager(Context context) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setLogin(String email, String password) {
        prefs.edit().putString(EMAIL_KEY, email).commit();
        prefs.edit().putString(PASSWORD_KEY, password).commit();
    }

    public void logout() {
        prefs.edit().clear().commit();
    }

    public String getEmail() {
        String email = prefs.getString(EMAIL_KEY,null);
        return email;
    }

    public String getPassword() {
        String pass = prefs.getString(PASSWORD_KEY,null);
        return pass;
    }

    public boolean getIncompleteRegistrationStatus() {
        boolean state = prefs.getBoolean(COMP_REG_REQ_KEY,false);
        return state;
    }

    public void setIncompleteRegistrationStatus(boolean status) {

            prefs.edit().putBoolean(COMP_REG_REQ_KEY,status);
    }



}
