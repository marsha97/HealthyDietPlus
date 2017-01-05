package com.project.HDPTeam.hdp.app.networks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.project.HDPTeam.hdp.app.fragments.AlertFragment;

/**
 * Created by kali on 12/15/16.
 */

public class CheckConnection {

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
    public void createInternetAccessDialog(Context context){
        String message,title;
        title = "Network Error";
        if(isNetworkAvailable(context)){
            message = "Server Error, please try again later.";
        }
        else {
            message = "No connection. Check your connection and try again";
        }
        AlertFragment.createDialog(message,title,context);
    }
}
