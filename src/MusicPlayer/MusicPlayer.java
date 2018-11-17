/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicPlayer;

import MusicPlayer.model.Song;
import MusicPlayer.view.MusicPlayerController;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 *
 * @author elezo
 */
public class MusicPlayer extends Application {
    
    private static MusicPlayerController mainController;
    public static MediaPlayer mediaPlayer;
    private static Media media;
    private static ArrayList<Song> nowPlayingList;
    private static int nowPlayingIndex;
    private static Song nowPlaying;
    private static Timer timer;
    private static int timerCounter;
    private static int secondsPlayed;
    private static boolean isLoopActive = false;
    private static boolean isShuffleActive = false;
    private static boolean isMuted = false;
    private static Object draggedItem;
    
    private static Stage stage;
    
    @Override
    public void start(Stage stage) throws Exception{
        
        MusicPlayer.stage = stage;
        MusicPlayer.stage.setTitle("Alien Music Player");
        
        try {
            // Load main layout from fxml file.
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/Musicplayer/View/MusicPlayer.FXML"));
            BorderPane view = mainLoader.load();
            
            //initialize controller and give access to main application
            mainController = mainLoader.getController();
//            
//            new Thread(){
//                @Override
//                public void run(){
//                    final URL lookatmefile = getClass().getResource("/MusicPlayer/util/Music/01 Look At Me!.m4a");
//                    media = new Media(lookatmefile.toString());
//                    mediaPlayer = new MediaPlayer(media);
//                    mediaPlayer.play();
//                }
//            }.start();
            
            

            // Shows the scene containing the layout.
            Scene scene = new Scene(view);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();

        
        } 
        catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
               
}
    
    public static boolean isPlaying() {
        return mediaPlayer != null && MediaPlayer.Status.PLAYING.equals(mediaPlayer.getStatus());
    }
    
    //Pauses current Song
    public static void pause(){
        if (isPlaying()){
            mediaPlayer.pause();           
        }
    }

    
    // Plays/Pauses Current Song
    public static void playPause(){
        if (isPlaying()){
            mediaPlayer.pause();
        }
        else {
            mediaPlayer.play();
        }
    }
    
    public static MusicPlayerController getMainController(){
        return mainController;
    }

    private static void checkLibraryXML(){
        /* function that checks if library exists
        case: check if library was renamed
        case: check if song was added from last start of application
        case: check if library !exist, create file otherwise.
        */
        
        File libraryXML = new File("MusicPlayer/util/resources/library.xml");
        if (!libraryXML.exists()){
            //createLibraryXML(); function to be created
        }
       
    }
    
public static void main(String[] args){
    Application.launch(MusicPlayer.class);
}
}
