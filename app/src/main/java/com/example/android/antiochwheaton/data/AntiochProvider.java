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
    public static final int CODE_BLOGS = 110;
    public static final int CODE_BLOGS_WITH_ID = 111;
    public static final int CODE_EVENTS = 120;
    public static final int CODE_EVENTS_WITH_ID = 121;
    public static final int CODE_MEDIA = 200;
    public static final int CODE_MEDIA_WITH_ID = 201;
    public static final int CODE_TAGS = 300;
    public static final int CODE_TAGS_WITH_ID = 301;

    private static final UriMatcher sUriMatcher = buildUriMather();

    DbHelper mOpenHelper;

    // DONE: 8/21/2017 - update Content provider with Blogs
    // DONE: 8/21/2017 - update Content provider with Events

    public static UriMatcher buildUriMather(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(DataContract.CONTENT_AUTORITY,DataContract.PATH_PODCASTS,CODE_PODCASTS);
        uriMatcher.addURI(DataContract.CONTENT_AUTORITY,DataContract.PATH_PODCASTS + "/#",CODE_PODCAST_WITH_ID);
        uriMatcher.addURI(DataContract.CONTENT_AUTORITY,DataContract.PATH_MEDIA,CODE_MEDIA);
        uriMatcher.addURI(DataContract.CONTENT_AUTORITY,DataContract.PATH_MEDIA + "/#",CODE_MEDIA_WITH_ID);
        uriMatcher.addURI(DataContract.CONTENT_AUTORITY,DataContract.PATH_TAGS,CODE_TAGS);
        uriMatcher.addURI(DataContract.CONTENT_AUTORITY,DataContract.PATH_TAGS + "/#",CODE_TAGS_WITH_ID);
        uriMatcher.addURI(DataContract.CONTENT_AUTORITY,DataContract.PATH_POSTS,CODE_BLOGS);
        uriMatcher.addURI(DataContract.CONTENT_AUTORITY,DataContract.PATH_POSTS + "/#",CODE_BLOGS_WITH_ID);
        uriMatcher.addURI(DataContract.CONTENT_AUTORITY,DataContract.PATH_EVENTS,CODE_EVENTS);
        uriMatcher.addURI(DataContract.CONTENT_AUTORITY,DataContract.PATH_EVENTS + "/#",CODE_EVENTS_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DbHelper(getContext());
        return true;
    }

    private int insertHelper(String tableName, @NonNull ContentValues[] values, @NonNull Uri uri){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        db.beginTransaction();
        int rowsInserted = 0;
        try{
            for (ContentValues value:values){
                long _id = db.insert(tableName,null,value);
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
    }
    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        switch (sUriMatcher.match(uri)){
            case CODE_PODCASTS:
                return insertHelper(DataContract.PodcastEntry.TABLE_NAME,values,uri);
            case CODE_MEDIA:
                return insertHelper(DataContract.MediaEntry.TABLE_NAME,values,uri);
            case CODE_TAGS:
                return insertHelper(DataContract.TagsEntry.TABLE_NAME,values,uri);
            case CODE_BLOGS:
                return insertHelper(DataContract.BlogEntry.TABLE_NAME,values,uri);
            case CODE_EVENTS:
                return insertHelper(DataContract.EventsEntry.TABLE_NAME,values,uri);
            default:
                return super.bulkInsert(uri,values);

        }
    }

    
    //// done: 6/27/2017 add media, tags cases to query
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
            case CODE_MEDIA:
                cursor = db.query(DataContract.MediaEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_MEDIA_WITH_ID:
                id = uri.getPathSegments().get(1);
                cursor = db.query(DataContract.MediaEntry.TABLE_NAME,
                        projection,
                        DataContract.MediaEntry.COLUMN_WP_ID + "=?",
                        new String[]{id},null,null,sortOrder);
                break;
            case CODE_TAGS:
                cursor = db.query(DataContract.TagsEntry.TABLE_NAME,
                        projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case CODE_TAGS_WITH_ID:
                id = uri.getPathSegments().get(1);
                cursor = db.query(DataContract.TagsEntry.TABLE_NAME,
                        projection,
                        DataContract.TagsEntry.COLUMN_WP_ID + "=?",
                        new String[]{id},null,null,sortOrder);
                break;
            case CODE_BLOGS:
                cursor = db.query(DataContract.BlogEntry.TABLE_NAME,
                        projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case CODE_BLOGS_WITH_ID:
                id = uri.getPathSegments().get(1);
                cursor = db.query(DataContract.BlogEntry.TABLE_NAME,
                        projection,
                        DataContract.BlogEntry.COLUMN_WP_ID + "=?",
                        new String[]{id},null,null,sortOrder);
                break;
            case CODE_EVENTS:
                cursor = db.query(DataContract.EventsEntry.TABLE_NAME,
                        projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case CODE_EVENTS_WITH_ID:
                id = uri.getPathSegments().get(1);
                cursor = db.query(DataContract.EventsEntry.TABLE_NAME,
                        projection,
                        DataContract.EventsEntry.COLUMN_WP_ID + "=?",
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

    //// done: 6/27/2017 add media, tags case to delete
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        int tasksDeleted;

        switch (sUriMatcher.match(uri)){
            case CODE_PODCASTS:
                tasksDeleted = db.delete(DataContract.PodcastEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case CODE_MEDIA:
                tasksDeleted = db.delete(DataContract.MediaEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case CODE_TAGS:
                tasksDeleted = db.delete(DataContract.TagsEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case CODE_BLOGS:
                tasksDeleted = db.delete(DataContract.BlogEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case CODE_EVENTS:
                tasksDeleted = db.delete(DataContract.EventsEntry.TABLE_NAME,selection,selectionArgs);
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
