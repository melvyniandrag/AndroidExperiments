package com.ballofknives.hellomoontake2;

import android.content.Context;
import android.media.MediaPlayer;

public class AudioPlayer {
    private MediaPlayer mPlayer;
    public boolean isPaused = false;

    public void stop(){
        isPaused = false;
        if(mPlayer!=null){
            mPlayer.release();
            mPlayer = null;
        }
    }

    public void play(Context c){
        isPaused = false;
        stop();
        mPlayer = MediaPlayer.create(c, R.raw.one_small_step);
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stop();
            }
        });
        mPlayer.start();
    }

    public void pause(){
        if(mPlayer!=null){
            if(isPaused){
                mPlayer.start();
                isPaused = false;
            }
            else {
                mPlayer.pause();
                isPaused = true;
            }
        }
    }

}
