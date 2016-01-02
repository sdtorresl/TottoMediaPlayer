/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
    
    // Primary Stage
    private Stage stage;
    
    @FXML
    private Label welcomeLabel;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.global = Global.getInstance();
        this.labels = this.global.getLabels();
        this.user = this.global.getUser();
        
        welcomeLabel.setText(labels.getString("welcome") + ", " + user.getFullName());
    }
    
    @FXML
    public void logout() throws IOException {
        this.global.setUser(new User());
        
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"), this.global.getLabels());
                        
        Scene scene = new Scene(root);
        this.stage = this.global.getMainStage();
        this.stage.setScene(scene);
        
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        this.stage.setX((primScreenBounds.getWidth() - this.stage.getWidth()) / 2);
        this.stage.setY((primScreenBounds.getHeight() - this.stage.getHeight()) / 2);
    }
    
    @FXML
    public void close() {
        this.stage = this.global.getMainStage();
        this.stage.close();
    }

}
