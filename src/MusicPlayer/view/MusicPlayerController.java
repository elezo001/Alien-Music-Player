/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicPlayer.view;

import MusicPlayer.MusicPlayer;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

/**
 * FXML Controller class
 *
 * @author elezo
 */
public class MusicPlayerController implements Initializable {
    
    @FXML private ScrollPane subViewRoot;
    @FXML private VBox sideBar;
    @FXML private VBox playlistBox;
    @FXML private VBox dragBox;
    @FXML private ImageView nowPlayingArtwork;
    @FXML private Label nowPlayingTitle;
    @FXML private Label nowPlayingArtist;
    @FXML private Slider timeSlider;
    @FXML private Region frontSliderTrack;    
    @FXML private Label timePassed;
    @FXML private Label timeRemaining;

    @FXML private HBox letterBox;
    @FXML private Separator letterSeparator;

    @FXML private Pane playButton;
    @FXML private Pane pauseButton;
    @FXML private Pane loopButton;
    @FXML private Pane shuffleButton;
    @FXML private HBox controlBox;

    @FXML private TextField searchBox;
    
    @FXML private Label dropped;
    @FXML private ListView<String> songList = new ListView<String>();
    
    
    
    @FXML
    private void handleDragFileIntoBox(DragEvent e){
        if (e.getGestureSource() != dragBox && e.getDragboard().hasFiles()){
            e.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        e.consume();
        }
    
    @FXML
    private void handleFileDrop(DragEvent e){
        Dragboard db = e.getDragboard();
        boolean success = false;
        if (db.hasFiles()){
            List<File> filesToAdd = db.getFiles();
            for(int i=0; i < filesToAdd.size(); i++){
                String songPath = filesToAdd.get(i).toString();
                songList.getItems().add(songPath);                    
                }
            success = true;
        }
     e.setDropCompleted(success);
     e.consume();
    }
    
    @FXML
    private void handleSongClick(MouseEvent e){
        // play song when clicked.
        String songPath = songList.getSelectionModel().getSelectedItem();
        String songFile = new File(songPath).toURI().toString();
        if (MusicPlayer.isPlaying()){
            MusicPlayer.mediaPlayer.stop();
        }
        try{
            Media media = new Media(songFile);
            MusicPlayer.mediaPlayer = new MediaPlayer(media);
            MusicPlayer.mediaPlayer.play();
        }
        catch(Exception exception){
            System.out.print(exception);
        }        
    }
    
    private boolean isSongPlaying(MediaPlayer mediaPlayer){
        Status status = mediaPlayer.getStatus();
        if (status == Status.PLAYING){
            return true;
        }
        return false;
    }


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        
        pauseButton.setOnMouseClicked( x -> {
            System.out.print("Pause button pressed");
            MusicPlayer.pause();
        });
        
        playButton.setOnMouseClicked( x -> {
            MusicPlayer.playPause();
        });
    
    }    
    
}
