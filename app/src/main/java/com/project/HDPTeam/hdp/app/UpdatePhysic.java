package com.project.HDPTeam.hdp.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdatePhysic extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    private Spinner mSpinner;
    private Button mUpdate;
    private RelativeLayout mLoadingCont;
    private ProgressBar mLoading;
    private final String URL = "http://192.168.0.102:80/hdplusdb/physic.php"; //baru bisa local,yang register juga
    private StringRequest mStringRequest;
    private RequestQueue mRequestQueue;
    private EditText mWeight, mHeight;
    private String mUname, mIntensity;
    private  int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_physic);

        mSpinner = (Spinner) findViewById(R.id.workout_spinner);
        ArrayAdapter<CharSequence> workoutAdapter =ArrayAdapter.createFromResource(this,R.array.workout_intensity,android.R.layout.simple_spinner_item);
        workoutAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(workoutAdapter);
        mSpinner.setOnItemSelectedListener(this);

        mUpdate = (Button) findViewById(R.id.update_button);

        mLoadingCont = (RelativeLayout) findViewById(R.id.loading_container);
        mLoadingCont.setVisibility(View.INVISIBLE);

        mWeight =(EditText)findViewById(R.id.weight_editText);
        mHeight = (EditText)findViewById(R.id.height_editText);
        SharedPreferences sharedPreferences = getSharedPreferences("LogIn", Context.MODE_PRIVATE);
        mUname = sharedPreferences.getString("USERNAME", "N/A");
        mRequestQueue = Volley.newRequestQueue(this);

        mUpdate.setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
        if(view == mUpdate){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            mLoadingCont.setVisibility(View.VISIBLE);
            mLoading = (ProgressBar) findViewById(R.id.loading_circle);
            mLoading.setIndeterminate(true);
            mStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject mJsonObj = new JSONObject(response);
                        if(mJsonObj.names().get(0).equals("errorEmpty")){
                            Toast.makeText(getApplicationContext(), mJsonObj.getString("errorEmpty"),Toast.LENGTH_SHORT).show();
                        }
                        else if(mJsonObj.names().get(0).equals("success")){
                            Toast.makeText(getApplicationContext(), mJsonObj.getString("success"),Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UpdatePhysic.this, MainMenu.class);
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
                    new CheckConnection().createInternetAccessDialog(UpdatePhysic.this);

                }
            }){
                @Override
                protected Map<String,String> getParams() throws AuthFailureError {
                    Map<String,String> mParameters = new HashMap<>();
                    mParameters.put("username", mUname);
                    mParameters.put("weight", String.valueOf(mWeight.getText()));
                    mParameters.put("height", String.valueOf(mHeight.getText()));
                    mParameters.put("intensity", mIntensity);
                    return mParameters;
                }
            };
            mRequestQueue.add(mStringRequest);
            mRequestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
                @Override
                public void onRequestFinished(Request<String> request){
                    if(mLoadingCont != null && mLoadingCont.isShown()){
                        mLoadingCont.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view,  int position, long id){
        mIntensity = mSpinner.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
