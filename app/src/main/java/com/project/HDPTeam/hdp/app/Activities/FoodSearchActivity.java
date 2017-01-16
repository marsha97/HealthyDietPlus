package com.project.HDPTeam.hdp.app.Activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.project.HDPTeam.hdp.app.OtherClass.titleBar;
import com.project.HDPTeam.hdp.app.R;
import com.project.HDPTeam.hdp.app.fragments.FragmentDetailFood;
import com.project.HDPTeam.hdp.app.fragments.FragmentSearchFood;

public class FoodSearchActivity extends SingleFragmentActivity implements FragmentSearchFood.fragmentOperation, titleBar.titleBarOperation{
    @Override
    protected Fragment createFragment(){
        FragmentSearchFood fragmentSearchFood = FragmentSearchFood.newInstance(R.layout.detail_food_fragment_test,"");
        return fragmentSearchFood;
    }

    public  void  replaceFragment(String mId, String title, int layoutID){
        FragmentDetailFood foodInfo = FragmentDetailFood.newInstance(mId, title, layoutID, 0,0);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, foodInfo);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void setTitleBar (String title){
        getSupportActionBar().setTitle(title);
    }
}
