package com.example.ushanfernando.sharetaxi;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ushanfernando.sharetaxi.Util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class Register {
    private static final String TAG = "Register";
    private static final String register_url= Constants.Domain+"/api/signup.php";
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";

    private final OnRegisterRequestComplete mOnRegisterRequestComplete;

    interface OnRegisterRequestComplete {
        public void onSuccessRegisterRequest(int status,String msg);
    }

    public Register(OnRegisterRequestComplete onRegisterRequestComplete) {
        mOnRegisterRequestComplete = onRegisterRequestComplete;
    }

    public void registerUser(Context context, String email, String password) {

        Log.d(TAG, "registerUser: function called!");
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put("email", email);
            request.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, register_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

//                        pDialog.dismiss();
                        try {
                            Log.d(TAG, "onResponse: "+response);
                            //Check if user got registered successfully
                            mOnRegisterRequestComplete.onSuccessRegisterRequest(response.getInt(KEY_STATUS),response.getString(KEY_MESSAGE));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        pDialog.dismiss();

                        //Display error message whenever an error occurs

                    }
                });

        // Access the RequestQueue through your singleton class.
        RequestHandler.getInstance(context).addToRequestQueue(jsArrayRequest);

    }

}
