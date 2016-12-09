package com.project.HDPTeam.hdp.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

        private Button loginButton, signupLink;
        private EditText mUname, mPassword;
        private int test;
        private final String URL = "http://192.168.43.104:80/hdplusdb/login.php";
        private StringRequest mStringRequest;
        private RequestQueue mRequestQueue;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            mUname = (EditText) findViewById(R.id.inputUsername);
            mPassword = (EditText) findViewById(R.id.inputPass);

            loginButton = (Button) findViewById(R.id.logInBtn);
            signupLink = (Button) findViewById(R.id.pageRegBtn);

            mRequestQueue = Volley.newRequestQueue(this);

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject mJsonObj = new JSONObject(response);
                                if(mJsonObj.names().get(0).equals("errorEmpty")){
                                    Toast.makeText(getApplicationContext(), mJsonObj.getString("errorEmpty"),Toast.LENGTH_SHORT).show();
                                }
                                else if (mJsonObj.names().get(0).equals("errorPass")) {
                                    Toast.makeText(getApplicationContext(), mJsonObj.getString("errorPass"), Toast.LENGTH_SHORT).show();
                                }
                                else if(mJsonObj.names().get(0).equals("errorUsername")) {
                                    Toast.makeText(getApplicationContext(), mJsonObj.getString("errorUsername"), Toast.LENGTH_SHORT).show();
                                }
                                else if(mJsonObj.names().get(0).equals("success")){
                                    Intent intent = new Intent(LoginActivity.this, Slider.class);
                                    startActivity(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }){
                        @Override
                        protected Map<String,String> getParams() throws AuthFailureError {
                            Map<String,String> mParameters = new HashMap<String, String>();
                            mParameters.put("username", mUname.getText().toString());
                            mParameters.put("password", mPassword.getText().toString());
                            return mParameters;
                        }
                    };
                    mRequestQueue.add(mStringRequest);
                }
            });

            signupLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(register);
                    finish();
                }
            });
        }
}
