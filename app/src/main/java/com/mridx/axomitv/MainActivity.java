package com.mridx.axomitv;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.google.android.exoplayer2.ui.PlayerView;

public class MainActivity extends AppCompatActivity {

    private PlayerView playerView;
    private PlayerManager playerManager;
    String contentUri = "http://vidnetcdn.vidgyor.com/assamtalks-origin/liveabr/assamtalks-origin/live1/chunks.m3u8";

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerView = findViewById(R.id.videoPlayer);

        playerManager = new PlayerManager(this);

        progressDialog = new ProgressDialog(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        playerManager.init(this, playerView , contentUri);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        playerManager.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        playerManager.reset();
    }

    public void showDialog(String message) {
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void hideDialog() {
        progressDialog.dismiss();
    }
}
