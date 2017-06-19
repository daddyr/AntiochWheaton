package com.example.android.antiochwheaton.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.android.antiochwheaton.data.DataContract;
import com.example.android.antiochwheaton.utilities.JsonUtils;
import com.example.android.antiochwheaton.utilities.NetworkUtils;

import java.net.URL;

/**
 * Created by Ryan on 6/9/2017.
 */

public class AntiochSyncTask {

    synchronized public static void syncData(Context context){
        try{
            URL podcastRequestUrl = NetworkUtils.buildUrl("podcast");

            String jsonPodcastRespons = NetworkUtils.getResponseFromHttpUrl(podcastRequestUrl);

            ContentValues[] podcastValues = JsonUtils.getPodcastContenValuesNamesFromJson(context, jsonPodcastRespons);

            if(podcastValues != null && podcastValues.length != 0){
                ContentResolver contentResolver = context.getContentResolver();

                contentResolver.delete(DataContract.PodcastEntry.CONTENT_URI,null,null);

                contentResolver.bulkInsert(DataContract.PodcastEntry.CONTENT_URI,podcastValues);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
