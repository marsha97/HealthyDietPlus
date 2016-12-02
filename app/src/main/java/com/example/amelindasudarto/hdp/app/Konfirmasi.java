package com.example.amelindasudarto.hdp.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by Amelinda Sudarto on 21/11/2016.
 */

public class Konfirmasi extends Activity {
    private static int KONF_TIME_OUT = 3000; //set timer (1000 = 1 dtk)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.konfirmasi);

        //new Handler().postDelayed(new Runnable() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent j = new Intent(Konfirmasi.this, Slider.class);
                startActivity(j);
                finish();
            }
        }, KONF_TIME_OUT);
    }
}
