package com.project.HDPTeam.hdp.app.model;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kali on 1/5/17.
 */

public class foodDatas {
    private static foodDatas sFoodDatas;

    private ArrayList<foodData> mFoodDatas = new ArrayList<>();

    public static foodDatas get (Context context, ArrayList<String> foodName, ArrayList<Long> id){
        sFoodDatas = new foodDatas(context, foodName, id);
        return sFoodDatas;
    }

    private foodDatas(Context context, ArrayList<String> foodName, ArrayList<Long> id){
        for (int i = 0; i < foodName.size(); i++){
            foodData mFoodData = new foodData();
            mFoodData.setFoodName(foodName.get(i));
            mFoodData.setId(id.get(i));
            mFoodDatas.add(mFoodData);
        }
    }

    public ArrayList<foodData> getFoodDatas(){
        return mFoodDatas;
    }

    private foodData getFoodData(long id){
        for (foodData mFoodData : mFoodDatas){
            if (id == mFoodData.getId()){
                return mFoodData;
            }
        }
        return null;
    }

    @Override
    public String toString(){
        String text = "";
        ArrayList<foodData> datas = getFoodDatas();
        for (foodData data : datas){
             text += data.getFoodName() + "\n";
        }
        return text;

    }


}
