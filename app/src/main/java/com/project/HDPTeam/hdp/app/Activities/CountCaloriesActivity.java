package com.project.HDPTeam.hdp.app.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.HDPTeam.hdp.app.R;
import com.project.HDPTeam.hdp.app.networks.CheckConnection;
import com.project.HDPTeam.hdp.app.networks.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CountCaloriesActivity extends AppCompatActivity implements View.OnClickListener {
    private RequestQueue mRequestQueue;
    private TextView mIdealWeight, mIdealCal, mBurnedCal;
    private Double getIdealWeight, getIdealCal;
    private String mUname;
    private Button mSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_calories);
        mUname = getIntent().getStringExtra("username");
        mIdealCal = (TextView) findViewById(R.id.idealWeightResult_textView);
        mIdealWeight = (TextView) findViewById(R.id.idealCalResult_textView);
        mBurnedCal = (TextView) findViewById(R.id.idealBurnedResult_textView);
        mIdealWeight = (TextView) findViewById(R.id.idealWeightResult_textView);
        mIdealCal = (TextView) findViewById(R.id.idealCalResult_textView);
        final ProgressDialog progressDialog = ProgressDialog.show(CountCaloriesActivity.this, "Loading Content", "Please Wait...", true, false);
        String showURL = "http://healthydietplus.esy.es/hdplusdb/showPhysic.php?username="+mUname;
        mSchedule = (Button) findViewById(R.id.shcedule_button);
        mSchedule.setOnClickListener(this);
        mRequestQueue = Singleton.getIsntance().getRequestQueue();
        StringRequest jsonStringRequest = new StringRequest(Request.Method.GET, showURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    JSONArray physics = json.getJSONArray("physics");
                    JSONObject getPhysic = physics.getJSONObject(0);
                    getIdealCal = getPhysic.getDouble("ideal_cal");
                    getIdealWeight = getPhysic.getDouble("ideal_weight");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                new CheckConnection().createInternetAccessDialog(CountCaloriesActivity.this);
            }
        });
        mRequestQueue.add(jsonStringRequest);
        mRequestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<JSONObject>() {

            @Override
            public void onRequestFinished(Request<JSONObject> request){
                writeToPreferences();
               mBurnedCal.setText(String.valueOf(getIdealCal));
               mIdealCal.setText(String.valueOf(getIdealCal));
               mIdealWeight.setText(String.valueOf(getIdealWeight) + " kg");
                progressDialog.dismiss();
            }
        });

    }
    public void writeToPreferences(){
        SharedPreferences physicPref = getSharedPreferences("PhysicData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = physicPref.edit();
        editor.putInt("maxCalories", getIdealCal.intValue());
        editor.putString("idealWeight", String.valueOf(getIdealCal));
        editor.commit();
    }
    @Override
    public void onClick(View view){
        Intent intent = new Intent(CountCaloriesActivity.this, MainMenu.class);
        startActivity(intent);
    }
}
