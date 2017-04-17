package com.google.volley_reglogin;

import android.support.test.InstrumentationRegistry;
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
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Created by rayleigh on 4/14/17.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityTest {

    private VolleyIdlingResource mVolleyResource;

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class);

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
    public void existingUserLogin() {
        String email = "dlee@gmail.com";
        String password = "1234";

        onView(withId(R.id.loginTextPassword)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.loginTextEmail)).perform(typeText(email), closeSoftKeyboard());

        onView(withId(R.id.loginButtonLogin)).perform(click());

        String successString = "Welcome User dlee@gmail.com";
        onView(withId(R.id.textViewUsername)).check(matches(allOf(withText(successString), isDisplayed())));
    }

    @Test
    public void newUserLogin() {
        String email = "sam11@gmail.com";
        String password = "1234";

        onView(withId(R.id.loginTextPassword)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.loginTextEmail)).perform(typeText(email), closeSoftKeyboard());

        onView(withId(R.id.loginButtonLogin)).perform(click());

        onView(withId(R.id.LoginMessage)).check(matches(withText("Login failed. User not exist!")));
    }
    @Test
    public void emptyEmailLogin() {
        String email = "";
        String password = "1234";

        onView(withId(R.id.loginTextPassword)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.loginTextEmail)).perform(typeText(email), closeSoftKeyboard());

        onView(withId(R.id.loginButtonLogin)).perform(click());

        onView(withId(R.id.LoginMessage)).check(matches(withText("Please enter email address!")));
    }
    @Test
    public void emptyPasswordLogin() {
        String email = "sam11@gmail.com";
        String password = "";

        onView(withId(R.id.loginTextPassword)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.loginTextEmail)).perform(typeText(email), closeSoftKeyboard());

        onView(withId(R.id.loginButtonLogin)).perform(click());

        onView(withId(R.id.LoginMessage)).check(matches(withText("Please enter password!")));
    }

    @After
    public void unregisterIdlingResource() {
        if(mVolleyResource != null) {
            Espresso.unregisterIdlingResources(mVolleyResource);
        }
    }
}
