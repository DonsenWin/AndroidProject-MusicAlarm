package com.example.ala.musicalarm;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;


public class SongAdapter extends BaseAdapter{

    private ArrayList<Song> songsArray;
    private LayoutInflater songInflater;


SongAdapter(Context C, ArrayList<Song> Songs){
    songsArray=Songs;
    songInflater=LayoutInflater.from(C);
}

    @Override
    public int getCount() {
        return songsArray.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout songLayout = (LinearLayout) songInflater.inflate(R.layout.song_item_layout,parent,false);

        TextView titleView = (TextView) songLayout.findViewById(R.id.song_title);
        TextView artistView = (TextView) songLayout.findViewById(R.id.song_artist);

        Song nowPlaying = songsArray.get(position);

        titleView.setText(nowPlaying.getTitle());
        artistView.setText(nowPlaying.getArtist());

        songLayout.setTag(position);

        return songLayout;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}