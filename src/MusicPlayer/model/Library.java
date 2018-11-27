/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicPlayer.model;

import java.util.ArrayList;

/**
 *
 * @author elezo
 */
public class Library {
    
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String ARTIST = "artist";
    private static final String ALBUM = "album";
    private static final String LENGTH = "length";
    private static final String TRACKNUMBER = "trackNumber";
    private static final String DISCNUMBER = "discNumber";
    private static final String PLAYCOUNT = "playCount";
    private static final String PLAYDATE = "playDate";
    private static final String LOCATION = "location";

    private static ArrayList<Song> songs;
    private static ArrayList<Artist> artists;
    private static ArrayList<Album> albums;
    private static ArrayList<Playlist> playlists;
    private static int maxProgress;
    
    
    public void addSongToLibrary(Song song){
        songs.add(song);
    }
    
    public void addArtistToLibrary(Artist artist){
        artists.add(artist);
    }
    
    public void addAlbumToLibrary(Album album){
        albums.add(album);
    }
    
    public void addPlaylistToLibrary(Playlist playlist){
        playlists.add(playlist);
    }
    
    public ArrayList<Song> getSongs(){
        return this.songs;
    }
    
    public ArrayList<Album> getAlbums(){
        return this.albums;
    }
    
    public ArrayList<Playlist> getPlaylists(){
        return this.playlists;
    }
    
    public ArrayList<Artist> getArtists(){
        return this.artists;
    }
}
