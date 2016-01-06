/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exeamediaplayer;

import java.io.File;
import java.nio.file.Files;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author sergio
 */
public class EditController implements Initializable {
    
    private Global global;
    private ResourceBundle labels;
    
    @FXML
    private TextField selectedFolder;
    @FXML
    private Label errorLabel;
    @FXML
    private ChoiceBox lanChoiceBox;
    
    private Stage editStage;
    private Preferences pref;
    private String folder;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        global = Global.getInstance();
        pref = global.getPreferences();
        labels = global.getLabels();
        
        selectedFolder.setText(pref.get("ROOT_FOLDER", ""));
        Locale currentLanguage = global.getCurrentLanguage();
        
        lanChoiceBox.setItems(FXCollections.observableArrayList("Español", "English"));
        
        if(currentLanguage.getDisplayName().equals("Spanish")) {
            lanChoiceBox.getSelectionModel().select(0);
        } else {
            lanChoiceBox.getSelectionModel().select(1);
        }   
    }    
    
    @FXML
    public void selectFolder() {
        Stage directoryStage = new Stage();
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("JavaFX Projects");
        File defaultDirectory = new File("/home");
        chooser.setInitialDirectory(defaultDirectory);
        File selectedDirectory = chooser.showDialog(directoryStage);
        folder = selectedDirectory.getPath();
        selectedFolder.setText(folder);
        errorLabel.setVisible(false);
    }
    
    @FXML
    public void save() {
        Boolean error = false;
        folder = selectedFolder.getText();
        Path path = Paths.get(folder);
        if (Files.exists(path)) {
            pref.put("ROOT_FOLDER", folder);
            editStage = global.getCurrentStage();
            editStage.close();
        } else {
            errorLabel.setVisible(true);
            error = true;
        }
        
        if (!error) {
            if (lanChoiceBox.getValue().toString().equals("Español")) {
                pref.put("LOCALE", "ES");
                global.setCurrentLanguage("ES");
            } 
            if (lanChoiceBox.getValue().toString().equals("English")) {
                pref.put("LOCALE", "EN");
                global.setCurrentLanguage("EN");
            }
            Alert alert = new Alert(AlertType.INFORMATION, 
                    labels.getString("restartRequired"), 
                    ButtonType.YES);
            alert.showAndWait();
        }
    }
    
    @FXML
    public void cancel() {
        editStage = global.getCurrentStage();
        editStage.close();
    }
}
