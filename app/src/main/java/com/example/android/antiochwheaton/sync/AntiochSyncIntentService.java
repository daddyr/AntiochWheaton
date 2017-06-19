package com.example.android.antiochwheaton.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Ryan on 6/10/2017.
 */

public class AntiochSyncIntentService extends IntentService {
    public AntiochSyncIntentService() {
        super("AntiochIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        AntiochSyncTask.syncData(this);
    }
}
