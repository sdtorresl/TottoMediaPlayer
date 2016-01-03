/*
 * To change license header, choose License Headers in Project Properties.
 * To change template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exeamediaplayer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Screen;
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
    
    // Primary Stage
    private Stage stage;
    
    @FXML
    private Label welcomeLabel;
    @FXML
    private Button playPauseButton;
    @FXML
    private Button prevButton;
    @FXML 
    private Button nextButton;
    
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
    }
    
    @FXML
    public void logout() throws IOException {
        global.setUser(new User());
        
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"), global.getLabels());
                        
        Scene scene = new Scene(root);
        stage = global.getMainStage();
        stage.setScene(scene);
        
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }
    
    @FXML
    public void close() {
        stage = global.getMainStage();
        stage.close();
    }

    @FXML
    public void play() {
        player.play();
    }
    
    @FXML
    public void pause() {
        player.pause();
    }
    
    @FXML
    public void next() {
        player.next();
    }
    
    public void prev() {
        player.prev();
    }
}