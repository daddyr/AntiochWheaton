package com.example.android.antiochwheaton.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.android.antiochwheaton.data.DataContract;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

/**
 * Created by Ryan on 6/10/2017.
 */

public class AntiochSyncUtils {

    private static final int SYNC_HOURS = 24;
    private static final int SYNC_INTERVAL_SECONDS = (int)(SYNC_HOURS * 60 * 60);
    private static final int SYNC_FLEXTIME_SECONDS = 60*60*12;
    private static final String SYNC_JOB_TAG = "antioch_sync_tag";

    private static boolean sInitialized;

    synchronized public static void scheduleSync(@NonNull final Context context){
        if(sInitialized) return;

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job constraintSyncJob = dispatcher.newJobBuilder()
                .setService(AntiochFirebaseJobService.class)
                .setTag(SYNC_JOB_TAG)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(SYNC_INTERVAL_SECONDS,SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();

        sInitialized = true;
    }

    synchronized public static void initialize(@NonNull final Context context){
        if(sInitialized) return;

        sInitialized = true;

        scheduleSync(context);

        startImmediateSync(context);
    }
    public static void startImmediateSync(Context context){
        Intent intent = new Intent(context,AntiochSyncIntentService.class);

        context.startService(intent);
    }
}
