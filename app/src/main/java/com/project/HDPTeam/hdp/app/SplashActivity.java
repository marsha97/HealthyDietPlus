package com.project.HDPTeam.hdp.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Amelinda Sudarto on 21/11/2016.
 */

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000; //set timer (1000 = 1 dtk)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i;
                SharedPreferences preferences = getSharedPreferences("LogIn", Context.MODE_PRIVATE);
                Boolean isLoggedIn = preferences.getBoolean("LOGGED_IN", false);
                if (!isLoggedIn){
                    i = new Intent(SplashActivity.this, LoginActivity.class);
                }
                else{
                    i = new Intent(SplashActivity.this, MainMenu.class);
                }
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
