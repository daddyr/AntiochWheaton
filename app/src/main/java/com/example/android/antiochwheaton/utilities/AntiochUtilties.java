package com.example.android.antiochwheaton.utilities;

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

    public static String getAuthor(String authorId){
        switch (authorId){
            case "11":
                return "Chris Otts";
            case "12":
                return "Gwen Meyers";
            case "13":
                return "Dean Roberts";
            default:
                return "Guest Speaker";
        }
    }
}
