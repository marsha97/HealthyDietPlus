package com.project.HDPTeam.hdp.app.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.project.HDPTeam.hdp.app.Activities.FoodSearchActivity;
import com.project.HDPTeam.hdp.app.Activities.HealthyDietPlus;
import com.project.HDPTeam.hdp.app.Activities.SingleFragmentActivity;
import com.project.HDPTeam.hdp.app.R;
import com.project.HDPTeam.hdp.app.networks.CheckConnection;
import com.project.HDPTeam.hdp.app.networks.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentDetailFood#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDetailFood extends Fragment implements AdapterView.OnItemSelectedListener{
    private final String url = "http://192.168.0.111:80/hdplusdb/foodDescription.php";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "foodID_FragmentDetailFood";
    private static final String ARG_PARAM2 = "foodName_FragmentDetailFood";

    // TODO: Rename and change types of parameters
    private int spinnerPosition = -1;
    private Spinner mSpinner;
    private TextView calories, protein, carb, fat, serve;
    private String amount;
    private String[] foodNutrition = new String[4];
    private String foodName;
    private String mId;
    private ArrayAdapter<String> spinnerServing;

    public FragmentDetailFood() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentDetailFood.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentDetailFood newInstance(String param1, String param2) {
        FragmentDetailFood fragment = new FragmentDetailFood();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId= getArguments().getString(ARG_PARAM1);
            foodName = getArguments().getString(ARG_PARAM2);
        }
        //mParam1 --> food_id
        makeRequest(mId);
    }

    public void makeRequest(String query){

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Loading Content", "Please Wait...", true, false);
        RequestQueue mRequestQueue = Singleton.getIsntance().getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getUrl(query), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               // Toast.makeText(getContext(), "onResponse", Toast.LENGTH_SHORT).show();
                parseJSON(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                new CheckConnection().createInternetAccessDialog(getContext());
            }
        });
        mRequestQueue.add(request);
        mRequestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<JSONObject>() {

            @Override
            public void onRequestFinished(Request<JSONObject> request){
                //Toast.makeText(HealthyDietPlus.getContext(), "onRequestFinished", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                calories.setText(foodNutrition[0]);
                fat.setText(foodNutrition[1]);
                carb.setText(foodNutrition[2]);
                protein.setText(foodNutrition[3]);
                serve.setText(amount);
            }
        });
    }

    public void parseJSON (JSONObject response){
        if (response == null || response.length() == 0){
            return;
        }
        try {
            int servingLength;
            JSONObject mServingData = null;
            JSONObject mfood = response.getJSONObject("food");
            JSONArray mServingArr = null;
            JSONObject mServingObj = null;

            JSONObject mServings = mfood.getJSONObject("servings");
            try{
                mServingArr = mServings.getJSONArray("serving");
            }
            catch (JSONException e){
                mServingObj = mServings.getJSONObject("serving");
            }
            //Toast.makeText(getContext(), String.valueOf(mServingArr != null), Toast.LENGTH_SHORT).show();
            if (spinnerPosition == -1){
                spinnerPosition = 0;
                if (mServingArr != null){
                    servingLength = mServingArr.length();
                }
                else{
                    servingLength = 1;
                }
                String[] spinnerItem = new String[servingLength];
                for (int i = 0; i < servingLength; i++){
                    if (mServingArr != null){
                        mServingData = mServingArr.getJSONObject(i);
                        spinnerItem[i] = mServingData.getString("measurement_description");
                        amount = "1";
                    }
                    else{
                        spinnerItem[i] = "";
                        amount = mServingObj.getString("serving_description");
                        break;
                    }

                }
                if (servingLength == 1){
                    mSpinner.setVisibility(View.INVISIBLE);
                }
                else {
                    spinnerServing = new ArrayAdapter<>(HealthyDietPlus.getContext(), R.layout.spinner_item, spinnerItem);
                    spinnerServing.setDropDownViewResource(R.layout.spinner_dropdown);
                    mSpinner.setAdapter(spinnerServing);
                }
            }
            //Toast.makeText(getContext(), String.valueOf(mServingData != null), Toast.LENGTH_SHORT).show();
            if (mServingArr != null){
                mServingData = mServingArr.getJSONObject(spinnerPosition);
                if (mServingData.getString("calories") != null)
                    foodNutrition[0] = mServingData.getString("calories");
                if(mServingData.getString("fat") != null)
                    foodNutrition[1] = mServingData.getString("fat");
                if (mServingData.getString("carbohydrate")!=null)
                    foodNutrition[2] = mServingData.getString("carbohydrate");
                if (mServingData.getString("protein") != null)
                    foodNutrition[3] = mServingData.getString("protein");
            }
            else{
                if (mServingObj.getString("calories") != null)
                    foodNutrition[0] = mServingObj.getString("calories");
                if (mServingObj.getString("fat") != null)
                     foodNutrition[1] = mServingObj.getString("fat");
                if ( mServingObj.getString("carbohydrate") != null)
                    foodNutrition[2] = mServingObj.getString("carbohydrate");
                if ( mServingObj.getString("protein") != null)
                    foodNutrition[3] = mServingObj.getString("protein");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getUrl (String query){
        return url + "?food_id=" + query;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Toast.makeText(getContext(), "onCreateView", Toast.LENGTH_SHORT).show();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_food, container, false);
        mSpinner = (Spinner) view.findViewById(R.id.servingUnit_spinner);
        calories = (TextView) view.findViewById(R.id.caloriesValue_textView);
        protein = (TextView) view.findViewById(R.id.proteinValue_textView);
        carb = (TextView) view.findViewById(R.id.carbValue_textView);
        fat = (TextView) view.findViewById(R.id.fatValue_textView);
        serve = (TextView) view.findViewById(R.id.servingValue_textView);
        ((FoodSearchActivity) getActivity()).setTitleBar(foodName);
        mSpinner.setOnItemSelectedListener(this);
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       // Toast.makeText(HealthyDietPlus.getContext(), "onItemSelected", Toast.LENGTH_SHORT).show();
        spinnerPosition = position;
        if (position != 0)
            makeRequest(mId);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
