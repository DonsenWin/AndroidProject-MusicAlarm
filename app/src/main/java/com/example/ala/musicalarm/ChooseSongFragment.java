package com.example.ala.musicalarm;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import java.util.ArrayList;


public class ChooseSongFragment extends Fragment {


    SongFragmentInterface mCallback;
    public ArrayList<Song> songListArrayF;
    public ListView songsListViewF ;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);

    }

    //activity communication
    public interface SongFragmentInterface{
        ArrayList<Song> songsNeeded();
    }

  //init

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (SongFragmentInterface) activity;
        }catch (ClassCastException ex){
            Log.e("SongFragment", "must implement SongFragmentInterface", ex );
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment

        final FrameLayout frameLayout =
                (FrameLayout) inflater.inflate(R.layout.fragment_choose_song, parent, false);
        songsListViewF = (ListView) frameLayout.findViewById(R.id.list_of_songs);
        songListArrayF = new ArrayList<Song>();
        songListArrayF= mCallback.songsNeeded();


        try {
            SongAdapter songAdapter = new SongAdapter(getActivity(), songListArrayF);
            songsListViewF.setAdapter(songAdapter);
        } catch (Exception e) {
            Log.e("CSF", "Error on SongAdapter", e);
        }

        return frameLayout;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


    }


}


