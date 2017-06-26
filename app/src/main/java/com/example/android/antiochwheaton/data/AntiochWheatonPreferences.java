package com.example.android.antiochwheaton.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.android.antiochwheaton.R;

/**
 * Created by Ryan on 6/23/2017.
 */

public class AntiochWheatonPreferences {

    public static void setLastNotificationTime(Context context, long timeOfNotification){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        String lastNotificationKey = context.getResources().getString(R.string.pref_last_notification);
        editor.putLong(lastNotificationKey,timeOfNotification);
        editor.apply();
    }

    public static long getLastNotificationTimeInMillis(Context context){
        String lastNotificationKey = context.getResources().getString(R.string.pref_last_notification);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        long lastNotificationtime = sharedPreferences.getLong(lastNotificationKey,0);

        return lastNotificationtime;
    }

    public static long getEllapsedTimeSinceLastNotification(Context context){
        long lastNotificationTimeMillis = getLastNotificationTimeInMillis(context);

        long timeSinceLastNotification = System.currentTimeMillis() - lastNotificationTimeMillis;
        return timeSinceLastNotification;
    }
}
