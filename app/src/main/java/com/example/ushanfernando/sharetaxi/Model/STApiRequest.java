package com.example.ushanfernando.sharetaxi.Model;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.ushanfernando.sharetaxi.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;


public class STApiRequest {
        private static final String TAG = "Api Request";
        private  final String mUrl;
        private  final String mInstanceName;


        private final OnRequestComplete mOnRequestComplete;


    public  interface OnRequestComplete {
            void onSuccessRequest(JSONObject result,String instanceName);
        }


        public STApiRequest(OnRequestComplete onRegisterRequestComplete,String url,String instanceName ) {
            mOnRequestComplete = onRegisterRequestComplete;
            mUrl =url;
            mInstanceName =instanceName;
            Log.d(TAG, "STApiRequest: construct called");
        }

        public void newRequest(Context context,  Map<String ,String> mRequestTags) {


            JSONObject request = new JSONObject();
            try {
                //Populate the request parameters
                Set< Map.Entry< String,String> > me = mRequestTags.entrySet();
                for (Map.Entry< String,String> rq:me){
                    request.put(rq.getKey(),rq.getValue());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                    (Request.Method.POST, mUrl, request, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

//                        pDialog.dismiss();

                                Log.d(TAG, "onResponse: "+response);
                                //Check if user got registered successfully
                                mOnRequestComplete.onSuccessRequest(response,mInstanceName);

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
//                        pDialog.dismiss();

                            //Display error message whenever an error occurs
                            Log.e(TAG, "onErrorResponse: "+error );

                        }
                    });

            // Access the RequestQueue through your singleton class.
            RequestHandler.getInstance(context).addToRequestQueue(jsArrayRequest);

        }




    }


