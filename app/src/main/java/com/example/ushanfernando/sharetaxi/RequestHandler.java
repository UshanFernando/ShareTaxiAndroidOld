package com.example.ushanfernando.sharetaxi;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestHandler {



        private static final String TAG = RequestHandler.class
                .getSimpleName();

        private RequestQueue mRequestQueue;
        private static RequestHandler mInstance;

        public RequestHandler (Context context) {
            if (mRequestQueue == null) {
                mRequestQueue = Volley.newRequestQueue(context);
            }
        }

        public static synchronized RequestHandler getInstance(Context context) {
            if (mInstance == null) {
                mInstance = new RequestHandler(context);
            }
            return mInstance;
        }

        public RequestQueue getRequestQueue() {
            return mRequestQueue;
        }

        public <T> void addToRequestQueue(Request<T> req, String tag) {
            // set the default tag if tag is empty
            req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
            getRequestQueue().add(req);
        }

        public <T> void addToRequestQueue(Request<T> req) {
            Log.d(TAG, "addToRequestQueue: called!");
            req.setTag(TAG);
            getRequestQueue().add(req);
        }

        public void cancelPendingRequests(Object tag) {
            if (mRequestQueue != null) {
                mRequestQueue.cancelAll(tag);
            }
        }
    }

