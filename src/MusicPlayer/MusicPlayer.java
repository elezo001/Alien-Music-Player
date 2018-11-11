/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicPlayer;

import MusicPlayer.model.Song;
import MusicPlayer.view.MusicPlayerController;
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
    private static MediaPlayer mediaPlayer;
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
    
    @Override
    public void start(Stage primaryStage) throws Exception{
        
        try {
            // Load main layout from fxml file.
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/Musicplayer/View/MusicPlayer.FXML"));
            BorderPane view = mainLoader.load();
            

            
            new Thread(){
                @Override
                public void run(){
                    final URL lookatmefile = getClass().getResource("/MusicPlayer/util/Music/01 Look At Me!.m4a");
                    media = new Media(lookatmefile.toString());
                    mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.play();
                }
            }.start();
            
            

            // Shows the scene containing the layout.
            Scene scene = new Scene(view);
            primaryStage.setTitle("Alien Music Player");
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.show();

        
        } 
        catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
               
}
    
public static void main(String[] args){
    launch(args);
}
}
