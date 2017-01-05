package com.project.HDPTeam.hdp.app.model;

/**
 * Created by kali on 1/1/17.
 */

public class foodData {

    private long mId;
    private String mFoodName;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getFoodName() {
        return mFoodName;
    }

    public void setFoodName(String foodName) {
        mFoodName = foodName;
    }

    @Override
    public String toString(){
        return "Id = " + mId + "\n" + "Food Name = " + mFoodName + "\n\n";
    }

}
