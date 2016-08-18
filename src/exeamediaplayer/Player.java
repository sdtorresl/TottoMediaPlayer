/*
 * To change license header, choose License Headers in Project Properties.
 * To change template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exeamediaplayer;

import database.*;
import java.io.File;
import java.lang.Thread.State;
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
//import static javafx.concurrent.Task.State;

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
        
    private User user;
    private Event event;
    private SQLConnector sql;
    private final Preferences pref;
    
    private String currentFolder;
    private final String OS = System.getProperty("os.name");
    
    // Create media player
    private Media media;
    private MediaPlayer mediaPlayer;
    private Thread th;
    
    // Task to play automatically the next media source
    private Task<Integer> task = new Task<Integer>() {
        @Override 
        protected Integer call() throws Exception {
                      
            while(true) {
                status = mediaPlayer.getStatus();
                
                // When media is finished
                if (mediaPlayer.getCurrentTime().greaterThanOrEqualTo(mediaPlayer.getStopTime())) {
                    media = new Media(getNextMediaURL());
                    mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.seek(mediaPlayer.getStartTime());
                    atEndOfMedia = false;
                    LOG.log(Level.INFO, "The media source is: ", mediaURL);
                    mediaPlayer.play();
                }
                
                // Block the thread for a short time
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

    public Thread getThread() {
        return th;
    }

    public void setThread(Thread th) {
        this.th = th;
    }

    public Task<Integer> getTask() {
        return task;
    }

    public void setTask(Task<Integer> task) {
        this.task = task;
    }
    
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
        
        event = sql.getEvent(user.getUsername(), user.getCountry());
        global.setCurrentEvent(event);
        pref = global.getPreferences();
        currentFolder = "";
        
        rootFolder = pref.get("ROOT_FOLDER", System.getProperty("user.dir"));
        System.out.println(rootFolder);
        
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
                LOG.log(Level.SEVERE, "An error ocurred creating new Media Player");
            }
        } else {
            media = new Media("");
            mediaPlayer = new MediaPlayer(media);
            LOG.log(Level.WARNING, "Can't get the media URL");
        }
        
        th = new Thread(task);
    }
    
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
        
        if(th.getState() == State.NEW) {
            System.out.println("Thread started!");
            th.start();
        }
    }
    
    public void stop() {
        task.cancel();
        th.interrupt();
        mediaPlayer.stop();
    }
    
    public void pause() {
        mediaPlayer.pause();
    }
    
    public void next() {
        mediaURL = getNextMediaURL();
        pause();
        mediaPlayer = new MediaPlayer(new Media(mediaURL));
        play();
    }
    
    /**
     * Implements the logic to get the next media source
     * based in a defined criteria
     * 
     * @return URL
     */
    private String getNextMediaURL() {
        Global global = Global.getInstance();
        sql = new SQLConnector();
        event = sql.getEvent(user.getUsername(), user.getCountry());
        global.setCurrentEvent(event);
        
        categories = event.getCategories();
        Category category;
        
        try {
            totalCategories = categories.size() - 1;
            category = categories.get(currentCategory);
        }
        catch (IndexOutOfBoundsException ex) {
            LOG.log(Level.WARNING, "No categories related to this event");
            return null;
        }
        
        // Counter of the current category
        if(currentCategory == totalCategories) {
            currentCategory = 0;
        }
        else {
            currentCategory++;
        }
        
        try {
            currentFolder = rootFolder + File.separator + category.getFolderName();

            LOG.log(Level.INFO, "Category: {0}", category.getName());
            LOG.log(Level.INFO, "Folder: {0}", currentFolder);

            File directory = new File(currentFolder);

            // Get all the files from a directory
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
        } catch (Exception ex) {
            LOG.log(Level.INFO, "Can't get file from category");
        }   
        
        return nextMediaURL;
    }
}
