/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicPlayer.view;

import MusicPlayer.MusicPlayer;
import MusicPlayer.model.Song;
import MusicPlayer.util.XMLEditor;
import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

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
    @FXML public Slider timeSlider;
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
    @FXML private TableView<Song> songView = new TableView<Song>();
    @FXML private TableColumn<Song, String> titleColumn;
    @FXML private TableColumn<Song, String> artistColumn;
    @FXML private TableColumn<Song, String> albumColumn;
    @FXML private TableColumn<Song, String> lengthColumn;
    @FXML private TableColumn<Song, String> playcountColumn;
    
    
    @FXML
    private void handleDragFileIntoBox(DragEvent e){
        if (e.getGestureSource() != dragBox && e.getDragboard().hasFiles()){
            e.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        e.consume();
        }
    
    @FXML
    private void addSongsFromXML(){
        XMLEditor.addOnStartup();
        ArrayList<String> fileStrings = XMLEditor.getXmlFileNames();
        List<File> songFileList = new ArrayList<File>();
        for (int i=0; i < fileStrings.size(); i++){
            File songFile = new File(fileStrings.get(i));
            songFileList.add(songFile);
        }
            XMLEditor.setFilesToAdd(songFileList);
            XMLEditor.createSongObject();
            ObservableList<Song> list = XMLEditor.getObservableSongs();
            XMLEditor.clearSongsToAdd();
        for(int i=0; i<fileStrings.size(); i++){
            Song song = list.get(i);
            songView.getItems().add(song);
        }
    }
    
    @FXML
    private void handleFileDrop(DragEvent e){
        Dragboard db = e.getDragboard();
        boolean success = false;
        if (db.hasFiles()){
            List<File> filesToAdd = db.getFiles();
            XMLEditor.setFilesToAdd(filesToAdd);
            XMLEditor.addSongToXml();
            ObservableList<Song> list = XMLEditor.getObservableSongs();
            XMLEditor.clearSongsToAdd();
            for(int i=0; i < list.size(); i++){
                Song song = list.get(i);
                songView.getItems().add(song);
                }
            success = true;
        }
     e.setDropCompleted(success);
     e.consume();
    }
    
    /*
    @FXML 
    private void pauseOnSliderDrag(){
        MusicPlayer.pause();
    }
    */
    
    
    /*
    private void showMetadata(String songPath){
        File file = new File(songPath);
        try{
        AudioFile audioFile = AudioFileIO.read(file);
        Tag tag = audioFile.getTag();
        AudioHeader header = audioFile.getAudioHeader();
        
        String title = tag.getFirst(FieldKey.TITLE);
        String artistTitle = tag.getFirst(FieldKey.ALBUM_ARTIST);
        if (artistTitle.equals(null) || artistTitle.equals("") || artistTitle.equals("null")){
            artistTitle = tag.getFirst(FieldKey.ARTIST);
        }
        String artist = (artistTitle == null || artistTitle.equals("") || artistTitle.equals("null")) ? "" : artistTitle;
        
        String album = tag.getFirst(FieldKey.ALBUM);
        
        Duration length = Duration.ofSeconds((long) header.getTrackLength());
        String track = tag.getFirst(FieldKey.TRACK);
        String disc = tag.getFirst(FieldKey.DISC_NO);
        
        int playCount = 0;
        String location = Paths.get(file.getAbsolutePath()).toString();
        
        System.out.print("Title: " + title + " Artist: " + artist + " Album: " + album + " Length: " + length + " Location: " + location);
        
        
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        
    }
    */
    




    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addSongsFromXML();
        
        songView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        titleColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("title"));
        artistColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("artist"));
        albumColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("album"));
        lengthColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("length"));
        
        timeSlider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasChanging, Boolean isChanging){
                if (wasChanging){
                    double duration = MusicPlayer.mediaPlayer.getTotalDuration().toSeconds();
                    double sliderValue = timeSlider.getValue() / 100;
                    double seekTo = (duration * sliderValue);
                    MusicPlayer.mediaPlayer.seek(Duration.seconds(seekTo));
                }
            }
        });

        songView.setRowFactory(x -> {
            TableRow<Song> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
             if (! row.isEmpty() && event.getButton()==MouseButton.PRIMARY 
             && event.getClickCount() == 2){
                 
                 Song song = row.getItem();
                 String songFile = new File(song.getLocation()).toURI().toString();
                 
             if (MusicPlayer.isPlaying()){
                MusicPlayer.mediaPlayer.stop();
            }
            try{
                Media media = new Media(songFile);
                MusicPlayer.mediaPlayer = new MediaPlayer(media);
                MusicPlayer.mediaPlayer.play();
            
                //add listener for slider timer
                MusicPlayer.mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                    @Override
                    public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                        double duration = MusicPlayer.mediaPlayer.getTotalDuration().toSeconds();
                        timeSlider.setValue((newValue.toSeconds() / duration) * 100.0);
                        
                        nowPlayingTitle.setText(Double.toString(timeSlider.getValue()));
                    }   
                });
                
            }
            catch(Exception exception){
                System.out.print(exception);
            }} 
            });
            return row;
        });
        
        pauseButton.setOnMouseClicked( x -> {
            MusicPlayer.pause();
        });
        
        playButton.setOnMouseClicked( x -> {
            MusicPlayer.playPause();
        });
    
    }    
    
}
