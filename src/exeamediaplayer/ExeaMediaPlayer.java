/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exeamediaplayer;

import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author sergio
 */
public class ExeaMediaPlayer extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        
        Global global = Global.getInstance();
        Preferences pref = global.getPreferences();
        global.setCurrentLanguage(pref.get("LOCALE", "ES"));
        global.setMainStage(stage);
        
        ResourceBundle labels = global.getLabels();
        
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"), labels);
        
        Scene scene = new Scene(root);
        stage.setTitle(labels.getString("appTitle"));
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
