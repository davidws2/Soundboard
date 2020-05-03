package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
    String path;
    final int REQUEST_PERMISSION_CODE = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playbtn = (Button) findViewById(R.id.play);
        stopbtn = (Button) findViewById(R.id.stopRecord);
        recordbtn = (Button) findViewById(R.id.record);
        pausebtn = (Button) findViewById(R.id.pause);
        playbtn.setEnabled(false);
        pausebtn.setEnabled(false);
        stopbtn.setEnabled(false);

        if (permission() == false) {
            requestPermission();
        }
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

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case REQUEST_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);
            }
                break;
        }
    }

    private void record() {
        if (mediaRecorder != null) {
            mediaRecorder.reset();
            mediaRecorder.release();
        }
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/soundBoard_sound";
        stopbtn.setEnabled(true);
        playbtn.setEnabled(false);
        pausebtn.setEnabled(false);
        mediaRecorder = new MediaRecorder();
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
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
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
    private boolean permission() {
        int mic = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return mic == PackageManager.PERMISSION_GRANTED && write == PackageManager.PERMISSION_GRANTED
                && read == PackageManager.PERMISSION_GRANTED;
    }


}
