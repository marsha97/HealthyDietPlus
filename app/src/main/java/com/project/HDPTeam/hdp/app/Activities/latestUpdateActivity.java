package com.project.HDPTeam.hdp.app.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.HDPTeam.hdp.app.R;
import com.project.HDPTeam.hdp.app.fragments.lastUpdateFragment;
import com.project.HDPTeam.hdp.app.networks.CheckConnection;
import com.project.HDPTeam.hdp.app.networks.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class latestUpdateActivity extends FragmentActivity {
    private RequestQueue mRequestQueue;
    private String mUname, mLastDate, mIntensity;
    private double mWeight, mHeight;
    private String[] intensities;
    private int maxCalories;
    private double idealWeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lastes_update);
        SharedPreferences sharedPreferences = getSharedPreferences("LogIn", Context.MODE_PRIVATE);
        mUname = sharedPreferences.getString("USERNAME", "N/A");
        intensities = getResources().getStringArray(R.array.workout_intensity);

        String showURL = "http://healthydietplus.esy.es/hdplusdb/showLatest.php?username="+mUname;

        mRequestQueue = Singleton.getIsntance().getRequestQueue();
        final ProgressDialog progressDialog = ProgressDialog.show(latestUpdateActivity.this, "Loading Content", "Please Wait...", true, false);
        StringRequest jsonStringRequest = new StringRequest(Request.Method.GET, showURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    JSONArray lasts = json.getJSONArray("physics");
                    JSONObject getLast = lasts.getJSONObject(0);
                    mWeight = getLast.getDouble("weight");
                    mHeight = getLast.getDouble("height");
                    int position = getLast.getInt("workout");
                    mLastDate = getLast.getString("last_update");
                    maxCalories = getLast.getInt("ideal_cal");
                    idealWeight = getLast.getDouble("ideal_weight");
                    mIntensity = intensities[position];

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                new CheckConnection().createInternetAccessDialog(latestUpdateActivity.this);
            }
        });
        mRequestQueue.add(jsonStringRequest);
        mRequestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<JSONObject>() {

            @Override
            public void onRequestFinished(Request<JSONObject> request){
                progressDialog.dismiss();
                writeToPreferences();
                if (!isFinishing())
                    makeLastFragment();
            }
        });

    }

    public void writeToPreferences(){
        SharedPreferences physicPref = getSharedPreferences("PhysicData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = physicPref.edit();
        editor.putInt("maxCalories", maxCalories);
        editor.putFloat("idealWeight", (float)idealWeight);
        editor.putFloat("weight", (float)mWeight);
        editor.commit();
    }

    public void makeLastFragment(){
        lastUpdateFragment mLastUpdate = new lastUpdateFragment();
        FragmentManager manager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("weight", String.valueOf(mWeight));
        bundle.putString("idealWeight", String.valueOf(idealWeight));
        bundle.putString("lastUpdate", mLastDate);
        bundle.putString("intensity", mIntensity);
        mLastUpdate.setArguments(bundle);
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.add(R.id.last_update, mLastUpdate, "countCalories");
        fragmentTransaction.commitAllowingStateLoss();
    }
}
