/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exeamediaplayer;

import java.net.URL;
import javax.media.Controller;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import net.sourceforge.jicyshout.jicylib1.SimpleMP3DataSource;

/**
 *
 * @author sergio
 */
public class OnlinePlayer {
    
    public OnlinePlayer(String url) {
        try {
            Player player = Manager.createPlayer(new URL(url));
            player.start();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
}
