package com.example.android.antiochwheaton.utilities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;

import com.example.android.antiochwheaton.PodcastDetail;
import com.example.android.antiochwheaton.R;
import com.example.android.antiochwheaton.data.AntiochWheatonPreferences;
import com.example.android.antiochwheaton.data.DataContract;

/**
 * Created by Ryan on 6/14/2017.
 */

public class NotificationUtils {

    public static final String[] PODCAST_NOTIFICATION_PROJECTION = {
            DataContract.PodcastEntry.COLUMN_WP_ID,
            DataContract.PodcastEntry.COLUMN_TITLE,
            DataContract.PodcastEntry.COLUMN_DATE,
            DataContract.PodcastEntry.COLUMN_IMAGE_URL
    };

    public static final int INDEX_WP_ID = 0;
    public static final int INDEX_TITLE = 1;
    public static final int INDEX_DATE = 2;
    public static final int INDEX_IMAGE = 3;

    public static final int PODCAST_NOTIFICATION_ID = 777;

    public static void notifyUserofNewPodcast(Context context, String id){
        Uri podcastUri = DataContract.PodcastEntry.buildPodcastUriWithId(id);

        Cursor latestPodcast = context.getContentResolver().query(podcastUri,
                PODCAST_NOTIFICATION_PROJECTION,
                null,
                null,
                null);

        if(latestPodcast.moveToFirst()){
            String title = latestPodcast.getString(INDEX_TITLE);
            String date = latestPodcast.getString(INDEX_DATE);

            String notificationTitle = context.getString(R.string.app_name);

            String notificationText = date + " " + title;

            Intent detailPodcastIntent = new Intent(context, PodcastDetail.class);

            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
            taskStackBuilder.addNextIntentWithParentStack(detailPodcastIntent);
            PendingIntent podcastPI = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setColor(ContextCompat.getColor(context,R.color.colorPrimary))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationText)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationText))
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setContentIntent(podcastPI)
                    .setAutoCancel(true);

            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(PODCAST_NOTIFICATION_ID, notificationBuilder.build());

            AntiochWheatonPreferences.setLastNotificationTime(context, System.currentTimeMillis());
        }

        latestPodcast.close();
    }
}
