package com.example.android.antiochwheaton.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Ryan on 6/6/2017.
 */

public class AntiochProvider extends ContentProvider{

    public static final int CODE_PODCASTS = 100;
    public static final int CODE_PODCAST_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMather();

    DbHelper mOpenHelper;

    public static UriMatcher buildUriMather(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(DataContract.CONTENT_AUTORITY,DataContract.PATH_PODCASTS,CODE_PODCASTS);
        uriMatcher.addURI(DataContract.CONTENT_AUTORITY,DataContract.PATH_PODCASTS + "/#",CODE_PODCAST_WITH_ID);

        return uriMatcher;
    }
    @Override
    public boolean onCreate() {
        mOpenHelper = new DbHelper(getContext());
        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)){
            case CODE_PODCASTS:
                db.beginTransaction();
                int rowsInserted = 0;
                try{
                    for (ContentValues value:values){
                        long _id = db.insert(DataContract.PodcastEntry.TABLE_NAME,null,value);
                        if(_id != -1){
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }

                if(rowsInserted > 0){
                    getContext().getContentResolver().notifyChange(uri,null);
                }

                return rowsInserted;
            default:
                return super.bulkInsert(uri,values);

        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int match = sUriMatcher.match(uri);
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        Cursor cursor;

        switch (match){
            case CODE_PODCASTS:
                cursor = db.query(DataContract.PodcastEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_PODCAST_WITH_ID:
                String id = uri.getPathSegments().get(1);
                cursor = db.query(DataContract.PodcastEntry.TABLE_NAME,
                        projection,
                        DataContract.PodcastEntry.COLUMN_WP_ID + "=?",
                        new String[]{id},null,null,sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: + " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        int tasksDeleted;

        switch (sUriMatcher.match(uri)){
            case CODE_PODCASTS:
                tasksDeleted = db.delete(DataContract.PodcastEntry.TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        if(tasksDeleted != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return tasksDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}