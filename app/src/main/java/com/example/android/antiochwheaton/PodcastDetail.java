package com.example.android.antiochwheaton;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.antiochwheaton.data.DataContract;
import com.example.android.antiochwheaton.databinding.ActivityPodcastDetailBinding;
import com.example.android.antiochwheaton.utilities.AntiochUtilties;
import com.squareup.picasso.Picasso;


import java.net.URL;
//todo: use databases to get data for images and authors


public class PodcastDetail extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    String[] mProjection = new String[]{
            DataContract.PodcastEntry.COLUMN_WP_ID,
            DataContract.PodcastEntry.COLUMN_TITLE,
            DataContract.PodcastEntry.COLUMN_DATE,
            DataContract.PodcastEntry.COLUMN_AUTHOR,
            DataContract.PodcastEntry.COLUMN_IMAGE_URL,
            DataContract.PodcastEntry.COLUMN_PODCAST_URL,
            DataContract.PodcastEntry.COLUMN_SUMMARY
    };

    public static final int ID = 0;
    public static final int TITLE = 1;
    public static final int DATE = 2;
    public static final int AUTHOR = 3;
    public static final int IMAGE = 4;
    public static final int URL = 5;
    public static final int SUMMARY = 6;

    private static final int ID_DETAIL_LOADER = 353;


    Button mButtonListen;

    private Uri mUri;

    String mPodcastUrl;

    ActivityPodcastDetailBinding mDetailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDetailBinding = DataBindingUtil.setContentView(this,R.layout.activity_podcast_detail);
        mButtonListen = (Button)findViewById(R.id.btnDetailListen);

        Intent intent = getIntent();

        mUri = intent.getData();

        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case ID_DETAIL_LOADER:
                return new CursorLoader(this,
                        mUri,
                        mProjection,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader not implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        boolean cursorHasData = false;
        if(data != null && data.moveToFirst()){
            cursorHasData = true;
        }

        if(!cursorHasData)
            return;

        String title = data.getString(TITLE);
        String date = data.getString(DATE);
        String authorId = data.getString(AUTHOR);
        String summary = data.getString(SUMMARY);
        String imageId = data.getString(IMAGE);

        String author = AntiochUtilties.getAuthor(this,authorId);
        String imageUrl = AntiochUtilties.getImageUrl(this,imageId);
        mDetailBinding.tvDetailTitle.setText(title);
        mDetailBinding.tvDetailDate.setText(date);
        mDetailBinding.tvDetailAuthor.setText(author);
        mDetailBinding.tvDetailDescription.setText(summary);
        if(imageUrl == ""){
            mDetailBinding.ivDetailImage.setImageResource(R.mipmap.ic_launcher);
        }else {
            Picasso.with(this).load(imageUrl).placeholder(R.mipmap.ic_launcher).into(mDetailBinding.ivDetailImage);
        }

        mPodcastUrl = data.getString(URL);


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void onListenClick(View view){
        Uri podcast = Uri.parse(mPodcastUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW,podcast);
        startActivity(intent);
    }
}
