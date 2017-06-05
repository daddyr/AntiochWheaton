package com.example.android.antiochwheaton;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
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

import com.example.android.antiochwheaton.utilities.NetworkUtils;
import com.example.android.antiochwheaton.utilities.OpenWeatherJsonUtils;

import java.io.IOException;
import java.net.URL;

public class Sermons extends AppCompatActivity implements PodcastAdapter.PodcastAdapterOnClickHandler, LoaderManager.LoaderCallbacks<String[]>{

    private static final int LOADER = 0;

    private RecyclerView mRecyclerView;
    private PodcastAdapter mPodcastAdapter;

    Toast mToast;
    TextView mErrorTextView;
    ProgressBar mLoadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sermons);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview_podcasts);

        mErrorTextView = (TextView)findViewById(R.id.tv_error_message);
        mLoadingProgressBar = (ProgressBar)findViewById(R.id.pb_loading);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mPodcastAdapter = new PodcastAdapter(this);

        mRecyclerView.setAdapter(mPodcastAdapter);

        LoaderManager.LoaderCallbacks<String[]> callback = Sermons.this;

        Bundle bundle = null;

        getSupportLoaderManager().initLoader(LOADER,bundle,callback);
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
            mPodcastAdapter.setmPodcastData(null);
            getSupportLoaderManager().restartLoader(LOADER,null,this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showPodcastDataView(){
        mErrorTextView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(){
        mErrorTextView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onListItemClick(String podcastName) {
        Intent intent = new Intent(this,PodcastDetail.class);
        intent.putExtra("podcast",podcastName);
        startActivity(intent);
    }

    @Override
    public Loader<String[]> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<String[]>(this) {
            String[] mPodcastData = null;

            @Override
            protected void onStartLoading() {
                if(mPodcastData != null){
                    deliverResult(mPodcastData);
                }else{
                    mLoadingProgressBar.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public String[] loadInBackground() {
                String search = "podcast";
                URL searchURL = NetworkUtils.buildUrl(search);
                try{
                    String searchResult = NetworkUtils.getResponseFromHttpUrl(searchURL);
                    String[] podcastData = OpenWeatherJsonUtils.getSimplePodcastNamesFromJson(Sermons.this,searchResult);

                    return podcastData;
                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(String[] data){
                mPodcastData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String[]> loader, String[] data) {
        mLoadingProgressBar.setVisibility(View.INVISIBLE);
        if(data != null){
            showPodcastDataView();
            mPodcastAdapter.setmPodcastData(data);
        }else{
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<String[]> loader) {

    }

}
