package com.project.HDPTeam.hdp.app.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.project.HDPTeam.hdp.app.OtherClass.titleBar;
import com.project.HDPTeam.hdp.app.R;
import com.project.HDPTeam.hdp.app.fragments.AlertFragment;
import com.project.HDPTeam.hdp.app.fragments.EatingMenuFragment;
import com.project.HDPTeam.hdp.app.fragments.FragmentDetailFood;
import com.project.HDPTeam.hdp.app.fragments.FragmentSearchFood;
import com.project.HDPTeam.hdp.app.fragments.Schedule;
import com.project.HDPTeam.hdp.app.networks.CheckConnection;
import com.project.HDPTeam.hdp.app.networks.Singleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kali on 1/9/17.
 */

public class ManageSchedule extends SingleFragmentActivity implements Schedule.FragmentReplacement, EatingMenuFragment.FragmentReplaceMent
                                                                        , FragmentSearchFood.fragmentOperation, titleBar.titleBarOperation
                                                                        , FragmentDetailFood.editPreferences {
    private int maxCalories;
    private double currentCal;
    private String choosenMenuTime;

    @Override
    protected Fragment createFragment() {
        currentCal = getCurrCal();
        Log.d("createFragment", "currentCal = " + String.valueOf(currentCal));
        SharedPreferences physic = getSharedPreferences("PhysicData", Context.MODE_PRIVATE);
        maxCalories = physic.getInt("maxCalories", 0);
        if (maxCalories == 0) {
            getFromDatabase();
        }
        Toast.makeText(ManageSchedule.this, "currentCal : " + String.valueOf(currentCal), Toast.LENGTH_SHORT).show();
        Fragment scheduleFragment = Schedule.newInstance(maxCalories);
        return scheduleFragment;
    }

    @Override
    public void setTitleBar(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void fromSchedule(String menu) {
        choosenMenuTime = menu;
        EatingMenuFragment eatingMenuFragment = EatingMenuFragment.newInstance(choosenMenuTime);
        super.replaceFragments(eatingMenuFragment);
    }

    @Override
    public void FromEatingMenu(String menu) {
        FragmentSearchFood fragmentSearchFood = FragmentSearchFood.newInstance(R.layout.set_food_schedule, "");
        super.replaceFragments(fragmentSearchFood);
    }

    @Override
    public void replaceFragment(String mId, String title, int layoutID) {
        Fragment fragment = FragmentDetailFood.newInstance(mId, title, layoutID, currentCal, maxCalories);
        super.replaceFragments(fragment);
    }

    public void getFromDatabase() {
        String username;
        final String[] calories = new String[1];
        SharedPreferences sharedPreferences = getSharedPreferences("LogIn", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("USERNAME", "N/A");
        final String url = "http://healthydietplus.esy.es/hdplusdb/getCalories.php?username=" + username;
        RequestQueue mRequestQueue;
        mRequestQueue = Singleton.getIsntance().getRequestQueue();
        final ProgressDialog progressDialog = ProgressDialog.show(ManageSchedule.this, "Getting Content", "Please Wait...", true, false);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    calories[0] = response.getString("lastCal");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                new CheckConnection().createInternetAccessDialog(ManageSchedule.this);
            }
        });
        mRequestQueue.add(objectRequest);
        mRequestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<JSONObject>() {
            @Override
            public void onRequestFinished(Request<JSONObject> request) {
                progressDialog.dismiss();
                maxCalories = Integer.parseInt(calories[0]);
                if (maxCalories == 0) {
                    AlertFragment.createDialog("Please update your data", "It's Still Zero", ManageSchedule.this);
                } else {
                    SharedPreferences pref = getSharedPreferences("PhysicData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("maxCalories", maxCalories);
                    editor.commit();
                    AlertFragment.createDialog("Please restart the application", "Get the Last Data", ManageSchedule.this);
                }
                Toast.makeText(ManageSchedule.this, "maxCal : " + String.valueOf(maxCalories), Toast.LENGTH_SHORT).show();
                Toast.makeText(ManageSchedule.this, "calories[0] : " + calories[0], Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void FromDetailFood(String foodName, double calories, double totalCal) {
       Log.d("FromDetailFood", "totalCal = " + String.valueOf(totalCal));
        addToPreferences (foodName,calories,totalCal);
    }

    public void addToPreferences(String foodName, double calories, double totalCal) {
        currentCal = calories + currentCal;
        Log.d("addToPreferences", "totalCal = " + String.valueOf(totalCal));
        Toast.makeText(HealthyDietPlus.getContext(), "totalCal = " + String.valueOf(totalCal), Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = getSharedPreferences(choosenMenuTime, Context.MODE_PRIVATE);
        SharedPreferences caloriesPreferences = getSharedPreferences("caloriesManagement", Context.MODE_PRIVATE);

        String sizeKey = choosenMenuTime + "Size";
        int menuSize = getSize() + 1;
        String calKey = choosenMenuTime + "Cal" + String.valueOf(menuSize);
        String foodKey = choosenMenuTime + "Food" + String.valueOf(menuSize);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        SharedPreferences.Editor caloriesEditor = caloriesPreferences.edit();

        editor.putString(calKey, String.valueOf(calories));
        caloriesEditor.putString("totalCal", String.valueOf(currentCal));
        editor.putInt(sizeKey, menuSize);
        editor.putString(foodKey, foodName);
        editor.commit();
        caloriesEditor.commit();
    }


    public void updateMenu (ArrayList<String> menu, int position){
        String foodKey = choosenMenuTime+"Food";
        SharedPreferences foodMenuPref = getSharedPreferences(choosenMenuTime, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = foodMenuPref.edit();
        editor.remove(foodKey+ String.valueOf(position+1));
        for (int i = 1; i <= menu.size(); i++){
            foodKey +=String.valueOf(i);
            editor.putString(foodKey, menu.get(i-1));
        }
        editor.commit();
    }



    public void updateCalories (ArrayList<String> calories, double foodCalories, int position){
        String caloriesKey = choosenMenuTime + "Cal";
        SharedPreferences caloriesPref = getSharedPreferences(choosenMenuTime, Context.MODE_PRIVATE);
        SharedPreferences.Editor caloriesEdit = caloriesPref.edit();

        caloriesEdit.remove(caloriesKey+String.valueOf(position+1));

        SharedPreferences totalCalPref = getSharedPreferences("caloriesManagement", Context.MODE_PRIVATE);
        SharedPreferences.Editor totalEditor = totalCalPref.edit();

        currentCal = getCurrCal() - foodCalories;
        for (int  i = 1 ; i <= calories.size(); i++){
            caloriesKey += String.valueOf(i);
            caloriesEdit.putString(caloriesKey,calories.get(i-1) );
        }
        caloriesEdit.commit();

        totalEditor.putString("totalCal", String.valueOf(currentCal));
        totalEditor.commit();

    }

    public HashMap<HashMap<String, String>, HashMap<String, String>> generateFoodCalHashMap() {
        SharedPreferences sharedPreferences = getSharedPreferences(choosenMenuTime, Context.MODE_PRIVATE);
        int size = getSize();

        HashMap<HashMap<String, String>, HashMap<String, String>> foodCal = new HashMap<>();
        HashMap<String, String> foodNameMap = new HashMap<>();
        HashMap<String, String> foodCalMap = new HashMap<>();
        for (int i = 1; i <= size; i++) {
            String foodKey = choosenMenuTime + "Food" + String.valueOf(i);
            String calKey = choosenMenuTime + "Cal" + String.valueOf(i);
            String foodName = sharedPreferences.getString(foodKey, "N/A");
            String calories = sharedPreferences.getString(calKey + String.valueOf(i), "N/A");
            if ((!foodName.equals("N/A")) && (!calories.equals("N/A"))){
                foodNameMap.put(foodKey, foodName);
                foodCalMap.put(calKey, calories);
                foodCal.put(foodNameMap, foodCalMap);
            }
        }
        return foodCal;
    }

    public void decreaseSize(){
        String sizeKey = choosenMenuTime +"Size";
        int newSize = getSize() - 1;
        SharedPreferences preferences = getSharedPreferences(choosenMenuTime, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(sizeKey, newSize);
        editor.commit();
    }

    public ArrayList<String> getFoodNames(){
        SharedPreferences sharedPreferences = getSharedPreferences(choosenMenuTime, Context.MODE_PRIVATE);
        int size = getSize();
        ArrayList<String> foodName = new ArrayList<>(size);
        for (int i = 1 ; i <= size; i++){
            String foodNameItem = sharedPreferences.getString(choosenMenuTime+"Food"+String.valueOf(i), "N/A");
            if (!foodNameItem.equals("N/A")){
                foodName.add(foodNameItem);
            }
        }
        return foodName;
    }
    public ArrayList<String> getCalories(){
        SharedPreferences sharedPreferences = getSharedPreferences(choosenMenuTime, Context.MODE_PRIVATE);
        int size = getSize();
        ArrayList<String> calories = new ArrayList<>(size);
        int  j = 0;
        for (int i = 1; i <= size; i++){
            String caloriesItem = sharedPreferences.getString(choosenMenuTime+"Cal"+String.valueOf(i), "");
            if (!caloriesItem.equals("")){
                calories.add(caloriesItem);
            }
        }
        return calories;
    }
    public int getSize (){
        SharedPreferences sharedPreferences = getSharedPreferences(choosenMenuTime, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(choosenMenuTime+"Size",0);
    }

    public double getCurrCal(){
        SharedPreferences sharedPreferences = getSharedPreferences("caloriesManagement", Context.MODE_PRIVATE);
        Log.d("getCurrCal", sharedPreferences.getString("totalCal", "0"));
        return Double.parseDouble(sharedPreferences.getString("totalCal", "0"));
    }
    public void addTime(String time, int hour, int minute){
        // TODO will add hour and minute if needed
        SharedPreferences sharedPreferences  = getSharedPreferences(choosenMenuTime, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor  = sharedPreferences.edit();
        editor.putString(choosenMenuTime+"Time", time);
        editor.commit();
    }
    public String getTime(){
        SharedPreferences timePreference = getSharedPreferences(choosenMenuTime, Context.MODE_PRIVATE);
        return timePreference.getString(choosenMenuTime+"Time", "Set Eating Time");
    }
}
