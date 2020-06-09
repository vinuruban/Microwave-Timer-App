package com.example.microwavetimer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int timerNumber; //integer set as seconds - not visible in UI
    SeekBar timerSeekBar;
    Button button;
    boolean buttonCheck;
    CountDownTimer countDownTimer;
    TextView timerView;
    int maxTime = 120;
    int startTime = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerView = (TextView) findViewById(R.id.timerView);
        buttonCheck = true;

        timerSeekBar = (SeekBar) findViewById(R.id.timerSeekBar);

        timerSeekBar.setMax(maxTime);
        timerSeekBar.setProgress(startTime);
        timerNumber = startTime; //in case button is clicked without altering the seek bar

        timerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int min = 1;

                if (progress < min) {
                    timerNumber = min;
                    timerSeekBar.setProgress(min);
                } else {
                    timerNumber = progress;
                }

                timeConvertion(timerNumber);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        //hit timer
        button = (Button) findViewById(R.id.timerButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buttonCheck == true) {
                    timerSeekBar.setEnabled(false);
                    startTimer();
                    button.setText("Stop!");
                    buttonCheck = false;
                }
                else {
                    resetTimer();
                }
            }
        });

    }

    public void startTimer() {
        countDownTimer = new CountDownTimer(timerNumber * 1000, 1000) {

            public void onTick(long millisecondsUntilDone) {
                int second = (int) millisecondsUntilDone/1000; //change from long to int
                timeConvertion(second);
            }

            public void onFinish() {
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.done);
                mediaPlayer.start();
                resetTimer();
            }
        }.start();
    }

    //below is the logic to convert seconds into minutes format
    public void timeConvertion(int sec) {
        int minutes = sec / 60; //rounds up to the closest integer
        int seconds = sec - (minutes * 60);
        String secondsString = "" + seconds;

        if (secondsString.length() != 2) { //ensure seconds are always shown as 2 digits
            secondsString = "0" + secondsString;
        }

        String convertion = minutes + ":" + secondsString;
        timerView.setText(convertion);
    }

    //reset timer
    public void resetTimer() {
        timerSeekBar.setEnabled(true);
        timerView.setText("0:20");
        timerSeekBar.setProgress(startTime);
        countDownTimer.cancel();
        button.setText("Go!");
        buttonCheck = true;
    }
}