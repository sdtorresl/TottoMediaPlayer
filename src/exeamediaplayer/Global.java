/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exeamediaplayer;

import java.util.Locale;
import java.util.ResourceBundle;
import javafx.stage.Stage;

/**
 *
 * @author sergio
 */
public class Global {
    private static Global instance = null;
    
    private final Locale spanish = new Locale("ES");
    private final Locale english = Locale.ENGLISH;
        
    private final Locale[] supportedLanguages = {
        spanish,
        english
    };

    private Locale currentLanguage;
    private ResourceBundle labels;
    private User user;
    
    private Stage mainStage;

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
    
    public Global() {
        this.currentLanguage = supportedLanguages[0];
        this.labels = ResourceBundle.getBundle("LabelsBundle", this.currentLanguage);
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
