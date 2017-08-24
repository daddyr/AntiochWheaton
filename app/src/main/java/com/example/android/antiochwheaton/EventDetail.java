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

import com.example.android.antiochwheaton.databinding.ActivityEventDetailBinding;
import com.example.android.antiochwheaton.utilities.AntiochUtilties;
import com.squareup.picasso.Picasso;

public class EventDetail extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{


    public static final int ID = 1;
    public static final int TITLE = 2;
    public static final int START_DATE = 3;
    public static final int END_DATE = 4;
    public static final int IMAGE = 5;
    public static final int CONTENT = 6;
    public static final int ADDRESS = 7;
    public static final int CITY  = 8;
    public static final int ZIP = 9;

    private static final int ID_DETAIL_LOADER = 355;

    ActivityEventDetailBinding mDetailBinding;

    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDetailBinding = DataBindingUtil.setContentView(this,R.layout.activity_event_detail);

        Intent intent = getIntent();

        mUri = intent.getData();

        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER,null,this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case ID_DETAIL_LOADER:
                return new CursorLoader(this,mUri,null,null,null,null);
            default:
                throw new RuntimeException("Loader not implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        boolean cursorHasData = false;
        if(data!=null && data.moveToFirst()){
            cursorHasData = true;
        }

        if(!cursorHasData)
            return;

        String title = data.getString(TITLE);
        String imageId = data.getString(IMAGE);
        String content = data.getString(CONTENT);
        String startDate = data.getString(START_DATE);
        String endDate = data.getString(END_DATE);
        String address = data.getString(ADDRESS);
        String city = data.getString(CITY);
        String zip = data.getString(ZIP);

        String imageUrl = AntiochUtilties.getImageUrl(this,imageId);


        content = android.text.Html.fromHtml(content).toString();
        mDetailBinding.tvEventDetailTitle.setText(title);
        mDetailBinding.tvEventDetailContent.setText(content);
        mDetailBinding.tvEventDetailDate.setText(AntiochUtilties.getFormattedEventDate(startDate,endDate));
        mDetailBinding.tvEventDetailLocation.setText(address + " " + city + ", IL" + zip);
        if(imageUrl.equals("")){
            mDetailBinding.ivEventDetailImage.setImageResource(R.mipmap.ic_launcher);
        }else
            Picasso.with(this).load(imageUrl).placeholder(R.mipmap.ic_launcher).into(mDetailBinding.ivEventDetailImage);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
