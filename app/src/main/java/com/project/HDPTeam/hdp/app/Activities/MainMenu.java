package com.project.HDPTeam.hdp.app.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.project.HDPTeam.hdp.app.R;

import org.w3c.dom.Text;

public class MainMenu extends NavDrawerActivity implements View.OnClickListener{

    private ImageView mUpdate, mLast, mFood, mSchedule;
    private String[] option = {"","Log Out"};
    private  SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_nav_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences("LogIn", Context.MODE_PRIVATE);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,  toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hview = navigationView.inflateHeaderView(R.layout.nav_header_nav_drawer);
        TextView name = (TextView)hview.findViewById(R.id.name);

        name.setText(sharedPreferences.getString("DISPLAY_NAME", "N/A"));
        navigationView.setNavigationItemSelectedListener(this);

        mUpdate = (ImageView) findViewById(R.id.update_button);
        mUpdate.setOnClickListener(this);

        mLast = (ImageView) findViewById(R.id.last_button);
        mLast.setOnClickListener(this);

        mSchedule= (ImageView) findViewById(R.id.schedule_button);
        mSchedule.setOnClickListener(this);
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
        else if (view == mSchedule){
            Intent intent = new Intent(this, ManageSchedule.class);
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
