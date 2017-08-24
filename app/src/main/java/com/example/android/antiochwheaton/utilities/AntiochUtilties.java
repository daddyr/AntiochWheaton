package com.example.android.antiochwheaton.utilities;

import android.content.Context;
import android.database.Cursor;

import com.example.android.antiochwheaton.data.DataContract;


public class AntiochUtilties {


    static String formattedURL(String url){
        return url.replace("\\","");
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

    //formatted date for meta only
    static String getFormattedDate(String date){
        String[] dates = date.split("-");

        String months[] = new String[]{"","January","February","March","April","May","June","July","August","September","October","November","December"};
        int dayInt = Integer.valueOf(dates[0]);
        int monthInt = Integer.valueOf(dates[1]);
        String year = dates[2];
        String month = months[monthInt];

        return month + " " + String.valueOf(dayInt) + ", " + year;

    }

    static String getFormattedJsonDate(String date){
        String[] dates = date.split("-");

        String months[] = new String[]{"","January","February","March","April","May","June","July","August","September","October","November","December"};
        String[] day = dates[2].split("T");
        int dayInt = Integer.valueOf(day[0]);
        int monthInt = Integer.valueOf(dates[1]);
        String year = dates[0];
        String month = months[monthInt];

        return month + " " + String.valueOf(dayInt) + ", " + year;
    }

    static String getFormattedSummary(String oldSummary){
       return oldSummary.substring(3,oldSummary.length()-6);
    }

    public static String getFormattedPlayTimeString(int time){
        time = time/1000;
        int minutes = time/60;
        int seconds = time%60;

        String strMinutes = minutes<10?"0"+String.valueOf(minutes):String.valueOf(minutes);
        String strSeconds = (seconds<10?"0":"")+String.valueOf(seconds);

        return strMinutes + ":" + strSeconds;
    }
}
