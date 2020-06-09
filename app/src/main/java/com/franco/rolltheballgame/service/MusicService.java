package com.franco.rolltheballgame.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.IBinder;

import com.franco.rolltheballgame.R;

import java.io.IOException;

public class MusicService extends Service {

    MediaPlayer mediaPlayer;

    public MusicService() { }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        this.mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            String filename = "android.resource://" + getPackageName() + "/raw/bgsong.mp3";
            mediaPlayer.setDataSource("https://freesound.org/data/previews/479/479859_10218985-lq.mp3");
            mediaPlayer.prepare();

            PlaybackParams p = new PlaybackParams();
            mediaPlayer.setLooping(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer.start();
        return super.onStartCommand(intent, flags, startId);
    }
}
