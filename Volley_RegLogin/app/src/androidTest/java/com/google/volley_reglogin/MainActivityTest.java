package com.google.volley_reglogin;

import android.support.test.espresso.Espresso;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by rayleigh on 4/16/17.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    private VolleyIdlingResource mVolleyResource;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        try {
            mVolleyResource = new VolleyIdlingResource("VolleyCalls");
            Espresso.registerIdlingResources(mVolleyResource);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void newUserRegister() throws Exception {

        String name = "sam11";
        String password = "1234";
        String email = "sam11@gmail.com";

        onView(withId(R.id.mainTextUsername)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.mainTextPassword)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.mainTextEmail)).perform(typeText(email), closeSoftKeyboard());

        onView(withId(R.id.mainButtonRegister)).perform(click());

        onView(withId(R.id.RegMessage)).check(matches(withText("Registration succeed. Please login!")));
    }

    @Test
    public void existingUserRegister() {
        String name = "dlee";
        String password = "1234";
        String email = "dlee@gmail.com";

        onView(withId(R.id.mainTextUsername)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.mainTextPassword)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.mainTextEmail)).perform(typeText(email), closeSoftKeyboard());

        onView(withId(R.id.mainButtonRegister)).perform(click());

        onView(withId(R.id.RegMessage)).check(matches(withText("Registration failed!")));
    }

    @After
    public void unregisterIdlingResource() {
        if(mVolleyResource != null) {
            Espresso.unregisterIdlingResources(mVolleyResource);
        }
    }

}
