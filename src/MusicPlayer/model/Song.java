/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicPlayer.model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.util.Duration;

/**
 *
 * @author elezo
 */
public final class Song implements Comparable<Song> {
    
    private SimpleStringProperty title;
    private SimpleStringProperty artist;
    private SimpleStringProperty album;
    private SimpleStringProperty length;
    
    private int playCount;
    
    private SimpleBooleanProperty playing;
    private String location;
    
    private int id;
    
    // Constructor for Song Class
    
    public Song(String title, String artist, String album, String length, String location, int id, int playCount){
        
        
        if (title == null) {
            Path path = Paths.get(location);
            String fileName = path.getFileName().toString();
            title = fileName.substring(0, fileName.lastIndexOf('.'));
        }

        
        this.title = new SimpleStringProperty(title);
        this.artist = new SimpleStringProperty(artist);
        this.id = id;
        this.album = new SimpleStringProperty(album);
        this.length = new SimpleStringProperty(length);
        this.location = location;
        
        this.playCount = playCount;
        
        
        
    }
    
    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title.get();
    }

    public StringProperty titleProperty() {
        return this.title;
    }

    public String getArtist() {
        return this.artist.get();
    }

    public StringProperty artistProperty() {
        return this.artist;
    }

    public String getAlbum() {
        return this.album.get();
    }


    public StringProperty albumProperty() {
        return this.album;
    }

    public String getLength() {
        return this.length.get();
    }

    public StringProperty lengthProperty() {
        return this.length;
    }

    public int getPlayCount() {
        return this.playCount;
    }

    public String getLocation() {
        return this.location;
    }

    public BooleanProperty playingProperty() {
        return this.playing;
    }

    public boolean getPlaying() {
        return this.playing.get();
    }

    public void setPlaying(boolean playing) {
        this.playing.set(playing);
    }


    
    

    @Override
    public int compareTo(Song o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
