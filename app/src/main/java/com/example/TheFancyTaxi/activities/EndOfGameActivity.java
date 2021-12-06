package com.example.TheFancyTaxi.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.example.TheFancyTaxi.Keys;
import com.example.TheFancyTaxi.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class EndOfGameActivity extends AppCompatActivity {

    private TextInputLayout game_over_EDT_name;
    private MaterialButton game_over_BTN_refresh;
    private TextView game_over_score;
    private int score;
    private Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_of_game);

        this.b = getIntent().getBundleExtra(Keys.BUNDLE_IDENTIFIER);
        findViews();

        score =b.getInt(Keys.SCORE);
        updateScore();
        initRefreshButton();

    }

    private void updateScore(){

        game_over_score.setText("your score is : "+score);
    }



    private void initRefreshButton() {
        game_over_BTN_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String playerName = String.valueOf(game_over_EDT_name.getEditText().getText());
                forwardData(playerName);
                finish();

            }
        });
    }
    private void forwardData(String playerName) {
        Intent myIntent = new Intent(this,  LeadersBoardActivity.class);
        b.putString(Keys.PLAYER_NAME,playerName);
        b.putInt(Keys.SCORE,score);
        myIntent.putExtra(Keys.BUNDLE_IDENTIFIER, b);
        startActivity(myIntent);
        finish();
    }



    private void findViews() {

       game_over_EDT_name = findViewById(R.id.eog_EDT_name);
       game_over_BTN_refresh = findViewById(R.id.eog_BTN_refresh);
       game_over_score = findViewById(R.id.eog_IMG_game_over_score);

    }
}





