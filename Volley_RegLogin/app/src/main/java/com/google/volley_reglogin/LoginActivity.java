package com.google.volley_reglogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rayleigh on 3/15/17.
 */

public class LoginActivity extends AppCompatActivity {

    public static final String LOGIN_URL_OFFSET = "/login?email=%1$s&password=%2$s";

    public static final String KEY_EMAIL="email";
    public static final String KEY_PASSWORD="password";

    private static final String TAG = "Login_Activity";

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;

    private TextView messageToShow;

    private String email;
    private String password;

    private String LOGIN_URL;

    private ComQueue helper = ComQueue.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Context context = this;

        LOGIN_URL = context.getResources().getString(R.string.Ip_address) + LOGIN_URL_OFFSET;

        editTextEmail = (EditText) findViewById(R.id.loginTextEmail);
        editTextPassword = (EditText) findViewById(R.id.loginTextPassword);

        messageToShow = (TextView) findViewById(R.id.LoginMessage);

        buttonLogin = (Button) findViewById(R.id.loginButtonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
    }

    private void userLogin() {
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            messageToShow.setText("Please enter password!");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            messageToShow.setText("Please enter email address!");
            return;
        }

        String mURL = String.format(LOGIN_URL, email, password);

        StringRequest userObjReq = new StringRequest(Request.Method.GET, mURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle access token.
                        Log.d(TAG, "Login received: " + response);
                        long token = Long.parseLong(response);
                        if(token == 0) {
                            // Toast.makeText(LoginActivity.this, R.string.loginfail_toast, Toast.LENGTH_LONG).show();
                            messageToShow.setText("Login failed. User not exist!");
                        } else {
                            openProfile();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error message: " + error.toString());
                        Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                });

        userObjReq.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        helper.add(userObjReq);
    }

    private void openProfile(){
        Intent intent = new Intent(this, ActivityUserProfile.class);
        intent.putExtra(KEY_EMAIL, email);
        startActivity(intent);
    }

}
