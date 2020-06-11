package com.franco.rolltheballgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.franco.rolltheballgame.service.MusicService;

public class MenuActivity extends AppCompatActivity {

    MusicService musicService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        // this.startSong();
    }

    private void startSong() {
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
    }

    public void startGame(View view) {
        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
        this.startActivity(intent);
    }
}