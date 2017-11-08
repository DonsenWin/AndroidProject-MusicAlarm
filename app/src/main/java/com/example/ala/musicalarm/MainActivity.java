package com.example.ala.musicalarm;

import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import android.os.IBinder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import com.example.ala.musicalarm.MusicPlayerService.musicBinder;
import android.widget.MediaController.MediaPlayerControl;


public class MainActivity extends AppCompatActivity
    implements ChooseSongFragment.SongFragmentInterface,
               ChooseDateFragment.DateFragmentInterface,
               ChooseMainFragment.MainFragmentInterface,
        MediaPlayerControl {

    public static final String A = "A";
    public static final String Ra= "randomize";
    public static final  String V = "volume";
    public static final  String Vib = "vibrate";

    public ArrayList<Song> songListArray;
    private MusicPlayerService musicPlayerService;
    private Intent playIntent;
    private boolean musicBound = false;
    private  boolean paused = false,
    playbackPaused = false;
    private MusicMediaController mediaController;

/////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songListArray = new ArrayList<Song>();
        getSongsList();


        FragmentTransaction ft3 = getFragmentManager().beginTransaction();
        ft3.replace(R.id.fragment_placeholder, new ChooseMainFragment()).commit();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        setMediaController();
        mediaController.hide();




    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);


        boolean a = intent.getBooleanExtra(A, false);
        boolean ra = intent.getBooleanExtra(Ra,false);
        int vol=intent.getIntExtra(V,20);
        boolean vib = intent.getBooleanExtra(Vib,false);
          if (a) {
            AudioManager audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);

            int o=  audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            o= vol*o/100;

              audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,o,0);

              if(ra){musicPlayerService.setShuffle();}

            musicPlayerService.playNext();

              if(vib) {Vibrator vibrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);
                  long[] pattern = { 10, 200, 10 };

                  if(vibrator.hasVibrator())vibrator.vibrate(pattern,0);



              }



        }


    }

    public void getSongsList() {

        ContentResolver musicResolver = this.getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor mCursor = musicResolver.query(musicUri, null, null, null, null);

        if (mCursor != null && mCursor.moveToFirst()) {
            int titleColumn = mCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = mCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artistColumn = mCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);


            while (mCursor.moveToNext()) {
                long thisId = mCursor.getLong(idColumn);
                String thisTitle = mCursor.getString(titleColumn);
                String thisArtist = mCursor.getString(artistColumn);
                songListArray.add(new Song(thisId, thisTitle, thisArtist));
            }


            Collections.sort(songListArray, new Comparator<Song>() {
                @Override
                public int compare(Song x, Song y) {
                    return x.getTitle().compareTo(y.getTitle());
                }
            });
        }
        mCursor.close();
    }

//fragments comm
    @Override
    public ArrayList<Song> songsNeeded() {
        return  songListArray;
    }

    @Override
    public boolean isPlayingNow() {

        return isPlaying();
    }

    @Override
    public boolean isRandomized() {

        if(musicPlayerService!=null && musicBound)
        {return musicPlayerService.getShuffle();}
        else {return false;}
    }

    @Override
    protected void onStart() {
        super.onStart();


        if (playIntent == null) {
            playIntent = new Intent(this, MusicPlayerService.class);
            bindService(playIntent, musicPlayingConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    private ServiceConnection musicPlayingConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            if (!musicBound) {
                musicBinder binder = (musicBinder) service;
                musicPlayerService = binder.getService();
                musicPlayerService.setSongsList(songListArray);
                musicBound = true;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;

        }
    };

/////////////////////////////////////////////////////////

     //buttons
    public void chooseDateButton(View view)
    {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, new ChooseDateFragment()).commit();

    }

    public void chooseMainButton(View view)
    {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, new ChooseMainFragment()).commit();
    }

    public void chooseSongButton(View view)
    {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, new ChooseSongFragment()).commit();
    }

    public void playPauseButton(View view) {

        if (musicPlayerService.isPlaying())
        {
            ImageButton imageButton = (ImageButton) view.findViewById(R.id.playButton);
            imageButton.setImageResource(R.drawable.play_selector);
            pause();
        } else
        {
            ImageButton imageButton = (ImageButton) view.findViewById(R.id.playButton);
            imageButton.setImageResource(R.drawable.pause_selector);
            start();
        }
    }

    public void exitButton(View view){
        stopService(playIntent);

        NotificationManager alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Vibrator vibrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.cancel();
        alarmNotificationManager.cancel(9);
        alarmNotificationManager.cancel(1);

        musicPlayerService = null;
        System.exit(0);
    }

    public void snoozeButton(View view){
    if (musicPlayerService.isPlaying())
    {    pause();

    }

        NotificationManager alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Vibrator vibrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.cancel();
        alarmNotificationManager.cancel(9);


        if (!musicPlayerService.isPlaying())
        {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    playNextSong();
                }
            }, 1000*60*5);
        }
    }

    public void nextButton(View view){
        playNextSong();
    }

    public void playRandomButton(View view)
    {
        if (musicPlayerService.getShuffle())
        {

            ImageButton imageButton = (ImageButton) view.findViewById(R.id.shuffleButton);
            imageButton.setImageResource(R.drawable.normal_selector);

        } else
        {
            ImageButton imageButton = (ImageButton) view.findViewById(R.id.shuffleButton);
            imageButton.setImageResource(R.drawable.random_selector);
        }

    musicPlayerService.setShuffle();
    }

//song
    public void songPicked(View view) {
        musicPlayerService.setCurrentSong(Integer.parseInt(view.getTag().toString()));
        musicPlayerService.playASong();

        if(playbackPaused){
            setMediaController();
            playbackPaused=false;
        }
        mediaController.show(0);

    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicPlayerService = null;
        super.onDestroy();
    }

    //media control
    private void setMediaController (){
        mediaController = new MusicMediaController(this);

        mediaController.setPrevNextListeners(new View.OnClickListener() {
            @Override
             public void onClick(View v) {
              playNextSong(); }
         }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPreviousSong();
            }
        });

        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(findViewById(R.id.list_of_songs));
        mediaController.setEnabled(true);
    }

    private void playNextSong() {
        musicPlayerService.playNext();
        if(playbackPaused){
            setMediaController();
            playbackPaused=false;
        }
        mediaController.show(0);
    }

    private void playPreviousSong() {
        musicPlayerService.playPrevious();
        if(playbackPaused){
            setMediaController();
            playbackPaused=false;
        }
        mediaController.show(0);
    }

    //Mediaplayercontrol
    @Override
    protected void onPause(){
        super.onPause();
        paused=true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(paused){
            setMediaController();
            paused=false;
        }
    }

    @Override
    protected void onStop(){
        mediaController.hide();
        super.onStop();
    }

//-----

    @Override
    public void start() {
    musicPlayerService.goOnPlaying();
    }

    @Override
    public void pause() {
        playbackPaused=true;
        musicPlayerService.pausePlayback();
    }

    @Override
    public int getDuration() {
        if(musicPlayerService!=null && musicBound && musicPlayerService.isPlaying())
        {return musicPlayerService.getDuration();}
        else {return 0;}
    }

    @Override
    public int getCurrentPosition() {
        if(musicPlayerService!=null && musicBound && musicPlayerService.isPlaying())
        {return musicPlayerService.getPlayingPosition();}
        else {return 0;}
    }

    @Override
    public void seekTo(int pos) {
        musicPlayerService.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        if(musicPlayerService!=null && musicBound)
        {return musicPlayerService.isPlaying();}
        else {return false;}
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }







}