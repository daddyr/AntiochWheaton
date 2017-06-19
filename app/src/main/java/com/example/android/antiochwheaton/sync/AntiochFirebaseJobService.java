package com.example.android.antiochwheaton.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by Ryan on 6/14/2017.
 */

public class AntiochFirebaseJobService extends JobService {

    AsyncTask mFetchDataTask;

    @Override
    public boolean onStartJob(JobParameters job) {
        mFetchDataTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                Context context = AntiochFirebaseJobService.this;
                AntiochSyncUtils.startImmediateSync(context);
                return null;
            }
        };

        mFetchDataTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if(mFetchDataTask != null) mFetchDataTask.cancel(true);

        return true;
    }
}
