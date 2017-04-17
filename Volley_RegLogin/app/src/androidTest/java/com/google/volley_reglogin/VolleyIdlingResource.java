package com.google.volley_reglogin;

import android.content.Context;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.core.deps.guava.base.Preconditions;
import android.util.Log;

import static android.support.test.espresso.core.deps.guava.base.Preconditions.checkNotNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * Created by rayleigh on 4/15/17.
 */

public class VolleyIdlingResource implements IdlingResource {

    private static final String TAG = "VolleyIdlingResource";

    private final String resourceName;

    // written from main thread, read from any thread.
    private volatile ResourceCallback resourceCallback;

    private Field mCurrentRequests;
    private RequestQueue mVolleyRequestQueue;

    public VolleyIdlingResource(String resourceName) throws SecurityException, NoSuchFieldException {
        this.resourceName = Preconditions.checkNotNull(resourceName);

        mVolleyRequestQueue = ComQueue.getInstance().getRequestQueue();

        mCurrentRequests = RequestQueue.class.getDeclaredField("mCurrentRequests");
        mCurrentRequests.setAccessible(true);
    }

    @Override
    public String getName() {
        return resourceName;
    }

    @Override
    public boolean isIdleNow() {
        try {
            Set<Request> set = (Set<Request>) mCurrentRequests.get(mVolleyRequestQueue);
            if (set != null) {
                int count = set.size();
                if (count == 0) {
                    Log.d(TAG, "Volley is idle now! with: " + count);
                    resourceCallback.onTransitionToIdle();
                } else {
                    Log.d(TAG, "Not idle... " + count);
                }
                return count == 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Something went wrong.. ");
        return true;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }
}
