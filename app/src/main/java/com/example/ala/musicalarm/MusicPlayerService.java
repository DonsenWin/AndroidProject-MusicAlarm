package com.example.ala.musicalarm;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.Random;
import android.media.AudioManager;
import android.os.Binder;
import android.os.PowerManager;
import android.util.Log;
import android.app.Notification;
import android.app.PendingIntent;


public class MusicPlayerService
    extends Service
    implements MediaPlayer.OnPreparedListener,
    MediaPlayer.OnErrorListener,
    MediaPlayer.OnCompletionListener{

    private MediaPlayer mediaPlayer;
    private ArrayList<Song> songsArray;
    private int playingSongPosition;
    private String songTitle="";
    private static final int NOTIFY_ID=1;

    private boolean shuffle=false;
    private Random rand;

    //setting up
    @Override
    public void onCreate()
    {   super.onCreate();
        rand=new Random();
        playingSongPosition =-1;
        mediaPlayer = new MediaPlayer();
        initializeMP();
    }

    public void initializeMP(){
        mediaPlayer.setWakeMode(getApplicationContext(),PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
    }

    public void setSongsList (ArrayList<Song> s){
    songsArray =s;   }

    //playback part
    public void playASong(){
        mediaPlayer.reset();
        Song playSong = songsArray.get(playingSongPosition);
        songTitle = playSong.getTitle();

        long currentlyPlaying = playSong.getId();
        Uri trackUri = ContentUris.withAppendedId( MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currentlyPlaying);

        try{
            mediaPlayer.setDataSource(getApplicationContext(),trackUri);
        } catch (Exception e){
            Log.e("MUSIC PLAYER SERVICE", "Error while setting data source", e);}


        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.prepareAsync();
    }

    @Override
    public void onPrepared(MediaPlayer mp)
    {   mp.start();

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.play_hover)
                .setTicker(songTitle)
                .setOngoing(true)
                .setContentTitle(getString(R.string.NowPlaying))
                .setContentText(songTitle);

        Notification notification;
        notification = builder.build();

        startForeground(NOTIFY_ID,notification);

    }

    public void setCurrentSong(int currentSongIndex){
    playingSongPosition =currentSongIndex;
    }

    //media control part
    public int getPlayingPosition(){
        return mediaPlayer.getCurrentPosition();
    }

    public int getDuration(){
        return mediaPlayer.getDuration();
    }

    public boolean isPlaying(){
        return  mediaPlayer.isPlaying();
    }

    public void pausePlayback(){
        mediaPlayer.pause();
    }

    public void seekTo (int playingPosition){
        mediaPlayer.seekTo(playingPosition);
    }

    public void goOnPlaying(){
        mediaPlayer.start();
    }

    public void playPrevious(){
        playingSongPosition--;
        if (playingSongPosition<0){
            playingSongPosition=songsArray.size()-1;}
        playASong();
    }

    public void playNext() {
      if(songsArray.size()>0) {
          if (shuffle) {
              int randomSong = playingSongPosition;
              while (randomSong == playingSongPosition) {
                  randomSong = rand.nextInt(songsArray.size());
              }
              playingSongPosition = randomSong;

          } else {
              playingSongPosition++;
              if (playingSongPosition >= songsArray.size()) {
                  playingSongPosition = 0;
              }
          }
          playASong();
      }
    }

    public void setShuffle(){
        shuffle = !shuffle;
    }

    public boolean getShuffle(){
        return shuffle;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
    if(mediaPlayer.getCurrentPosition()>0){
    mp.reset();
    playNext();
    }
    }

    //binder part
    public class musicBinder extends Binder {
    MusicPlayerService getService(){return MusicPlayerService.this;}
}

    private final IBinder musicBind = new musicBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {   return musicBind;    }

    @Override
    public boolean onUnbind(Intent intent) {
        mediaPlayer.stop();
        mediaPlayer.release();
        return false;
    }

    //destroying/error part
    @Override
    public void onDestroy() {
        stopForeground(true);


            mediaPlayer.release();
        super.onDestroy();

    }
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

}