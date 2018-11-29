package com.example.trianglesolutions.musicplayer;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView startTime, endTime;
    SeekBar volumeSeekbar, songTimeSeekbar;
    ImageButton play;
    ImageView songImage;

    MediaPlayer mediaPlayer;
    int totalTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        volumeSeekbar = findViewById(R.id.volumeSeekbar);
        songTimeSeekbar = findViewById(R.id.songTimeSeekbar);
        play = findViewById(R.id.play);
        songImage = findViewById(R.id.songImage);

        mediaPlayer = MediaPlayer.create(this, R.raw.song1);
        mediaPlayer.setLooping(true);
        mediaPlayer.seekTo(0);
        mediaPlayer.setVolume(0.5f, 0.5f);
        totalTime = mediaPlayer.getDuration();

        songImage.setBackgroundResource(R.drawable.sallu);

        songTimeSeekbar.setMax(totalTime);
        songTimeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mediaPlayer.seekTo(progress);
                    songTimeSeekbar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volumeNum = progress/100f;
                mediaPlayer.setVolume(volumeNum, volumeNum);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(mediaPlayer != null){
                    try {
                        Message msg = new Message();
                        msg.what = mediaPlayer.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    }catch (Exception e){}
                }
            }
        }).start();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                    play.setBackgroundResource(R.drawable.pause);
                }else{
                    mediaPlayer.stop();
                    play.setBackgroundResource(R.drawable.play);
                }
            }
        });
        }
    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            int currentPostion = msg.what;
            songTimeSeekbar.setProgress(currentPostion);

            String elapsedTime = createElapsedTime(currentPostion);
            startTime.setText(elapsedTime);

            String remainingTime = createElapsedTime(totalTime-currentPostion);
            endTime.setText("-"+remainingTime);
        }
    };

    public String createElapsedTime(int time){
        String timelabel = "";
        int min = time/1000/60;
        int sec = time/1000%60;

        timelabel = min+ ":";
        if(sec<10)
            timelabel += "0";
        timelabel += sec;

        return timelabel;
    }

}
