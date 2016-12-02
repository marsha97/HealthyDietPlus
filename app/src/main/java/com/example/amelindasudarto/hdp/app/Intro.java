package com.example.amelindasudarto.hdp.app;

/**
 * Created by Amelinda Sudarto on 28/11/2016.
 */

import android.content.Context;
import android.content.SharedPreferences;

public class Intro {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _contex;

    //shared pref mode
    int PRIVATE_MODE = 0;

    //shared preferances file name
    private static final String PREF_NAME = "TeQue";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLauch";

    public Intro(Context context){
        this._contex = context;
        pref = _contex.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setIsFirstTimeLaunch(boolean isFirstTimeLaunch){
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTimeLaunch);
        editor.commit();
    }

    public boolean isFirstTimeLaunch(){
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);

    }
}
