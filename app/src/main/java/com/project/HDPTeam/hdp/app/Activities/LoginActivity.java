package com.project.HDPTeam.hdp.app.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project.HDPTeam.hdp.app.R;
import com.project.HDPTeam.hdp.app.networks.CheckConnection;
import com.project.HDPTeam.hdp.app.networks.Singleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity{

        private Button loginButton, signupLink;
        private EditText mUname, mPassword;
        private final String URL = "http://192.168.0.111:80/hdplusdb/login.php"; //baru bisa local,yang register juga
        private StringRequest mStringRequest;
        private RequestQueue mRequestQueue;
        private ProgressBar mLoading;
        private RelativeLayout mLoadingCont;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            mUname = (EditText) findViewById(R.id.username_editText);
            mPassword = (EditText) findViewById(R.id.password_editText);

            loginButton = (Button) findViewById(R.id.login_btn);
            signupLink = (Button) findViewById(R.id.register_btn);

            mLoadingCont = (RelativeLayout) findViewById(R.id.loading_container);
            mLoadingCont.setVisibility(View.INVISIBLE);

            mRequestQueue = Singleton.getIsntance().getRequestQueue();
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, "Loading Content", "Please Wait...", true, false);
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
                                    SharedPreferences preferences = getSharedPreferences("LogIn", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putBoolean("LOGGED_IN", true);
                                    editor.putString("USERNAME", mUname.getText().toString());
                                    editor.commit();

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
                            volleyError.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Network error, please check your connection", Toast.LENGTH_SHORT).show();
                            new CheckConnection().createInternetAccessDialog(LoginActivity.this);

                        }
                    }){
                        @Override
                        protected Map<String,String> getParams() throws AuthFailureError {
                            Map<String,String> mParameters = new HashMap<>();
                            mParameters.put("username", mUname.getText().toString());
                            mParameters.put("password", mPassword.getText().toString());
                            return mParameters;
                        }
                    };
                    mRequestQueue.add(mStringRequest);
                    mRequestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {

                        @Override
                        public void onRequestFinished(Request<String> request){
                         progressDialog.dismiss();
                        }
                    });
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
