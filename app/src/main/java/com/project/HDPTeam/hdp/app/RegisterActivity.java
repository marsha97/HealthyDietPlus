package com.project.HDPTeam.hdp.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

/**
 * Created by Amelinda Sudarto on 21/11/2016.
 */

/** belum ada handle network error
 *
 *
 */

public class RegisterActivity extends AppCompatActivity {

    private EditText mUname; //== username;
    private EditText mPswd; // == password;
    private EditText mRetype; // == mRetype;
    //EditText date ; //== date;
    private EditText mDisplay;
    private RequestQueue mRequestQueue;
    private final String URL = "http://192.168.43.104:80/hdplusdb/register.php";
    private Button signupLink2;
    private StringRequest mStringRequest;
    private Button signupBtn;
    private Intro intro;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        mUname = (EditText) findViewById(R.id.username_editText);
        mPswd = (EditText) findViewById(R.id.password_editText);
        mRetype = (EditText) findViewById(R.id.retypePass_editText);
        //date = (EditText) findViewById(R.id.editText3);
        signupBtn = (Button) findViewById(R.id.signUp_button);
        mDisplay = (EditText) findViewById(R.id.displayName_editText);
        mRequestQueue = Volley.newRequestQueue(this);
        signupBtn.setOnClickListener(new View.OnClickListener() {

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
                            else if(mJsonObj.names().get(0).equals("errorUsername")){
                                Toast.makeText(getApplicationContext(), mJsonObj.getString("errorUsername"), Toast.LENGTH_SHORT).show();
                            }
                            else if(mJsonObj.names().get(0).equals("errorDisplayName")){
                                Toast.makeText(getApplicationContext(), mJsonObj.getString("errorDisplayName"), Toast.LENGTH_SHORT).show();
                            }
                            else if(mJsonObj.names().get(0).equals("success")){
                                intro = new Intro(getApplicationContext());
                                intro.setIsFirstTimeLaunch(true);
                                Intent intent = new Intent(RegisterActivity.this, Konfirmasi.class);
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
                    protected Map<String,String> getParams() throws AuthFailureError{
                        Map<String,String> mParameters = new HashMap<String, String>();
                        mParameters.put("username", mUname.getText().toString());
                        mParameters.put("password", mPswd.getText().toString());
                        mParameters.put("rePass", mRetype.getText().toString());
                        mParameters.put("displayName", mDisplay.getText().toString());

                        return mParameters;
                    }
                };
                mRequestQueue.add(mStringRequest);
            }
        });

    }

}
