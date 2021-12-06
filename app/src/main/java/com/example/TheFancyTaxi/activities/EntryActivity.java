package com.example.TheFancyTaxi.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import com.example.TheFancyTaxi.Keys;
import com.example.TheFancyTaxi.R;
import com.google.android.material.button.MaterialButton;

public class EntryActivity extends AppCompatActivity {

    private MaterialButton start_game_button;
    private MaterialButton preferences_button;
    private MaterialButton leaders_board_button;
    private Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestLocPermissions();

        if (savedInstanceState != null) {
            this.b = savedInstanceState;
        } else {
            this.b = new Bundle();
        }
        setContentView
                (R.layout.activity_entry);
        findViews();
        initButtons();


    }

    private void requestLocPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 44);
    }

    private void initButtons() {
        preferences_button.setOnClickListener(v -> runActivity(MenuActivity.class));
        start_game_button.setOnClickListener(v -> runActivity(GameActivity.class));
        leaders_board_button.setOnClickListener(v -> runActivity(LeadersBoardActivity.class));
    }


    private void runActivity(Class activity) {
        Intent myIntent = new Intent(this, activity);
        myIntent.putExtra(Keys.BUNDLE_IDENTIFIER, b);
        startActivity(myIntent);
    }


    private void findViews() {
        start_game_button = findViewById(R.id.start_game_button);
        preferences_button = findViewById(R.id.preferences_button);
        leaders_board_button = findViewById(R.id.leaders_board_button);

    }
}