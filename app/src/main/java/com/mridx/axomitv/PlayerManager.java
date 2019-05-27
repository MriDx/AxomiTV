package com.mridx.axomitv;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.ads.AdsMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;

import static android.widget.Toast.*;

public class PlayerManager {

    private DataSource.Factory dataSourceFactory;

    private SimpleExoPlayer player;

    private long contentPosition;

    public PlayerManager(Context context) {
        dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, context.getString(R.string.app_name)));
    }


    public void init(Context context, PlayerView playerView, String contentUri) {

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        player = ExoPlayerFactory.newSimpleInstance(context);
        playerView.setPlayer(player);

        //String contentUrl = "http://vidnetcdn.vidgyor.com/assamtalks-origin/liveabr/assamtalks-origin/live1/chunks.m3u8";
        MediaSource contentMediaSource = buildMediaSource(Uri.parse(contentUri));

        player.seekTo(contentPosition);
        player.prepare(contentMediaSource);

        player.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Log.d("player", "Player state changed");
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.d("player", "Player error");
            }

            @Override
            public void onSeekProcessed() {
                Log.d("player", "Player ready");
                progressDialog.dismiss();
            }
        });

        player.setPlayWhenReady(true);

    }

    private MediaSource buildMediaSource(Uri uri) {

        int type = Util.inferContentType(uri);
        switch (type) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            case C.TYPE_OTHER:
                return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
                default:
                    throw new IllegalStateException("Unsupported type: " +type);
        }
    }

    public void reset() {
        if (player != null) {
            contentPosition = player.getContentPosition();
            player.release();
            player = null;
        }
    }

    public void release() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

}
