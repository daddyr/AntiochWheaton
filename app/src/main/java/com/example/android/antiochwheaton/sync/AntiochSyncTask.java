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

            URL podcastRequestUrl = NetworkUtils.buildUrl(NetworkUtils.PATH_PODCAST);

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
            
            ids = context.getContentResolver().query(DataContract.MediaEntry.CONTENT_URI,
                    new String[]{DataContract.MediaEntry._ID},
                    null,null,null);
            number = ids.getCount();
            ids.close();
            
            URL mediaRequestUrl = NetworkUtils.buildUrl(NetworkUtils.PATH_MEDIA);
            
            String jsonMediaRespone = NetworkUtils.getResponseFromHttpUrl(mediaRequestUrl);
            
            ContentValues[] mediaValues = JsonUtils.getMediaContenValuesNamesFromJson(context, jsonMediaRespone);
            
            if(mediaValues != null && mediaValues.length != 0 && mediaValues.length > number){
                ContentResolver contentResolver = context.getContentResolver();
                contentResolver.delete(DataContract.MediaEntry.CONTENT_URI,null,null);
                contentResolver.bulkInsert(DataContract.MediaEntry.CONTENT_URI,mediaValues);
            }
            
            ids = context.getContentResolver().query(DataContract.TagsEntry.CONTENT_URI,
                    new String[]{DataContract.TagsEntry._ID},null,null,null);
            number = ids.getCount();
            ids.close();

            URL tagsRequestUrl = NetworkUtils.buildUrl(NetworkUtils.PATH_TAGS);

            String jsontagsRespone = NetworkUtils.getResponseFromHttpUrl(tagsRequestUrl);

            ContentValues[] tagsValues = JsonUtils.getTagsContenValuesNamesFromJson(context, jsontagsRespone);

            if(tagsValues != null && tagsValues.length != 0 && tagsValues.length > number){
                ContentResolver contentResolver = context.getContentResolver();
                contentResolver.delete(DataContract.TagsEntry.CONTENT_URI,null,null);
                contentResolver.bulkInsert(DataContract.TagsEntry.CONTENT_URI,tagsValues);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
