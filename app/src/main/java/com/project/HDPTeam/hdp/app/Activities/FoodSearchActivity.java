package com.project.HDPTeam.hdp.app.Activities;

import android.support.v4.app.Fragment;

import com.project.HDPTeam.hdp.app.fragments.FragmentSearchFood;

public class FoodSearchActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment(){
        FragmentSearchFood fragmentSearchFood = FragmentSearchFood.newInstance("","");
        return fragmentSearchFood;
    }

    public void setTitleBar (String title){
        getSupportActionBar().setTitle(title);
    }

}
