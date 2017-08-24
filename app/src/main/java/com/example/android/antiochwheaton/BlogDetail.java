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

import com.example.android.antiochwheaton.data.DataContract;
import com.example.android.antiochwheaton.databinding.ActivityBlogDetailBinding;
import com.example.android.antiochwheaton.utilities.AntiochUtilties;
import com.squareup.picasso.Picasso;

public class BlogDetail extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    String[] mProjection = new String[]{
            DataContract.BlogEntry.COLUMN_WP_ID,
            DataContract.BlogEntry.COLUMN_TITLE,
            DataContract.BlogEntry.COLUMN_CONTENT,
            DataContract.BlogEntry.COLUMN_IMAGE_URL
    };

    public static final int ID = 0;
    public static final int TITLE = 1;
    public static final int CONTENT = 2;
    public static final int IMAGE = 3;

    private static final int ID_DETAIL_LOADER = 354;

    ActivityBlogDetailBinding mDetailBinding;
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDetailBinding = DataBindingUtil.setContentView(this,R.layout.activity_blog_detail);

        Intent intent = getIntent();

        mUri = intent.getData();

        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER,null,this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case ID_DETAIL_LOADER:
                return new CursorLoader(this,mUri,mProjection,null,null,null);
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

        String imageUrl = AntiochUtilties.getImageUrl(this,imageId);


        content = android.text.Html.fromHtml(content).toString();
        mDetailBinding.tvBlogDetailTitle.setText(title);
        mDetailBinding.tvBlogDetailContent.setText(content);
        if(imageUrl == ""){
            mDetailBinding.ivBlogDetailImage.setImageResource(R.mipmap.ic_launcher);
        }else{
            Picasso.with(this).load(imageUrl).placeholder(R.mipmap.ic_launcher).into(mDetailBinding.ivBlogDetailImage);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
