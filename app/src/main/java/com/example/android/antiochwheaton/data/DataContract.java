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
    public static final String PATH_POSTS = "posts";
    public static final String PATH_EVENTS = "events";
    public static final String PATH_MEDIA = "media";
    public static final String PATH_TAGS = "tags";

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

        public static Uri buildPodcastUriWithId(String id){
            return CONTENT_URI.buildUpon()
                    .appendPath(id)
                    .build();
        }
    }

    public static final class BlogEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_POSTS).build();

        public static final String TABLE_NAME = "posts";

        public static final String COLUMN_WP_ID = "wp_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_IMAGE_URL = "image";

        public static Uri buildUriWithId(String id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(id)
                    .build();
        }
    }

    public static final class EventsEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_EVENTS).build();

        public static final String TABLE_NAME = "events";

        public static final String COLUMN_WP_ID = "wp_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_START_DATE = "start_date";
        public static final String COLUMN_END_DATE = "end_date";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_CITY = "city";
        public static final String COLUMN_ZIP = "zip";

        public static Uri buildUriWithId(String id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(id)
                    .build();
        }
    }

    public static final class MediaEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEDIA).build();

        public static final String TABLE_NAME = "media";

        public static final String COLUMN_WP_ID = "wp_id";
        public static final String COLUMN_URL = "url";

        public static Uri buildMediaUriWithId(String id){
            return CONTENT_URI.buildUpon()
                    .appendPath(id)
                    .build();
        }
    }

    public static final class TagsEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TAGS).build();

        public static final String TABLE_NAME = "tags";

        public static final String COLUMN_WP_ID = "wp_id";
        public static final String COLUMN_VALUE = "value";

        public static Uri buildTagsUriWithId(String id){
            return CONTENT_URI.buildUpon()
                    .appendPath(id)
                    .build();
        }
    }



}
