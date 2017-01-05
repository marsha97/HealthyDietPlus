package com.project.HDPTeam.hdp.app.networks;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.project.HDPTeam.hdp.app.Activities.HealthyDietPlus;

/**
 * Created by kali on 1/1/17.
 */

public class Singleton {

    private static Singleton sInstance = null;
    private static RequestQueue mRequestQueue;

    public Singleton (){
        mRequestQueue = Volley.newRequestQueue(HealthyDietPlus.getContext());
    }

    public static Singleton getIsntance(){
        if (sInstance == null){
            sInstance = new Singleton();
        }
        return sInstance;
    }

    public static RequestQueue getRequestQueue(){
        return mRequestQueue;
    }

}
