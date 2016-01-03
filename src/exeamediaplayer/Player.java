/*
 * To change license header, choose License Headers in Project Properties.
 * To change template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exeamediaplayer;

import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

/**
 *
 * @author sergio
 */
public class Player {
    private String mediaURL = "file:///home/sergio/Music/springtide_-_06_-_Bright_Sunny_End.mp3";
    private String prevMediaURL = "file:///home/sergio/Music/palimpsest_trio_-_06_-_enjambements.mp3";
    private String nextMediaURL = "file:///home/sergio/Music/palimpsest_trio_-_06_-_enjambements.mp3";
    private Boolean atEndOfMedia;
    private Status status;

    public Boolean getAtEndOfMedia() {
        return atEndOfMedia;
    }

    public void setAtEndOfMedia(Boolean atEndOfMedia) {
        this.atEndOfMedia = atEndOfMedia;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
    // create media player
    Media media;
    MediaPlayer mediaPlayer;
    
    public Media getMedia() {
        return this.media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public MediaPlayer getMediaPlayer() {
        return this.mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }
    
    public Player() {
       media = new Media(mediaURL);
       mediaPlayer = new MediaPlayer(media);
       atEndOfMedia = false;
    }
    
    public Player(String mediaURL) {
        this.mediaURL = mediaURL;
        prevMediaURL = null;
        media = new Media(this.mediaURL);
        mediaPlayer = new MediaPlayer(media);
    }
    
    @FXML
    public void play() {
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
    }
    
    public void stop() {
        mediaPlayer.stop();
    }
    
    public void pause() {
        mediaPlayer.pause();
    }
    
    public void next() {
        prevMediaURL = mediaURL;
        mediaURL = nextMediaURL;
        pause();
        media = new Media(mediaURL);
        play();
        //TODO
        //nextMediaURL = getNextMediaURL();
    }
    
    public void prev() {
        //if (prevMediaURL != null) {
            nextMediaURL = mediaURL;
            mediaURL = prevMediaURL; 
            pause();
            media = new Media(mediaURL);
            play();
        //}
    }
    
}
