package com.example.secondgame.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.secondgame.R;

public class StartMenuActivity extends AppCompatActivity {

    private Button startGame_BTN;

    private Button top10_BTN;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startmenu);


        findViews();
        initViews();
        getLocation();
    }


    private void initViews() {
        startGame_BTN.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(StartMenuActivity.this, ChooseMenuActivity.class));

        });

        top10_BTN.setOnClickListener(view -> {
            changeActivity();
        });


    }

    private void findViews() {
        startGame_BTN = findViewById(R.id.startMenu_BTN_startGame);
        top10_BTN = findViewById(R.id.startMenu_BTN_top10);

    }

    private void changeActivity() {

        finish();
        Intent intent = new Intent(StartMenuActivity.this, RecordActivity.class);
        startActivity(intent);
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                StartMenuActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                StartMenuActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }
    }
}
