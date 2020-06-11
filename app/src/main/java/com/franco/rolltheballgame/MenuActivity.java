package com.franco.rolltheballgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void startGame(View view) {
        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
        this.startActivity(intent);
    }
}