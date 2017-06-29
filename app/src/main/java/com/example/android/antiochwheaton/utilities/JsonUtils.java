/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.antiochwheaton.utilities;

import android.content.ContentValues;
import android.content.Context;

import com.example.android.antiochwheaton.data.DataContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Utility functions to handle OpenWeatherMap JSON data.
 */
public final class JsonUtils {

    /**
     * This method parses JSON from a web response and returns an array of Strings
     * describing the weather over various days from the forecast.
     * <p/>
     * Later on, we'll be parsing the JSON into structured data within the
     * getFullWeatherDataFromJson function, leveraging the data we have stored in the JSON. For
     * now, we just convert the JSON into human-readable strings.
     *
     * @param forecastJsonStr JSON response from server
     *
     * @return Array of Strings describing weather data
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static ContentValues[] getPodcastContenValuesNamesFromJson(Context context, String forecastJsonStr)
            throws JSONException {

        /* Weather information. Each day's forecast info is an element of the "list" array */


        final String JSON_TITLE = "title";
        final String JSON_RENDERED = "rendered";
        final String JSON_ID = "id";
        final String JSON_URL = "link";
        final String JSON_CONTENT = "content";
        final String JSON_META = "meta";
        final String JSON_DATE = "date_recorded";
        final String JSON_AUDIO_FILE = "audio_file";
        final String JSON_AUTHOR = "tags";
        final String JSON_MEDIA = "featured_media";

        /* String array to hold each day's weather String */


        JSONArray podcastJson = new JSONArray(forecastJsonStr);

        //JSONArray array = forecastJson.getJSONArray("");

        ContentValues[] podcastContentValues = new ContentValues[podcastJson.length()];


        for (int i = 0; i < podcastJson.length(); i++) {

            JSONObject podcast = podcastJson.getJSONObject(i);

            String podcastId = podcast.getString(JSON_ID);
            JSONObject title = podcast.getJSONObject(JSON_TITLE);
            String strTitle = title.getString(JSON_RENDERED);

            JSONObject meta = podcast.getJSONObject(JSON_META);
            String strDate = meta.getString(JSON_DATE);
            JSONArray tags = podcast.getJSONArray(JSON_AUTHOR);
            String strAuthor = "";
            if(tags != null)
                strAuthor = tags.getString(0);
            JSONObject content = podcast.getJSONObject(JSON_CONTENT);
            String strContent = podcast.getString(JSON_MEDIA);
            String podcastURL = AntiochUtilties.formattedURL(meta.getString(JSON_AUDIO_FILE));
            String sermonURL = AntiochUtilties.formattedURL(podcast.getString(JSON_URL));


            ContentValues podcastValues = new ContentValues();
            podcastValues.put(DataContract.PodcastEntry.COLUMN_WP_ID,podcastId);
            podcastValues.put(DataContract.PodcastEntry.COLUMN_TITLE,strTitle);
            podcastValues.put(DataContract.PodcastEntry.COLUMN_DATE,strDate);
            podcastValues.put(DataContract.PodcastEntry.COLUMN_AUTHOR,strAuthor);
            podcastValues.put(DataContract.PodcastEntry.COLUMN_IMAGE_URL,strContent);
            podcastValues.put(DataContract.PodcastEntry.COLUMN_PODCAST_URL,podcastURL);
            podcastValues.put(DataContract.PodcastEntry.COLUMN_SUMMARY,sermonURL);

            podcastContentValues[i] = podcastValues;
        }

        return podcastContentValues;
    }


    public static ContentValues[] getMediaContenValuesNamesFromJson(Context context, String JsonStr)
            throws JSONException {

        /* Weather information. Each day's forecast info is an element of the "list" array */

        final String JSON_ID = "id";
        final String JSON_LINK = "rendered";

        /* String array to hold each day's weather String */


        JSONArray mediaJson = new JSONArray(JsonStr);

        //JSONArray array = forecastJson.getJSONArray("");

        ContentValues[] mediaContentValues = new ContentValues[mediaJson.length()];


        for (int i = 0; i < mediaJson.length(); i++) {

            JSONObject media = mediaJson.getJSONObject(i);

            String mediaId = media.getString(JSON_ID);
            String mediaLink = media.getJSONObject("guid").getString(JSON_LINK);


            ContentValues mediaValues = new ContentValues();
            mediaValues.put(DataContract.MediaEntry.COLUMN_WP_ID,mediaId);
            mediaValues.put(DataContract.MediaEntry.COLUMN_URL,mediaLink);

            mediaContentValues[i] = mediaValues;
        }

        return mediaContentValues;
    }

    public static ContentValues[] getTagsContenValuesNamesFromJson(Context context, String JsonStr)
            throws JSONException {

        /* Weather information. Each day's forecast info is an element of the "list" array */

        final String JSON_ID = "id";
        final String JSON_VALUE = "name";

        /* String array to hold each day's weather String */


        JSONArray tagsJson = new JSONArray(JsonStr);

        //JSONArray array = forecastJson.getJSONArray("");

        ContentValues[] tagsContentValues = new ContentValues[tagsJson.length()];


        for (int i = 0; i < tagsJson.length(); i++) {

            JSONObject tags = tagsJson.getJSONObject(i);

            String tagsId = tags.getString(JSON_ID);
            String tagsLink = tags.getString(JSON_VALUE);


            ContentValues tagsValues = new ContentValues();
            tagsValues.put(DataContract.TagsEntry.COLUMN_WP_ID,tagsId);
            tagsValues.put(DataContract.TagsEntry.COLUMN_VALUE,tagsLink);

            tagsContentValues[i] = tagsValues;
        }

        return tagsContentValues;
    }
    /**
     * Parse the JSON and convert it into ContentValues that can be inserted into our database.
     *
     * @param context         An application context, such as a service or activity context.
     * @param forecastJsonStr The JSON to parse into ContentValues.
     *
     * @return An array of ContentValues parsed from the JSON.
     */
    public static ContentValues[] getFullWeatherDataFromJson(Context context, String forecastJsonStr) {
        /** This will be implemented in a future lesson **/
        return null;
    }
}