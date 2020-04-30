package com.example.ushanfernando.sharetaxi;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class SimpleRequest {

    private static final String TAG = "Simple Request";
    private  final String mUrl;
    private final OnRequestComplete mOnRequestComplete;


   public interface OnRequestComplete {
        public void onSuccessRequest(String result);
    }


    public SimpleRequest(OnRequestComplete onRegisterRequestComplete, String url ) {
        mOnRequestComplete = onRegisterRequestComplete;
        mUrl =url;
    }


    public void newRequestEmpty(Context context) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, mUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        mOnRequestComplete.onSuccessRequest(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                textView.setText("That didn't work!");
            }
        });

        // Access the RequestQueue through your singleton class.
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);

    }

}
