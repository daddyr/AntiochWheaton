package com.example.android.antiochwheaton.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ryan on 6/5/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "antioch.db";
    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_PODCAST_TABLE = "CREATE TABLE " + DataContract.PodcastEntry.TABLE_NAME + " (" +
                DataContract.PodcastEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DataContract.PodcastEntry.COLUMN_WP_ID + " TEXT NOT NULL UNIQUE," +
                DataContract.PodcastEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                DataContract.PodcastEntry.COLUMN_DATE + " TEXT NOT NULL," +
                DataContract.PodcastEntry.COLUMN_AUTHOR + " TEXT NOT NULL," +
                DataContract.PodcastEntry.COLUMN_IMAGE_URL + " TEXT NOT NULL," +
                DataContract.PodcastEntry.COLUMN_PODCAST_URL + " TEXT NOT NULL," +
                DataContract.PodcastEntry.COLUMN_SUMMARY + " TEXT NOT NULL" +

                ");";

        db.execSQL(SQL_CREATE_PODCAST_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.PodcastEntry.TABLE_NAME);
        onCreate(db);
    }
}
