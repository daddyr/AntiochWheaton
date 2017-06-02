package com.example.android.antiochwheaton;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.antiochwheaton.utilities.NetworkUtils;
import com.example.android.antiochwheaton.utilities.OpenWeatherJsonUtils;

import java.io.IOException;
import java.net.URL;

public class Sermons extends AppCompatActivity {

    TextView mTextDisplayPodcasts;
    TextView mErrorTextView;
    ProgressBar mLoadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sermons);

        mTextDisplayPodcasts = (TextView)findViewById(R.id.tv_podcast_data);
        mErrorTextView = (TextView)findViewById(R.id.tv_error_message);
        mLoadingProgressBar = (ProgressBar)findViewById(R.id.pb_loading);

        mTextDisplayPodcasts.setText("");

        loadData();
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
            loadData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showPodcastDataView(){
        mErrorTextView.setVisibility(View.INVISIBLE);
        mTextDisplayPodcasts.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(){
        mErrorTextView.setVisibility(View.VISIBLE);
        mTextDisplayPodcasts.setVisibility(View.INVISIBLE);
    }

    private void loadData(){
        showPodcastDataView();
        String type = "podcast";
        new GetPodcastData().execute(NetworkUtils.buildUrl(type));
    }

    private class GetPodcastData extends AsyncTask<URL,Void,String[]>{

        @Override
        protected void onPreExecute() {
            mLoadingProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(URL... params) {
            URL searchURL = params[0];
            String searchResult = null;
            try{
                searchResult = NetworkUtils.getResponseFromHttpUrl(searchURL);
                String[] podcastData = OpenWeatherJsonUtils.getSimplePodcastNamesFromJson(Sermons.this,searchResult);

                return podcastData;
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] podcastData) {
            mLoadingProgressBar.setVisibility(View.INVISIBLE);
            if (podcastData != null){
                mTextDisplayPodcasts.setVisibility(View.VISIBLE);
                for (String podcast : podcastData)
                    mTextDisplayPodcasts.append(podcast + "\n\n\n");
            }else{
                showErrorMessage();
            }
        }
    }
}
