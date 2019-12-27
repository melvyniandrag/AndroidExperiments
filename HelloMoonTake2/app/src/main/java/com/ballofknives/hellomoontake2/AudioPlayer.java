package com.ballofknives.hellomoontake2;

import android.content.Context;
import android.media.MediaPlayer;

public class AudioPlayer {
    private MediaPlayer mPlayer;

    public void stop(){
        if(mPlayer!=null){
            mPlayer.release();
            mPlayer = null;
        }
    }

    public void play(Context c){
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

}
