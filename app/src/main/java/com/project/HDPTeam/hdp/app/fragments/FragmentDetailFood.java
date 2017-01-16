package com.project.HDPTeam.hdp.app.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.project.HDPTeam.hdp.app.Activities.HealthyDietPlus;
import com.project.HDPTeam.hdp.app.OtherClass.titleBar;
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
    private final String url = "http://healthydietplus.esy.es/hdplusdb/foodDescription.php";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "foodID_FragmentDetailFood";
    private static final String ARG_PARAM2 = "foodName_FragmentDetailFood";
    private static final String ARG_PARAM3 = "layoutID_FragmentDetailFood";
    private static final String ARG_PARAM4 = "currentCalories_FragmentDetailFood";
    private static final String ARG_PARAM5 = "maxCalories_FragmentDetailFood";

    // TODO: Rename and change types of parameters
    private int spinnerPosition = -1;
    private Integer layoutID;
    private Spinner mSpinner;
    private TextView calories, protein, carb, fat, serve, title;
    private EditText customServe;
    private double customAmount;
    private String amount;
    private String[] foodNutrition = new String[4];
    private String foodName;
    private String mId;
    private String caloriesVal, fatVal, proteinVal, carboVal;
    private ArrayAdapter<String> spinnerServing;
    private Button addMenu;
    private double maxCal, currentCal;
    private Integer menuSize;

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
    public static FragmentDetailFood newInstance(String param1, String param2, int param3, double param4, double param5) {
        FragmentDetailFood fragment = new FragmentDetailFood();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putInt(ARG_PARAM3, param3);
        args.putDouble (ARG_PARAM4, param4);
        args.putDouble(ARG_PARAM5,param5);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId= getArguments().getString(ARG_PARAM1);
            foodName = getArguments().getString(ARG_PARAM2);
            layoutID = getArguments().getInt(ARG_PARAM3);
            currentCal = getArguments().getDouble(ARG_PARAM4);
            maxCal = getArguments().getDouble(ARG_PARAM5);
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
                countAllData();
                setText();
                if (customServe == null){
                    if (changetoDouble(amount) % 1 == 0){
                        Double d = changetoDouble(amount);
                        Integer i = d.intValue();
                        serve.setText(String.valueOf(i));
                    }
                    else {
                        serve.setText(amount);
                    }
                }
                progressDialog.dismiss();
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
            //inisialisasi spinner
            if (spinnerPosition == -1){
                spinnerPosition = 0;
                if (mServingArr != null){
                    servingLength = mServingArr.length();
                    mServingData = mServingArr.getJSONObject(0);
                    amount = mServingData.getString("number_of_units");
                }
                else{
                    servingLength = 1;
                }
                String[] spinnerItem = new String[servingLength];
                for (int i = 0; i < servingLength; i++){
                    if (mServingArr != null){
                        mServingData = mServingArr.getJSONObject(i);
                        spinnerItem[i] = mServingData.getString("measurement_description");
                    }
                    else{
                        String servingDesc =  mServingObj.getString("measurement_description")+ " (" +mServingObj.getString("serving_description")+")";
                        spinnerItem[i] = servingDesc;
                        amount = mServingObj.getString("number_of_units");
                        break;
                    }

                }
                customAmount = changetoDouble(amount);
                spinnerServing = new ArrayAdapter<>(HealthyDietPlus.getContext(), R.layout.spinner_item, spinnerItem);
                spinnerServing.setDropDownViewResource(R.layout.spinner_dropdown);
                mSpinner.setAdapter(spinnerServing);
            }
            //ambil data dari input user
            if (mServingArr != null){
                mServingData = mServingArr.getJSONObject(spinnerPosition);
                amount = mServingData.getString("number_of_units");
            }
            if (customServe != null){
                if (customServe.getText().toString().equals("")){
                    Double d = changetoDouble(amount);
                    if (d % 1 == 0){
                        customServe.setText(String.valueOf(d.intValue()));
                    }
                    else {
                        customServe.setText(amount);
                    }
                }
                customAmount = changetoDouble(customServe.getText().toString());
            }
            else {
                customAmount = changetoDouble(amount);
            }

            if (mServingArr != null){
                mServingData = mServingArr.getJSONObject(spinnerPosition);
                caloriesVal = mServingData.getString("calories");
                fatVal = mServingData.getString("fat");
                carboVal = mServingData.getString("carbohydrate");
                proteinVal = mServingData.getString("protein");
            }
            else{
                caloriesVal = mServingObj.getString("calories");
                fatVal = mServingObj.getString("fat");
                carboVal = mServingObj.getString("carbohydrate");
                proteinVal = mServingObj.getString("protein");
            }
            Log.d("parsedJSON", "caloriesVal is : " + caloriesVal);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void countAllData(){
        foodNutrition[0] = String.valueOf(countNutrition(changetoDouble(caloriesVal)));
        foodNutrition[1] = String.valueOf(countNutrition(changetoDouble(fatVal)));
        foodNutrition[2] = String.valueOf(countNutrition(changetoDouble(carboVal)));
        foodNutrition[3] = String.valueOf(countNutrition(changetoDouble(proteinVal)));
    }

    private void setText (){
        calories.setText(foodNutrition[0]);
        fat.setText(foodNutrition[1]);
        carb.setText(foodNutrition[2]);
        protein.setText(foodNutrition[3]);
    }

    private Double changetoDouble (String text){
        Log.d("changetoDouble", "text is : " + text);

        return Double.parseDouble(text);
    }

    private Double countNutrition (double nutrition){

        Log.d("countNutrition", "customAmount is : " + String.valueOf(customAmount));

        return Math.round(nutrition/changetoDouble(amount)*customAmount*100.0)/100.0;
    }
    public String getUrl (String query){
        return url + "?food_id=" + query;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Toast.makeText(getContext(), "onCreateView", Toast.LENGTH_SHORT).show();
        // Inflate the layout for this fragment
        View view = inflater.inflate(layoutID, container, false);
        mSpinner = (Spinner) view.findViewById(R.id.servingUnit_spinner);
        calories = (TextView) view.findViewById(R.id.caloriesValue_textView);
        protein = (TextView) view.findViewById(R.id.proteinValue_textView);
        carb = (TextView) view.findViewById(R.id.carbValue_textView);
        fat = (TextView) view.findViewById(R.id.fatValue_textView);
        title = (TextView) view.findViewById(R.id.title);
        if (layoutID == R.layout.detail_food_fragment_test) {
            serve = (TextView) view.findViewById(R.id.servingValue_textView);
        }
        else if (layoutID == R.layout.set_food_schedule){
            customServe = (EditText) view.findViewById(R.id.servingValue_editText);
            addMenu = (Button) view.findViewById(R.id.addMenu_button);
            customServe.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String str = String.valueOf(s);
                    if (!str.equals(""))
                        customAmount = changetoDouble(str);

                    else
                        customAmount = 0.0;
                    if ((caloriesVal != null) &&(fatVal!=null) && (carboVal != null) && (proteinVal != null)){
                        countAllData();
                    }
                    setText();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            addMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double newCal = currentCal + changetoDouble(foodNutrition[0]);
                    Toast.makeText(getContext(),"currentCal = " + String.valueOf(currentCal), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(),"newCal = " + String.valueOf(newCal), Toast.LENGTH_SHORT).show();
                    if (maxCal < newCal){
                        AlertFragment.createDialog("Please eat less !!", "Too Much Calories", getContext());
                    }
                    else{
                        // TODO tambah foodname, banyak kalorinya ke preference
                        // TODO update current calories di preference
                        double calories = changetoDouble(foodNutrition[0]);
                        ((editPreferences) getActivity()).FromDetailFood(foodName, calories, newCal);
                        AlertFragment.createDialog("List updated !", "Done!", getContext());
                    }
                }
            });
        }
        title.setText(foodName);
        ((titleBar.titleBarOperation) getActivity()).setTitleBar("Food Info");
        mSpinner.setOnItemSelectedListener(this);
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(HealthyDietPlus.getContext(), String.valueOf(spinnerPosition), Toast.LENGTH_SHORT).show();
        spinnerPosition = position;
        makeRequest(mId);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public interface editPreferences {
       public void FromDetailFood(String foodName, double calories, double totalCal);
    }
}