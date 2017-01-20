package com.project.HDPTeam.hdp.app.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by kali on 1/17/17.
 */

public class notification extends AppCompatActivity {
    public SharedPreferences notifPreferences;
    public SharedPreferences.Editor notifEditor;

    public void buildPreferences(){
        notifPreferences = getSharedPreferences("notification", Context.MODE_PRIVATE);
        notifEditor = notifPreferences.edit();
    }

    public int getSize (String type){
        return notifPreferences.getInt(type+"Size" , 0);
    }

    public void increaseSize (String type){
        int size = getSize(type) + 1;
        notifEditor.putInt(type+"Size", size);
        notifEditor.commit();
    }

    public void decreaseSize (String type){
        int size = getSize(type) - 1;
        notifEditor.putInt(type+"Size", size);
        notifEditor.commit();
    }

    public void addTime (int min, int hour, String type){
        int size = getSize(type);
        notifEditor.putString("notifTime"+ String.valueOf(size), hour + " : " + min);
        notifEditor.commit();
    }


}
