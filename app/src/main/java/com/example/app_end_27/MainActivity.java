package com.example.app_end_27;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SeekBar sbPosition;
    TextView tvElapsed, tvRemain;
    Button btPlay, btStop;

    private MusicBinder myBinder;
    private Handler mHandler = new Handler();
    Intent MediaServiceIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MediaServiceIntent = new Intent(this, MediaService.class);
        startService(MediaServiceIntent);
        bindService(MediaServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        btPlay = findViewById(R.id.button_Play);
        sbPosition = findViewById(R.id.seekBar_Position);
        tvElapsed = findViewById(R.id.textview_ElapsedTime);
        tvRemain = findViewById(R.id.textview_RemainingTime);
        btStop = findViewById(R.id.button_Stop);

    }
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (MusicBinder) service;
            if (myBinder.isPlaying()) {
                btPlay.setBackgroundResource(R.drawable.ic_baseline_pause_24);
            } else {
                btPlay.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
            }
            sbPosition.setMax(myBinder.getProgress());
            sbPosition.setOnSeekBarChangeListener(position);

            btPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setIsPlayButton(btPlay);
                }
            });
            btStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mHandler.removeCallbacks(runnable);
                    myBinder.closeMedia();
                    stopService(MediaServiceIntent);
                    finish();
                }
            });
            mHandler.post(runnable);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                sbPosition.setProgress(myBinder.getPlayPosition());
                String elapsedTime = createTimeLabel(myBinder.getPlayPosition());
                tvElapsed.setText(elapsedTime);
                String remainingTime = createTimeLabel(myBinder.getProgress() - myBinder.getPlayPosition());
                tvRemain.setText("- " + remainingTime);
                mHandler.postDelayed(runnable, 1000);
            }
            catch (Exception e){
            }
        }
    };
    private void setIsPlayButton(Button bt) {
        if (myBinder.isPlaying()) {
            myBinder.pauseMusic();
            bt.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
        } else {
            myBinder.playMusic();
            bt.setBackgroundResource(R.drawable.ic_baseline_pause_24);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        unbindService(mServiceConnection);
    }
    private SeekBar.OnSeekBarChangeListener position = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean isSeekbarOnTouch) {
            if (isSeekbarOnTouch) {
                myBinder.seekToPosition(i);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };
    private String createTimeLabel(int time) {
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;
        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;
        return timeLabel;
    }
}