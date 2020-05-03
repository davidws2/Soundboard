package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static android.app.PendingIntent.getActivity;

public class MainActivity extends AppCompatActivity {
    Button playbtn;
    Button stopbtn;
    Button recordbtn;
    Button pausebtn;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    String path;
    Spinner dropdown;
    ArrayAdapter<String> adapter;
    String speed;
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
        dropdown = findViewById(R.id.spinner);
        String[] items = new String[]{"x.25", "x.5", "x1","x2","x4"};
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        if (permission() == false) {
            requestPermission();
        }
        playbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
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
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/sound.3gp";
        stopbtn.setEnabled(true);
        playbtn.setEnabled(false);
        pausebtn.setEnabled(false);
        recordbtn.setEnabled(false);
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(path);
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void playback() {
        mediaPlayer = new MediaPlayer();
        speed = dropdown.getSelectedItem().toString();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            if (speed.equals("x.25")) {
                mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed((float) .25));
            } else if (speed.equals("x.5")) {
                mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed((float) .5));
            } else if (speed.equals("x2")) {
                mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed((float) 2));
            } else if (speed.equals("x4")) {
                mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed((float) 4));
            }
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        pausebtn.setEnabled(true);
        recordbtn.setEnabled(false);
        stopbtn.setEnabled(false);
    }
    private void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        recordbtn.setEnabled(true);
    }
    private void stop() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        playbtn.setEnabled(true);
        recordbtn.setEnabled(true);
        stopbtn.setEnabled(false);
    }
    private boolean permission() {
        int mic = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return mic == PackageManager.PERMISSION_GRANTED && write == PackageManager.PERMISSION_GRANTED
                && read == PackageManager.PERMISSION_GRANTED;
    }


}
