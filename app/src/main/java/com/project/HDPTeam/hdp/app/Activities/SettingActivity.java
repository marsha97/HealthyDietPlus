package com.project.HDPTeam.hdp.app.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.LinearLayout;

import com.project.HDPTeam.hdp.app.R;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout aboutUs, termCon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        aboutUs = (LinearLayout) findViewById(R.id.aboutUs);
        termCon = (LinearLayout) findViewById(R.id.termCon);
        aboutUs.setOnClickListener(this);
        termCon.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (v == aboutUs){
           intent = new Intent(SettingActivity.this, AboutUsActivity.class);
        }
        else if (v == termCon){
           intent = new Intent(SettingActivity.this, TermConActivity.class);
        }
        startActivity(intent);

    }
}
