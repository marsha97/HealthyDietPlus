package com.project.HDPTeam.hdp.app.Activities;

import android.app.Application;
import android.content.Context;

/**
 * Created by kali on 1/1/17.
 */

public class HealthyDietPlus extends Application {

    private static HealthyDietPlus sInstance = null;

    @Override
    public void onCreate(){
        super.onCreate();
        sInstance = this;
    }

    public static HealthyDietPlus getInstance(){
        return sInstance;
    }

    public static Context getContext(){
        return sInstance.getApplicationContext();
    }

}
