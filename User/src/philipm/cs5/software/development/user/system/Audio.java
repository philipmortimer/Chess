/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package philipm.cs5.software.development.user.system;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


/**
 * Plays desired sounds
 * @author mortimer
 */
public class Audio{
    /**
     * Plays the specified sound file. The sound method is threaded
     * @param PATH The file name
     */
    public static synchronized void playSound(final String PATH) {
       //creates new thread that plays the desired sound clip
      new Thread(new Runnable() {
          /**
           * Plays the chosen audio clip
           */
        @Override
        public void run() {
          try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(
              new File(PATH));
            clip.open(inputStream);
            clip.start(); 
          } catch (Exception e) {
            
          }
        }
      }).start();
    }
}
