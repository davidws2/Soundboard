package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContextWrapper;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;

import static android.app.PendingIntent.getActivity;

public class MainActivity extends AppCompatActivity {
    Button playbtn;
    Button stopbtn;
    Button recordbtn;
    Button pausebtn;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    File rootDataDir;
    String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootDataDir = this.getFilesDir();
        path = rootDataDir.getPath() + "/app/src/main/res/raw/sound.mp3";
        playbtn = (Button) findViewById(R.id.play);
        stopbtn = (Button) findViewById(R.id.stopRecord);
        recordbtn = (Button) findViewById(R.id.record);
        pausebtn = (Button) findViewById(R.id.pause);
        playbtn.setEnabled(false);
        pausebtn.setEnabled(false);
        stopbtn.setEnabled(false);
        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playback();
            }
        });
        pausebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause();
            }
        });
        recordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                record();
            }
        });
        stopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop();
            }
        });

    }

    private void record() {
        if (mediaRecorder != null) {
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaPlayer.reset();
            mediaPlayer.release();
        }
        stopbtn.setEnabled(true);
        playbtn.setEnabled(false);
        pausebtn.setEnabled(false);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mediaRecorder.setOutputFile(path);
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    private void playback() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.sound);
        }
        mediaPlayer.start();
        pausebtn.setEnabled(true);
        recordbtn.setEnabled(false);
        stopbtn.setEnabled(false);
    }
    private void pause() {
        mediaPlayer.pause();
        recordbtn.setEnabled(true);
        stopbtn.setEnabled(true);
    }
    private void stop() {
        mediaRecorder.stop();
        playbtn.setEnabled(true);
    }
}
