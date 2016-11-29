package com.theasciickers.cs245.theasciickers;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Melanie on 11/25/16.
 */

public class AudioPlayer {
    private MediaPlayer player;

    public void play(Context context){
        if(player == null ){
            player = MediaPlayer.create(context,R.raw.anamanaguchi_prom_night_lindsay_lowend_rmx);
            player.setLooping(true);
        }
        player.start();

    }

    public void stop(){
        if(player != null){
            player.pause(); // used to be release changed to pause to preserve point in song
           // player = null;
        }
    }
}
