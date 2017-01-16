package com.project.HDPTeam.hdp.app.Activities;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.project.HDPTeam.hdp.app.Listener.DrawerItemClickLIstener;

import com.project.HDPTeam.hdp.app.Adapter.ListAdapter;
import com.project.HDPTeam.hdp.app.R;
import android.view.View;

public class withDrawer extends AppCompatActivity {
    private ListView mListView;
    private DrawerLayout mDrawerLayout;
    private String menu[] = {"Calories Control", "Tutorial", "Food List", "Notifications", "Settings"};
    private int imageID[]= {R.drawable.ic_action_name, R.drawable.ic_action_name, R.drawable.foodlist_fix, R.drawable.ic_notifications_black_24dp, R.drawable.ic_action_setting};
    private ListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_drawer);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mActionBarToogle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.navigation_drawer_open, R.string.navigation_drawer_close){

        };

        mListView = (ListView)findViewById(R.id.left_drawer);
       // mAdapter = new ListAdapter(HealthyDietPlus.getContext(), menu, imageID);
        //mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new DrawerItemClickLIstener());
    }
}
