/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicPlayer.util;

import MusicPlayer.model.Song;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

/**
 *
 * @author elezo
 */
public class XMLEditor {
    
    private static ArrayList<Song> songsToAdd = new ArrayList<>();
    private static ArrayList<File> songFilesToAdd = new ArrayList(); 
    private static ArrayList<String> xmlSongFileNames = new ArrayList<>();
    
    
    private static void addSongToXml(){
        
        createSongObject();
        
    }
    
    private static void createSongObject() {
        
        for(File songFile : songFilesToAdd){
            try{
                AudioFile audioFile = AudioFileIO.read(songFile);
                Tag tag = audioFile.getTag();
                AudioHeader header = audioFile.getAudioHeader();
                
            }
                
            catch (Exception ex){
                ex.printStackTrace();
            }
        
        
        }
    }
}
