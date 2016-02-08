/*
 * To change license header, choose License Headers in Project Properties.
 * To change template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exeamediaplayer;

import database.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author sergio
 */
public class MainController implements Initializable {
    
    // Global variables
    private User user;
    private Global global;
    private ResourceBundle labels;
    private Player player;
    private Boolean playing;
    
    // Primary Stage
    private Stage stage;
    
    @FXML
    private Label welcomeLabel;
    @FXML
    private ToggleButton playPauseButton;
    @FXML
    private Label listeningLabel;
    @FXML
    private Label listeningOnLabel;
    @FXML
    private ToggleButton playPauseOnButton;
        
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        global = Global.getInstance();
        labels = global.getLabels();
        user = global.getUser();
        
        welcomeLabel.setText(labels.getString("welcome") + ", " + user.getFullName());
                
        player = new Player();
        global.setPlayer(player);
    }
    
    @FXML
    public void logout() throws IOException {
        player.stop();
        global.setUser(new User());
        
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"), global.getLabels());
                        
        Scene scene = new Scene(root);
        stage = global.getMainStage();
        stage.centerOnScreen();
        stage.hide();
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    public void close() {
        player.stop();
        stage = global.getMainStage();
        stage.close();
    }

    @FXML
    public void play() {
        player.play();
        playing = !(player.getStatus() == Status.PLAYING);
        playPauseButton.setSelected(playing);
        if (player.getStatus() == Status.PLAYING)
            listeningLabel.setText(global.getCurrentEvent().getName());
        else
            listeningLabel.setText(labels.getString("eventError"));
    }
    
    @FXML
    public void playOnline() {
        player.play();
        playing = !(player.getStatus() == Status.PLAYING);
        playPauseButton.setSelected(playing);
    }
    
    @FXML
    public void next() {
        player.next();
        playing = !(player.getStatus() == Status.PLAYING);
        playPauseButton.setSelected(playing);
        if (player.getStatus() == Status.PLAYING)
            listeningLabel.setText(global.getCurrentEvent().getName());
        else
            listeningLabel.setText(labels.getString("eventError"));
    }
    
    @FXML
    public void edit() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Edit.fxml"), global.getLabels());
                        
        Scene scene = new Scene(root);
        Stage editStage = new Stage();
        global.setCurrentStage(editStage);
        editStage.centerOnScreen();
        editStage.setScene(scene);
        editStage.show();
    }
}