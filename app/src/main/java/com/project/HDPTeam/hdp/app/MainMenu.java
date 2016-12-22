package com.project.HDPTeam.hdp.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity implements View.OnClickListener{

    private Button mUpdate, mLast, mFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mUpdate = (Button) findViewById(R.id.update_button);
        mUpdate.setOnClickListener(this);

        mLast = (Button) findViewById(R.id.last_button);
        mLast.setOnClickListener(this);

        mFood = (Button) findViewById(R.id.foodList_button);
        mFood.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        if (view == mUpdate){
            Intent intent = new Intent(this, UpdatePhysic.class);
            startActivity(intent);
        }
        //yang lain nanti...
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
