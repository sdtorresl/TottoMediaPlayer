/*
 * To change license header, choose License Headers in Project Properties.
 * To change template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exeamediaplayer;

import database.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import util.CircularArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.concurrent.Task;
import javafx.util.Duration;

/**
 *
 * @author sergio
 */
public class Player {
    private static final Logger LOG = Logger.getLogger(LoginController.class.getName());
    
    private String mediaURL;
    private String nextMediaURL;
    private final String rootFolder;
    private Boolean atEndOfMedia;
    private Status status;
    private CircularArrayList<Category> categories;
    private int currentCategory;
    private int totalCategories;
        
    private final User user;
    private final Event event;
    private final SQLConnector sql;
    private final Preferences pref;
    
    private String currentFolder;
    private final String OS = System.getProperty("os.name");
    
    // Create media player
    private Media media;
    private MediaPlayer mediaPlayer;
    private Thread th;
    
    public Boolean getAtEndOfMedia() {
        return atEndOfMedia;
    }

    public void setAtEndOfMedia(Boolean atEndOfMedia) {
        this.atEndOfMedia = atEndOfMedia;
    }

    public Status getStatus() {
        return mediaPlayer.getStatus();
    }
        
    public MediaPlayer getMediaPlayer() {
        return this.mediaPlayer;
    }
    
    public Player() {
        Global global = Global.getInstance();
        sql = new SQLConnector();
        user = global.getUser();
        event = sql.getEvent(user.getUsername(), 47, "Saturday");
        pref = global.getPreferences();
        currentFolder = "";
        
        rootFolder = pref.get("ROOT_FOLDER", "/Music");
        LOG.log(Level.INFO, "Player started!");
        LOG.log(Level.INFO, "Root Folder: {0}", rootFolder);   
        LOG.log(Level.INFO, "Operative System: {0}", OS);
        System.out.print(event);
        
        // Initialize the current category
        currentCategory = 0;
        mediaURL = getNextMediaURL();
        
        if(mediaURL != null || !"".equals(mediaURL)) {
            try {
                media = new Media(mediaURL); 
                mediaPlayer = new MediaPlayer(media);
                atEndOfMedia = false;
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "An error ocurred creating new Media Player", ex);
            }
        } else {
            LOG.log(Level.WARNING, "Can't get the media URL");
        }
        
        th = new Thread(task);
    }
    
    private Task<Integer> task = new Task<Integer>() {
        @Override protected Integer call() throws Exception {
                       
            while(true) {
                status = mediaPlayer.getStatus();
                
                if (mediaPlayer.getCurrentTime().greaterThanOrEqualTo(mediaPlayer.getStopTime())) {
                    media = new Media(getNextMediaURL());
                    mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.seek(mediaPlayer.getStartTime());
                    atEndOfMedia = false;
                    LOG.log(Level.INFO, "The media source is: ", mediaURL);
                    mediaPlayer.play();
                }
                
                // Now block the thread for a short time, but be sure
                // to check the interrupted exception for cancellation!
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException interrupted) {
                    if (isCancelled()) {
                        LOG.log(Level.INFO, "Player finished");
                        break;
                    }
                }
            }
            return 0;
        }
    };
    
    @FXML
    public void play() {
        if(mediaPlayer == null) {
            try {
                media = new Media(mediaURL); 
                mediaPlayer = new MediaPlayer(media);
                atEndOfMedia = false;
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "An error ocurred creating new Media Player", ex);
                return;
            }
        } 
        
        mediaPlayer.play();
        
        status = mediaPlayer.getStatus();
 
        if (status == Status.UNKNOWN  || status == Status.HALTED) {
           // don't do anything in these states
           return;
        }
 
        if ( status == Status.PAUSED || status == Status.READY || status == Status.STOPPED) {
            // rewind the movie if we're sitting at the end
            if (atEndOfMedia) {
                mediaPlayer.seek(mediaPlayer.getStartTime());
                atEndOfMedia = false;
            }
            mediaPlayer.play();
        } else {
            mediaPlayer.pause();
        }
        
        th.start();
    }
    
    public void stop() {
        task.cancel();
        th.interrupt();
        mediaPlayer.stop();
    }
    
    public void pause() {
        task.cancel();
        th.interrupt();
        mediaPlayer.pause();
    }
    
    public void next() {
        mediaURL = getNextMediaURL();
        pause();
        mediaPlayer = new MediaPlayer(new Media(mediaURL));
        play();
    }
    
    /**
     * 
     * @return 
     */
    private String getNextMediaURL() {
                       
        categories = event.getCategories();
        totalCategories = categories.size() - 1;
        Category category = categories.get(currentCategory);
        
        // Counter of the current category
        if(currentCategory == totalCategories) {
            currentCategory = 0;
        }
        else {
            currentCategory++;
        }
        
        currentFolder = rootFolder + File.separator + category.getFolderName();

        LOG.log(Level.INFO, "Category: {0}", category.getName());
        LOG.log(Level.INFO, "Folder: {0}", currentFolder);
        
        File directory = new File(currentFolder);

        // get all the files from a directory
        File[] filesList = directory.listFiles();
        ArrayList<File> musicFiles = new ArrayList<>();
        
        for (File file : filesList) {
            if (file.isFile())
                musicFiles.add(file);
        }
        
        // Get random file from category folder
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(musicFiles.size());
        File currentFile = musicFiles.get(index);
        nextMediaURL = currentFile.toURI().toString();
        LOG.log(Level.INFO, "File: {0}", nextMediaURL);
        
        return nextMediaURL;
    }
}
