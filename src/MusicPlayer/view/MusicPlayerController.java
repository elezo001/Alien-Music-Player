/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicPlayer.view;

import java.net.URL;
import java.util.ResourceBundle;
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
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

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
    @FXML private ListView<String> songList;
    
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
            songList.getItems().add(db.getFiles().toString());
            success = true;
        }
     e.setDropCompleted(success);
     e.consume();
    }


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
    
    }    
    
}