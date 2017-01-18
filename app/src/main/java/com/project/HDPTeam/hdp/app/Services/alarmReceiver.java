package com.project.HDPTeam.hdp.app.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.project.HDPTeam.hdp.app.Activities.MainMenu;
import com.project.HDPTeam.hdp.app.Activities.ManageSchedule;
import com.project.HDPTeam.hdp.app.R;

/**
 * Created by kali on 1/18/17.
 */

public class alarmReceiver extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate (){
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this.getApplicationContext(), ManageSchedule.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification mNotif = new Notification.Builder(this)
                .setContentTitle("Healthy Diet+")
                .setContentText("It's Time to Eat")
                .setSmallIcon(R.drawable.time_eat)
                .setContentIntent(pendingIntent)
                .setSound(sound)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(23,mNotif);
    }
}
