package com.example.android.antiochwheaton.data;

import android.provider.BaseColumns;

/**
 * Created by Ryan on 6/5/2017.
 */

public class DataContract {
    public static final class PodcastEntry implements BaseColumns{
        public static final String TABLE_NAME = "podcasts";

        public static final String COLUMN_WP_ID = "wp_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_IMAGE_URL = "image";
        public static final String COLUMN_PODCAST_URL = "podcast";
        public static final String COLUMN_SUMMARY = "summary";
    }


}
