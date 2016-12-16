package com.project.HDPTeam.hdp.app;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Amelinda Sudarto on 21/11/2016.
 */

/** belum ada handle network error
 *
 *
 */

public class RegisterActivity extends AppCompatActivity implements
        View.OnClickListener {

    private EditText mUname; //== username;
    private EditText mPswd; // == password;
    private EditText mRetype; // == mRetype;
    //EditText date ; //== date;
    private EditText mDisplay;
    private RequestQueue mRequestQueue;
    private final String URL = "http://192.168.0.113:80/hdplusdb/register.php";
    private StringRequest mStringRequest;
    private Button signupBtn;
    private Intro intro;
    private Button mDateBtn;
    private Button mGenderBtn;
    private int mYear, mMonth, mDay;
    private String mGender = "";
    private String mDay2, mMonth2, mYear2; //tanggal yang diinputkan user

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        mUname = (EditText) findViewById(R.id.username_editText);
        mPswd = (EditText) findViewById(R.id.password_editText);
        mRetype = (EditText) findViewById(R.id.reType_editText);
        //date = (EditText) findViewById(R.id.editText3);
        signupBtn = (Button) findViewById(R.id.register_button);
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
                        volleyError.printStackTrace();
                        volleyError.printStackTrace();
                        //Toast.makeText(getApplicationContext(),"Network error, please check your connection", Toast.LENGTH_SHORT).show();
                        new CheckConnection().createInternetAccessDialog(RegisterActivity.this);

                        return;
                    }
                }){
                    @Override
                    protected Map<String,String> getParams() throws AuthFailureError{
                        Map<String,String> mParameters = new HashMap<String, String>();
                        mParameters.put("username", mUname.getText().toString());
                        mParameters.put("password", mPswd.getText().toString());
                        mParameters.put("rePass", mRetype.getText().toString());
                        mParameters.put("displayName", mDisplay.getText().toString());
                        mParameters.put("day", String.valueOf(mDay2));
                        mParameters.put("month", String.valueOf(mMonth2));
                        mParameters.put("year", String.valueOf(mYear2));
                        mParameters.put("gender", mGender);

                        return mParameters;
                    }
                };
                mRequestQueue.add(mStringRequest);
            }
        });

        mDateBtn = (Button) findViewById(R.id.birth_button);
        mGenderBtn = (Button) findViewById(R.id.gender_button);
        mDateBtn.setOnClickListener(this);
        mGenderBtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        if (v == mDateBtn) {
            //current date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    mDay2 = String.valueOf(dayOfMonth);
                    mMonth2 = String.valueOf(month);
                    mYear2 = String.valueOf(year);
                    mDateBtn.setText(dayOfMonth + "-" + (month+1) + "-" + year);
                }
            }, mYear,mMonth,mDay);
            datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            datePickerDialog.setTitle("Date of Birth");
            datePickerDialog.show();
        }
        else if(v == mGenderBtn){
            final AlertDialog genderDialog;
            final String[] items = {"Male", "Female"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Gender");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case 0:
                            mGender = items[0];
                            break;
                        case 1:
                            mGender = items[1];
                            break;
                    }
                    mGenderBtn.setText(mGender);
                    dialog.dismiss();
                }
            });
            genderDialog = builder.create();
            genderDialog.show();
        }
    }
}
