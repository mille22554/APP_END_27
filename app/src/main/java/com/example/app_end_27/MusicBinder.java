package com.example.app_end_27;

import android.media.MediaPlayer;
import android.os.Binder;

class MusicBinder extends Binder {

    private MediaPlayer mediaPlayer;

    public MusicBinder(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void pauseMusic() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void playMusic() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public int getProgress() {
        return mediaPlayer.getDuration();
    }

    public int getPlayPosition() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public void seekToPosition(int sec) {
        mediaPlayer.seekTo(sec);
    }

    public void closeMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
