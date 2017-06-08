package com.example.android.antiochwheaton;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.antiochwheaton.data.DataContract;
import com.example.android.antiochwheaton.utilities.NetworkUtils;
import com.example.android.antiochwheaton.utilities.JsonUtils;

import java.net.URL;

public class Sermons extends AppCompatActivity implements
        PodcastAdapter.PodcastAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor>{

    String[] mProjection = new String[]{
            DataContract.PodcastEntry.COLUMN_TITLE,
            DataContract.PodcastEntry.COLUMN_DATE,
            DataContract.PodcastEntry.COLUMN_IMAGE_URL
    };

    final int TITLE = 0;
    final int DATE = 1;
    final int IMAGE = 2;

    private static final int LOADER = 0;

    private RecyclerView mRecyclerView;
    private PodcastAdapter mPodcastAdapter;
    private int mPosition = RecyclerView.NO_POSITION;

    Toast mToast;
    TextView mErrorTextView;
    ProgressBar mLoadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sermons);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview_podcasts);

        mLoadingProgressBar = (ProgressBar)findViewById(R.id.pb_loading);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mPodcastAdapter = new PodcastAdapter(this,this);

        mRecyclerView.setAdapter(mPodcastAdapter);

        showLoading();

        getSupportLoaderManager().initLoader(LOADER,null,this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.podcast,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemClicked = item.getItemId();
        if(itemClicked == R.id.action_refresh){
            getSupportLoaderManager().restartLoader(LOADER,null,this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showLoading(){
        mLoadingProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onListItemClick(String podcastName) {
        Intent intent = new Intent(this,PodcastDetail.class);
        intent.putExtra("podcast",podcastName);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, DataContract.PodcastEntry.CONTENT_URI,mProjection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mPodcastAdapter.swapCursor(data);

        if(mPosition == RecyclerView.NO_POSITION){
            mPosition = 0;
        }

        mRecyclerView.smoothScrollToPosition(mPosition);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mPodcastAdapter.swapCursor(null);
    }

}
