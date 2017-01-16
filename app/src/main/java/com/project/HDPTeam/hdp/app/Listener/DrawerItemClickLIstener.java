package com.project.HDPTeam.hdp.app.Listener;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.project.HDPTeam.hdp.app.Activities.FoodSearchActivity;
import com.project.HDPTeam.hdp.app.Activities.HealthyDietPlus;
import com.project.HDPTeam.hdp.app.Activities.MainMenu;
import com.project.HDPTeam.hdp.app.Activities.Slider;

/**
 * Created by kali on 1/9/17.
 */

public class DrawerItemClickLIstener implements ListView.OnItemClickListener {
    private Intent intent;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        goToActivity(position);
    }

    private void goToActivity(int position) {
        switch (position){
            case (0) : intent = new Intent(HealthyDietPlus.getContext(), MainMenu.class);
                        break;
            case (1) : intent = new Intent(HealthyDietPlus.getContext(), Slider.class);
                        break;
            case (2) : intent = new Intent(HealthyDietPlus.getContext(), FoodSearchActivity.class);
                        break;
            default: return;
        }
    }
}
