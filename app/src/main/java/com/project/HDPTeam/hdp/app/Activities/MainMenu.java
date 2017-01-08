package com.project.HDPTeam.hdp.app.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;

import com.project.HDPTeam.hdp.app.R;

public class MainMenu extends AppCompatActivity implements View.OnClickListener{

    private ImageView mUpdate, mLast, mFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mUpdate = (ImageView) findViewById(R.id.update_button);
        mUpdate.setOnClickListener(this);

        mLast = (ImageView) findViewById(R.id.last_button);
        mLast.setOnClickListener(this);

        mFood = (ImageView) findViewById(R.id.foodList_button);
        mFood.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        if (view == mUpdate){
            Intent intent = new Intent(this, UpdatePhysic.class);
            startActivity(intent);
        }
        else if (view == mLast){
            Intent intent = new Intent(this, latestUpdateActivity.class);
            startActivity(intent);
        }
        else if (view == mFood){
            Intent intent = new Intent(this, FoodSearchActivity.class);
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

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }
}
