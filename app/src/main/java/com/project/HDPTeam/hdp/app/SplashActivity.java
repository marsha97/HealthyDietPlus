package com.project.HDPTeam.hdp.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

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
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
