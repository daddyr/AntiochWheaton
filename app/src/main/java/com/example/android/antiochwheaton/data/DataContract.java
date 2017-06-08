package com.example.android.antiochwheaton.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Ryan on 6/5/2017.
 */

public class DataContract {

    public static final String CONTENT_AUTORITY = "com.example.android.antiochwheaton";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTORITY);

    public static final String PATH_PODCASTS = "podcasts";

    public static final class PodcastEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_PODCASTS).build();

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
