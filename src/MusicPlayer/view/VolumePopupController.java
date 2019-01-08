/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicPlayer.view;

import MusicPlayer.MusicPlayer;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author elezo
 */
public class VolumePopupController implements Initializable {
    
    @FXML private Slider volumeSlider;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
         volumeSlider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasChanging, Boolean isChanging){
                if (wasChanging){
                    double volume = volumeSlider.getValue() / 100; 
                    MusicPlayer.mediaPlayer.setVolume(volume);
                }
            }
        });
    }

    public Slider getSlider(){
        return volumeSlider;
    }
    
    
    
}
