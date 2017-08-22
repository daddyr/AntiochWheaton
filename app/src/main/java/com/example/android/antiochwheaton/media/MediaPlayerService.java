package com.example.android.antiochwheaton.media;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaSessionManager;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.android.antiochwheaton.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;

/**
 * Created by ryan_ on 7/25/2017.
 */

public class MediaPlayerService extends Service implements MediaPlayer.OnCompletionListener,
    MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
    MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener, AudioManager.OnAudioFocusChangeListener{

    private final IBinder iBinder = new LocalBinder();

    private MediaPlayer mediaPlayer;
    private String mediaFile;
    private int resumePosition;
    WifiManager.WifiLock wifiLock;

    private AudioManager audioManager;

    private boolean ongoingCall = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;

    public static final String ACTION_PLAY = "com.valdioveliu.valdio.audioplayer.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.valdioveliu.valdio.audioplayer.ACTION_PAUSE";
    public static final String ACTION_PREVIOUS = "com.valdioveliu.valdio.audioplayer.ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "com.valdioveliu.valdio.audioplayer.ACTION_NEXT";
    public static final String ACTION_STOP = "com.valdioveliu.valdio.audioplayer.ACTION_STOP";

    private MediaSessionManager mediaSessionManager;
    private MediaSessionCompat mediaSession;
    private MediaControllerCompat.TransportControls transportControls;

    private static final int NOTIFICATION_ID = 101;

    String imageUrl;

    public enum PlaybackStatus{
        PLAYING,
        PAUSED
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        callStateListener();
        registerBecomingNoisyReceiver();
    }

    private void initMediaPlayer(){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnInfoListener(this);
        mediaPlayer.reset();
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);


        wifiLock = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE)).createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
        wifiLock.acquire();

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try{
            mediaPlayer.setDataSource(mediaFile);
        }catch (IOException e){
            e.printStackTrace();
            stopSelf();
        }
        mediaPlayer.prepareAsync();

    }


    private void initMediaSession(){
        if(mediaSessionManager != null) return;

        mediaSessionManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
        mediaSession = new MediaSessionCompat(getApplicationContext(),"AudioPlayer");
        transportControls = mediaSession.getController().getTransportControls();
        mediaSession.setActive(true);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        updateMetaData();

        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                super.onPlay();
                resumeMedia();
                buildNotification(PlaybackStatus.PLAYING);
            }

            @Override
            public void onPause() {
                super.onPause();
                pauseMedia();
                buildNotification(PlaybackStatus.PAUSED);
            }

            @Override
            public void onStop() {
                super.onStop();
                removeNotification();
                stopSelf();
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
                skipToNext();
                buildNotification(PlaybackStatus.PLAYING);
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
                skipToPrevious();
                buildNotification(PlaybackStatus.PLAYING);
            }

            @Override
            public void onSeekTo(long pos) {
                super.onSeekTo(pos);
            }
        });
    }

    private void updateMetaData(){
        //Bitmap albumArt = BitmapFactory.decodeResource(getResources(),R.drawable.image);

        //mediaSession.setMetadata(new MediaMetadataCompat.Builder().putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt).putString(MediaMetadataCompat.METADATA_KEY_ARTIST, activeAudio.getArtist()));

    }

    public void playMedia(){
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }
    }

    public boolean getStatus(){
        return mediaPlayer.isPlaying();
    }


    private void stopMedia(){
        if(mediaPlayer == null) return;
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
    }

    public void pauseMedia(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            resumePosition = mediaPlayer.getCurrentPosition();
            buildNotification(PlaybackStatus.PAUSED);
        }
    }

    public void skipToNext(){
        pauseMedia();
        resumePosition += 5000;
        if(resumePosition >= mediaPlayer.getDuration())
            stopMedia();
        else
            resumeMedia();
    }

    public void skipToPrevious(){
        resumePosition -= 3000;
        if(resumePosition < 0)
            resumePosition = 0;
        mediaPlayer.seekTo(resumePosition);
    }

    public void resumeMedia(){
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.seekTo(resumePosition);
            mediaPlayer.start();
            buildNotification(PlaybackStatus.PLAYING);
        }
    }
    @Override
    public void onAudioFocusChange(int focusState) {
        switch (focusState){
            case AudioManager.AUDIOFOCUS_GAIN:
                if(mediaPlayer == null)initMediaPlayer();
                else if(!mediaPlayer.isPlaying()) mediaPlayer.start();
                mediaPlayer.setVolume(1.0f,1.0f);
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                if(mediaPlayer.isPlaying()) mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                if(mediaPlayer.isPlaying())mediaPlayer.pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                if (mediaPlayer.isPlaying()) mediaPlayer.setVolume(0.1f,0.1f);
                break;
        }
    }

    private boolean requestAudioFocus(){
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
            return true;
        }
        return false;
    }

    private boolean removeAudioFocus(){
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == audioManager.abandonAudioFocus(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            mediaFile = intent.getExtras().getString("media");
            imageUrl = intent.getExtras().getString("image");
        }catch (NullPointerException e){
            stopSelf();
        }

        if (requestAudioFocus() == false){
            stopSelf();
        }

        if (mediaSessionManager == null) {
            try {
                initMediaSession();
                initMediaPlayer();
            } catch (Exception e) {
                e.printStackTrace();
                stopSelf();
            }
            buildNotification(PlaybackStatus.PLAYING);
        }

        //Handle Intent action from MediaSession.TransportControls
        handleIncomingActions(intent);

        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null){
            stopMedia();
            mediaPlayer.release();
        }
        if(wifiLock!= null){
            wifiLock.release();
        }
        removeAudioFocus();

        if(phoneStateListener != null){
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        }

        removeNotification();

        unregisterReceiver(becomingNoisyReciever);


    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        stopMedia();
        stopSelf();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        switch (what){
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Log.d("Media Player Error","Media not valid for progressive playback");
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.d("MediaPlayer Error","Media Error Server Died " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.d("MediaPlayer Error","Media Error Unknown " + extra);
                break;
        }
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        playMedia();
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {

    }

    public class LocalBinder extends Binder {
        public MediaPlayerService getService(){
            return MediaPlayerService.this;
        }
    }

    private BroadcastReceiver becomingNoisyReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            pauseMedia();
            buildNotification(PlaybackStatus.PAUSED);
        }
    };

    private void registerBecomingNoisyReceiver(){
        IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(becomingNoisyReciever,intentFilter);
    }

    private void callStateListener(){
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        phoneStateListener = new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state){
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                    case TelephonyManager.CALL_STATE_RINGING:
                        if(mediaPlayer != null){
                            pauseMedia();
                            ongoingCall = true;
                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        if(mediaPlayer != null){
                            if(ongoingCall){
                                ongoingCall = false;
                                resumeMedia();
                            }
                        }
                        break;

                }
            }
        };

        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private Target loadTarget;
    private Bitmap image;



    private void buildNotification(PlaybackStatus playbackStatus){
        int notificationAction = android.R.drawable.ic_media_pause;
        PendingIntent play_pauseAction = null;

        if(playbackStatus == PlaybackStatus.PLAYING){
            notificationAction = android.R.drawable.ic_media_pause;
            play_pauseAction = playbackAction(1);
        }else if (playbackStatus == PlaybackStatus.PAUSED){
            notificationAction = android.R.drawable.ic_media_play;
            play_pauseAction = playbackAction(0);
        }

        if(imageUrl == ""){
            image = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
        }else{
            Picasso.with(this).load(imageUrl).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    image = bitmap;
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }

        final Bitmap largeIcon = image;

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setShowWhen(false)
                .setStyle(new NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.getSessionToken()))
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setLargeIcon(largeIcon)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Colossians 4")
                .addAction(android.R.drawable.ic_media_rew, "prevoius",playbackAction(2))
                .addAction(notificationAction, "pause",play_pauseAction)
                .addAction(android.R.drawable.ic_media_ff, "next",playbackAction(3));

        ((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID,
                notificationBuilder.build());
    }

    private void removeNotification(){
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    private PendingIntent playbackAction(int actionNumber){
        Intent playBackAction = new Intent(this, MediaPlayerService.class);
        switch (actionNumber){
            case 0:
                playBackAction.setAction(ACTION_PLAY);
                return PendingIntent.getService(this,actionNumber,playBackAction,0);
            case 1:
                playBackAction.setAction(ACTION_PAUSE);
                return PendingIntent.getService(this,actionNumber,playBackAction,0);
            case 2:
                playBackAction.setAction(ACTION_PREVIOUS);
                return PendingIntent.getService(this,actionNumber,playBackAction,0);
            case 3:
                playBackAction.setAction(ACTION_NEXT);
                return PendingIntent.getService(this,actionNumber,playBackAction,0);
            default:
                break;
        }

        return null;
    }

    private void handleIncomingActions(Intent playbackAction){
        if(playbackAction == null || playbackAction.getAction() == null) return;

        String actionString = playbackAction.getAction();
        if(actionString.equalsIgnoreCase(ACTION_PLAY)){
            transportControls.play();
        }else if (actionString.equalsIgnoreCase(ACTION_PAUSE)){
            transportControls.pause();
        }else if (actionString.equalsIgnoreCase(ACTION_STOP)){
            transportControls.stop();
        }else if (actionString.equalsIgnoreCase(ACTION_PREVIOUS)){
            transportControls.skipToPrevious();
        }else if (actionString.equalsIgnoreCase(ACTION_NEXT)){
            transportControls.skipToNext();
        }
    }
}
