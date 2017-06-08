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
}
