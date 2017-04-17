package com.google.volley_reglogin;


import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by rayleigh on 4/4/17.
 */

public class ComQueue extends Application {
    private static RequestQueue mRequestQueue;
    private static ComQueue mInstance;

    public static final String TAG = ComQueue.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    public static synchronized ComQueue getInstance() {
        return mInstance;
    }

    public static synchronized RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("Not initialized");
        }

    }

    public <T> void add(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancel() {
        mRequestQueue.cancelAll(TAG);
    }
}
