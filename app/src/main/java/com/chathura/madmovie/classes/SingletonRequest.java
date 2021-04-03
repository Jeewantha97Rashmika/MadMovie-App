package com.chathura.madmovie.classes;


import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class SingletonRequest {

    private static SingletonRequest mInstance;
    private Context mContext;
    private RequestQueue mRequestQueue;

    public SingletonRequest(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized SingletonRequest getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SingletonRequest(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }
}

