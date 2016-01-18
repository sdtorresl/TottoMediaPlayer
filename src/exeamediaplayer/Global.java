/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exeamediaplayer;
import database.*;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.stage.Stage;
import java.util.prefs.Preferences;

/**
 *
 * @author sergio
 */
public class Global {
    private static Global instance = null;
        
    // Locale settings
    private final Locale spanish = new Locale("ES");
    private final Locale english = Locale.ENGLISH;
    private Locale currentLanguage;
    private ResourceBundle labels;    
    private final Locale[] supportedLanguages = {
        spanish,
        english
    };
    
    private User user;
    private Event currentEvent;
    private Stage mainStage;
    private Stage currentStage;
    private Preferences preferences;
    
    private Player player;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Stage getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public Event getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
    }
    
    public Global() {
        currentLanguage = supportedLanguages[0];
        labels = ResourceBundle.getBundle("LabelsBundle", currentLanguage);
        loadPreferences();
    }
    
    private void loadPreferences() {
        preferences = Preferences.userRoot();
    }
    
    public static Global getInstance() {
        if (instance == null) {
            instance = new Global();
        }
        return instance;
    }

    public Locale getCurrentLanguage() {
        return currentLanguage;
    }

    public void setCurrentLanguage(Locale currentLanguage) {
        this.currentLanguage = currentLanguage;
    }

    public void setCurrentLanguage(String language) {
        if(language.equals("ES")) {
            this.currentLanguage = supportedLanguages[0];
        }
        else if (language.equals("EN")) {
            this.currentLanguage = supportedLanguages[1];
        }
    }
    
    public ResourceBundle getLabels() {
        this.labels = ResourceBundle.getBundle("LabelsBundle", this.currentLanguage);
        return this.labels;
    }

    public void setLabels(ResourceBundle labels) {
        this.labels = labels;
    }  
}
