/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicPlayer;

import MusicPlayer.model.Song;
import MusicPlayer.view.MusicPlayerController;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author elezo
 */
public class MusicPlayer extends Application {
    
    private static MusicPlayerController mainController;
    public static MediaPlayer mediaPlayer;
    private static Media media;
    private static ArrayList<Song> currentPlayingList;
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
        //checks if library XML database exists, if not it creates one.
        checkLibraryXML();
                        
        try {
            // Load main layout from fxml file.
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/Musicplayer/View/MusicPlayer.FXML"));
            BorderPane view = mainLoader.load();
            
            //initialize controller and give access to main application
            mainController = mainLoader.getController();
            

            // Shows the scene containing the layout.
            Scene scene = new Scene(view);
            stage.setScene(scene);
            //stage.setMaximized(true);
            stage.show();
            

        
        } 
        catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
        
               
}
    public static Song getPlayingSong(){
        return nowPlaying;
    }
    
    public static void setNowPlaying(Song song){
        nowPlaying = song;
        nowPlayingIndex = song.getId();
    }
    
    public static void setCurrentPlayingList(List<Song> list){
        currentPlayingList = new ArrayList<>(list);
    }
    
    public static void printCurrentPlayingList(){
        for(int i=0; i<currentPlayingList.size(); i++){
            System.out.print(currentPlayingList.get(i).getTitle() + System.lineSeparator());
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
    
    public static Stage getStage(){
        return stage;
    }
    
    //Plays next song in list
    public static void next(){
        
        mediaPlayer.stop();
        
        if(nowPlayingIndex < currentPlayingList.size() - 1){
            Song songToPlay = currentPlayingList.get(nowPlayingIndex + 1);
            String songFile = new File(songToPlay.getLocation()).toURI().toString();
            
            Media media = new Media(songFile);
            MusicPlayer.mediaPlayer = new MediaPlayer(media);
            MusicPlayer.mediaPlayer.play();
                
            MusicPlayer.setNowPlaying(songToPlay);
            
            addProgressListener();
        }
        //Play song at top of list, since were at the end
        else{
            Song songToPlay = currentPlayingList.get(0);
            String songFile = new File(songToPlay.getLocation()).toURI().toString();
            
            Media media = new Media(songFile);
            MusicPlayer.mediaPlayer = new MediaPlayer(media);
            MusicPlayer.mediaPlayer.play();
                
            MusicPlayer.setNowPlaying(songToPlay);
            
            addProgressListener();
        }
            mainController.nowPlayingArtist.setText(MusicPlayer.getPlayingSong().getArtist());
            mainController.nowPlayingSong.setText(MusicPlayer.getPlayingSong().getTitle());
            mainController.timeRemaining.setText(MusicPlayer.getPlayingSong().getLength());
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
    
    protected static void updateValues(){
        Platform.runLater(new Runnable(){
            public void run(){
                double currentTime = MusicPlayer.mediaPlayer.getCurrentTime().toSeconds();
                double duration = MusicPlayer.mediaPlayer.getTotalDuration().toSeconds();
                getMainController().timeSlider.setValue((currentTime / duration) * 100.0);                
            }
        });
    }
    
    
    
    public static void seek(double seconds) {
        if (mediaPlayer != null) {
            mediaPlayer.seek(new Duration(seconds * 1000));
            timerCounter = (int) seconds * 4;
        }
    }
    
    public static MusicPlayerController getMainController(){
        return mainController;
    }
    
    public static void addProgressListener(){
        MusicPlayer.mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                        double duration = MusicPlayer.mediaPlayer.getTotalDuration().toSeconds();
                        mainController.timeSlider.setValue((newValue.toSeconds() / duration) * 100.0);
                        int s = (int) newValue.toSeconds();
                        mainController.timePassed.setText(String.format("%2d:%02d", (s % 3600) / 60, (s % 60)));
                    }   
                });
    }
    

    private static void checkLibraryXML() throws IOException{
        /* function that checks if library exists
        case: check if library was renamed
        case: check if song was added from last start of application
        case: check if library !exist, create file otherwise.
        */
        
        //File libraryXML = new File("C:/Users/elezo/Documents/NetBeansProjects/MusicPlayer/src/MusicPlayer/util/resources/library.xml");
        File libraryXML = new File("src/resources/library.xml");
       

        if (libraryXML.exists()){
            System.out.print("library exists");
            //check to see if we need to add a song to XMl from last run
           
        }
        else{
            System.out.print("library does not exist, creating xml");
            try{
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            
            Element rootElement= doc.createElement("library");
            doc.appendChild(rootElement);
            Element musicLibrary = doc.createElement("musicLibrary");
            rootElement.appendChild(musicLibrary);
            Element songs = doc.createElement("songs");
            rootElement.appendChild(songs);
            Element playlists = doc.createElement("playlists");
            rootElement.appendChild(playlists);
            Element currentPlayingList = doc.createElement("currentPlayingList");
            rootElement.appendChild(currentPlayingList);
                               
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(libraryXML);
            transformer.transform(source, result);
            
            } catch (ParserConfigurationException ex) {
                ex.printStackTrace();
            } catch (TransformerException ex) {
                ex.printStackTrace();
            }
        }
       
    }
    
public static void main(String[] args){
    Application.launch(MusicPlayer.class);
}
}
