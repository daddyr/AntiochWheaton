package com.example.android.antiochwheaton.utilities;

import android.content.Context;
import android.database.Cursor;

import com.example.android.antiochwheaton.data.DataContract;

/**
 * Created by Ryan on 6/6/2017.
 */

public class AntiochUtilties {

    public static String formattedURL(String url){
        String returnString = url.replace("\\","");
        return returnString;
    }

    public static String formattedImageURL(String content){
        String[] items = content.split(" ");
        String s;
        for(int i = 0; i < items.length;i++){
            s = items[i];
        }
        String returnString = items[12];
        return returnString;
    }

    public static String formattedSummaryURL(String content){
        String[] items = content.split("p>");
        String s;
        for(int i = 0;i < items.length;i++){
            s = items[i];
        }

        String returnString = items[1];
        return returnString;
    }

    public static String getAuthor(Context context, String authorId){
        Cursor tagsCursor = context.getContentResolver().query(DataContract.TagsEntry.buildTagsUriWithId(authorId),
                new String[]{DataContract.TagsEntry.COLUMN_VALUE},null,null,null);

        String author = "";
        if(tagsCursor.moveToFirst()){
            author = tagsCursor.getString(0);
        }

        tagsCursor.close();

        return author;
    }

    public static String getImageUrl(Context context, String mediaId){
        String imageURL = "";

        Cursor mediaCursor = context.getContentResolver().query(DataContract.MediaEntry.buildMediaUriWithId(mediaId),
                new String[]{DataContract.MediaEntry.COLUMN_URL},null,null,null);

        if(mediaCursor.moveToFirst()){
            imageURL = mediaCursor.getString(0);
        }

        mediaCursor.close();

        return imageURL;
    }
}
