package com.example.ala.musicalarm;


public class Song {

    private long id;
    private String title;
    private String artist;


public Song(long SongId, String SongTitle, String SongArtist){
    id=SongId;
    title=SongTitle;
    artist=SongArtist;
}

    public long getId(){return id;}
    public String getTitle(){return title;}
    public String getArtist(){return artist;}


}
