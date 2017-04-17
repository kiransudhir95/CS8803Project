package com.google.volley_reglogin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_NAME = "username";

    private static final String TAG = "Main_Activity";
    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;

    private TextView messageToShow;

    private Button buttonRegister;
    private Button buttonLogin;

    private String name;
    private String password;
    private String email;

    private String REGISTER_URL;

    private ComQueue helper = ComQueue.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = this;

        REGISTER_URL = context.getResources().getString(R.string.Ip_address);

        editTextUsername = (EditText) findViewById(R.id.mainTextUsername);
        editTextPassword = (EditText) findViewById(R.id.mainTextPassword);
        editTextEmail= (EditText) findViewById(R.id.mainTextEmail);

        messageToShow = (TextView) findViewById(R.id.RegMessage);

        buttonRegister = (Button) findViewById(R.id.mainButtonRegister);
        buttonLogin = (Button) findViewById(R.id.mainButtonLogin);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
    }

    private void registerUser(){

        name = editTextUsername.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        email = editTextEmail.getText().toString().trim();

        Map<String, String> userobj = new HashMap<String, String>();
        userobj.put(KEY_EMAIL,email);
        userobj.put(KEY_PASSWORD,password);
        userobj.put(KEY_NAME,name);

        GenericRequest jsonObjReq = new GenericRequest(Request.Method.POST, REGISTER_URL, String.class, userobj,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle access token.
                        Log.d(TAG, "Register received: " + response);
                        long token = Long.parseLong(response);
                        // String token = response;
                        if(token == 0) {
                            Log.d(TAG, "Received 0!");
                            // Toast.makeText(MainActivity.this, R.string.registerfail_toast, Toast.LENGTH_LONG).show();
                            messageToShow.setText("Registration failed!");
                        } else {
                            // Login into the profile
                            // openProfile();
                            Log.d(TAG, "Register success!");
                            // Toast.makeText(MainActivity.this, R.string.Welcome, Toast.LENGTH_LONG).show();
                            messageToShow.setText("Registration succeed. Please login!");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        helper.add(jsonObjReq);
    }

    /*
    private void openProfile(){
        Intent intent = new Intent(this, ActivityUserProfile.class);
        intent.putExtra(KEY_EMAIL, email);
        startActivity(intent);
    }
    */
}
