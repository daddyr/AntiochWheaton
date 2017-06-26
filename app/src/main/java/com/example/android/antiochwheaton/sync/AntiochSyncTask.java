package com.example.android.antiochwheaton.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.format.DateUtils;

import com.example.android.antiochwheaton.data.AntiochWheatonPreferences;
import com.example.android.antiochwheaton.data.DataContract;
import com.example.android.antiochwheaton.utilities.JsonUtils;
import com.example.android.antiochwheaton.utilities.NetworkUtils;
import com.example.android.antiochwheaton.utilities.NotificationUtils;

import java.net.URL;

/**
 * Created by Ryan on 6/9/2017.
 */

public class AntiochSyncTask {

    synchronized public static void syncData(Context context){
        try{

            String[] projection = {DataContract.PodcastEntry._ID};

            Cursor ids = context.getContentResolver().query(DataContract.PodcastEntry.CONTENT_URI,projection,null,null,null);
            int number = ids.getCount();
            ids.close();

            URL podcastRequestUrl = NetworkUtils.buildUrl("podcast");

            String jsonPodcastRespons = NetworkUtils.getResponseFromHttpUrl(podcastRequestUrl);

            ContentValues[] podcastValues = JsonUtils.getPodcastContenValuesNamesFromJson(context, jsonPodcastRespons);

            if(podcastValues != null && podcastValues.length != 0  && podcastValues.length > number){
                ContentResolver contentResolver = context.getContentResolver();

                contentResolver.delete(DataContract.PodcastEntry.CONTENT_URI,null,null);

                contentResolver.bulkInsert(DataContract.PodcastEntry.CONTENT_URI,podcastValues);

                long timeSinceLastNotification = AntiochWheatonPreferences.getEllapsedTimeSinceLastNotification(context);

                if(timeSinceLastNotification >= DateUtils.DAY_IN_MILLIS){
                    ids = context.getContentResolver().query(DataContract.PodcastEntry.CONTENT_URI,projection,null,null,null);
                    if(ids.moveToFirst()) {
                        String id = String.valueOf(ids.getInt(0));
                        NotificationUtils.notifyUserofNewPodcast(context,id);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
