package com.project.HDPTeam.hdp.app.Activities;

import android.content.Intent;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import android.view.MenuItem;
import com.project.HDPTeam.hdp.app.R;

public class NavDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.tutorial) {
            Intro intro = new Intro(this);
            intro.setIsFirstTimeLaunch(true);
            Intent intent = new Intent(this, Slider.class);
            startActivity(intent);
        } else if (id == R.id.foodListMenu) {
            Intent intent = new Intent(this, FoodSearchActivity.class);
            startActivity(intent);
        }  else if (id == R.id.settings) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.notification) {
            Intent intent = new Intent(this, ReminderActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
