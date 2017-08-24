package com.example.android.antiochwheaton;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.android.antiochwheaton.data.DataContract;
import com.example.android.antiochwheaton.databinding.ActivityPodcastDetailBinding;
import com.example.android.antiochwheaton.media.MediaPlayerService;
import com.example.android.antiochwheaton.utilities.AntiochUtilties;
import com.squareup.picasso.Picasso;

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

    public static final int TITLE = 1;
    public static final int DATE = 2;
    public static final int AUTHOR = 3;
    public static final int IMAGE = 4;
    public static final int URL = 5;
    public static final int SUMMARY = 6;

    private static final int ID_DETAIL_LOADER = 353;

    private boolean firstClick = true;

    ImageButton mPlayButton;
    ImageButton mSkipForwardButton;
    ImageButton mSkipBackwardButton;
    SeekBar mSeekBar;
    TextView mCurrentTime;
    TextView mDuration;
    private Handler myHandler = new Handler();


    String imageUrl;
    String title;
    int currentPlayTime;
    int duration;

    private Uri mUri;

    String mPodcastUrl;

    ActivityPodcastDetailBinding mDetailBinding;

    private MediaPlayerService player;
    boolean serviceBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDetailBinding = DataBindingUtil.setContentView(this,R.layout.activity_podcast_detail);
        mPlayButton = (ImageButton)findViewById(R.id.play_button);
        mSkipBackwardButton = (ImageButton)findViewById(R.id.skip_back_button);
        mSkipForwardButton = (ImageButton)findViewById(R.id.skip_forward_button);
        mSeekBar = (SeekBar)findViewById(R.id.seek_bar_podcast);
        mCurrentTime = (TextView) findViewById(R.id.textCurrentPlayTime);
        mDuration = (TextView) findViewById(R.id.textDurationPlayTime);
        mDuration.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();
        currentPlayTime = 0;


        mUri = intent.getData();

        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(serviceBound && fromUser){
                    currentPlayTime = progress;
                    player.moveToPosition(currentPlayTime);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putBoolean("ServiceState",serviceBound);
        outState.putInt("CurrentTime",currentPlayTime);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        serviceBound = savedInstanceState.getBoolean("ServiceState");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(serviceBound){
            unbindService(serviceConnection);
            player.removeNotification();
            player.stopSelf();
            myHandler.removeCallbacks(UpdateMediaTime);
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;
            mCurrentTime.setText(R.string.time_zero);
            mDuration.setText(R.string.time_zero);
            duration = player.getDuration();
            //String time = AntiochUtilties.getFormattedPlayTimeString(duration);
            //mDuration.setText(time);
            mDuration.setVisibility(View.VISIBLE);
            //Toast.makeText(getApplicationContext(),String.valueOf(duration),Toast.LENGTH_LONG).show();
            mSeekBar.setMax(duration);
            myHandler.postDelayed(UpdateMediaTime,100);
        }


        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };


    private void playAudio(String media){
        if(!serviceBound){
            Intent playerIntent = new Intent(this, MediaPlayerService.class);
            playerIntent.putExtra("media",media);
            playerIntent.putExtra("title",title);
            startService(playerIntent);
            bindService(playerIntent,serviceConnection, Context.BIND_AUTO_CREATE);
        }
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

        title = data.getString(TITLE);
        String date = data.getString(DATE);
        String authorId = data.getString(AUTHOR);
        String summary = data.getString(SUMMARY);
        String imageId = data.getString(IMAGE);

        String author = AntiochUtilties.getAuthor(this,authorId);
        imageUrl = AntiochUtilties.getImageUrl(this,imageId);
        mDetailBinding.tvDetailTitle.setText(title);
        mDetailBinding.tvDetailDate.setText(date);
        mDetailBinding.tvDetailAuthor.setText(author);
        mDetailBinding.tvDetailDescription.setText(summary);
        if(imageUrl.equals("")){
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
        if(firstClick){
            playAudio(mPodcastUrl);
            firstClick = false;
            mPlayButton.setImageResource(android.R.drawable.ic_media_pause);
        }else {
            if (player.getStatus()) {
                player.pauseMedia();
                mPlayButton.setImageResource(android.R.drawable.ic_media_play);
            }else{
                player.resumeMedia();
                mPlayButton.setImageResource(android.R.drawable.ic_media_pause);
            }
        }
    }

    public void onSkipForward(View view){
        if(player.getStatus())
            player.skipToNext();
    }

    public void onSkipBackward(View view){
        if(player.getStatus())
            player.skipToPrevious();
    }

    private Runnable UpdateMediaTime = new Runnable() {
        @Override
        public void run() {
            if(serviceBound && player.getStatus()) {
                currentPlayTime = player.getCurrentPosition();
                mCurrentTime.setText(AntiochUtilties.getFormattedPlayTimeString(currentPlayTime));
                if(duration != player.getDuration()) {
                    duration = player.getDuration();
                    mDuration.setText(AntiochUtilties.getFormattedPlayTimeString(player.getDuration()));
                    mSeekBar.setMax(player.getDuration());
                }
                mSeekBar.setProgress(currentPlayTime);
            }
            myHandler.postDelayed(UpdateMediaTime, 100);
        }
    };
}
