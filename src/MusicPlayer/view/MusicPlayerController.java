/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicPlayer.view;

import MusicPlayer.MusicPlayer;
import MusicPlayer.model.Library;
import MusicPlayer.model.Song;
import MusicPlayer.util.XMLEditor;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
    @FXML public Label nowPlayingSong;
    @FXML public Label nowPlayingArtist;
    @FXML public Slider timeSlider;
    @FXML private Region frontSliderTrack;    
    @FXML public Label timePassed;
    @FXML public Label timeRemaining;
    
    private Stage volumePopup;
    private VolumePopupController volumePopupController;

    @FXML private HBox letterBox;
    @FXML private Separator letterSeparator;
    
    @FXML private HBox artistsButton;

    @FXML private Pane playButton;
    @FXML private Pane pauseButton;
    @FXML private Pane nextButton;
    @FXML private Pane loopButton;
    @FXML private Pane shuffleButton;
    @FXML private HBox controlBox;

    @FXML private TextField searchBox;
    
    @FXML private Label dropped;
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
            ObservableList<Song> list = XMLEditor.getObservableSongsToAdd();
            MusicPlayer.setCurrentPlayingList(list);
            XMLEditor.clearSongsToAdd();
        for(int i=0; i<fileStrings.size(); i++){
            Song song = list.get(i);
            songView.getItems().add(song);
        }
        MusicPlayer.printCurrentPlayingList();
    }
    

    @FXML
    private void handleFileDrop(DragEvent e){
        Dragboard db = e.getDragboard();
        boolean success = false;
        if (db.hasFiles()){
            List<File> filesToAdd = db.getFiles();
            XMLEditor.setFilesToAdd(filesToAdd);
            XMLEditor.addSongToXml();
            ObservableList<Song> list = XMLEditor.getObservableSongsToAdd();
            XMLEditor.clearSongsToAdd();
            for(int i=0; i < list.size(); i++){
                Song song = list.get(i);
                songView.getItems().add(song);
                }
            success = true;
            MusicPlayer.setCurrentPlayingList(Library.getObservableSongs());
        }
        
     e.setDropCompleted(success);
     e.consume();
     MusicPlayer.printCurrentPlayingList();
    }
    
    @FXML private void loadView(String viewName) throws IOException{
        FXMLLoader loader = FXMLLoader.load(getClass().getResource(viewName));
        BorderPane view = loader.load();
        
        Scene newScene = new Scene(view);
        
        MusicPlayer.getStage().setScene(newScene);
        
    }
    
    private void createVolumePopup(){
        try{
        Stage stage = MusicPlayer.getStage();
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/MusicPlayer/View/VolumePopup.fxml"));
        VBox view = loader.load();
        volumePopupController = loader.getController();
        Stage popup = new Stage();
        popup.initStyle(StageStyle.UNDECORATED);
        popup.setScene(new Scene(view));
        popup.initOwner(stage);
        
        volumePopup = popup;
        
        stage.widthProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> obs, Number oldVal, Number newVal){
                if ((newVal.intValue() > oldVal.intValue()) || (newVal.intValue() < oldVal.intValue())){
                volumePopup.hide();
                }
            }
        });
        
        stage.heightProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> obs, Number oldVal, Number newVal){
                if ((newVal.intValue() > oldVal.intValue()) || (newVal.intValue() < oldVal.intValue())){
                volumePopup.hide();
                }
            }
        });
        
        
    }   
        catch (IOException ex) {
            Logger.getLogger(MusicPlayerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    public void volumeClick() {
    	if (!volumePopup.isShowing()) {
    		Stage stage = MusicPlayer.getStage();
    		volumePopup.setX(stage.getX() + stage.getWidth() - 30);
        	volumePopup.setY(stage.getY() + stage.getHeight() - 360);
    		volumePopup.show();
    	}
        else{
            volumePopup.hide();
        }
    }

    
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
        
        createVolumePopup();


        
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
                
                MusicPlayer.setNowPlaying(song);
                
                MusicPlayer.mediaPlayer.setOnEndOfMedia(new Runnable() {
                    @Override
                    public void run() {
                        MusicPlayer.next();
                    }
                });

                
                nowPlayingArtist.setText(MusicPlayer.getPlayingSong().getArtist());
                nowPlayingSong.setText(MusicPlayer.getPlayingSong().getTitle());
                timeRemaining.setText(song.getLength());
            
                //add listener for slider timer
                MusicPlayer.addProgressListener();

                
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
        
        nextButton.setOnMouseClicked( x -> {
            MusicPlayer.next();            
        });
                        
        artistsButton.setOnMouseClicked(x -> {
            try {
                loadView("/Musicplayer/View/Artists.FXML");
            } catch (IOException ex) {
                Logger.getLogger(MusicPlayerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
    
    }    
    
}
