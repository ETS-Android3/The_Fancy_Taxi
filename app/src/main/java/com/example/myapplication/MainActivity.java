package com.example.myapplication;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private final int DELAY = 1000;
    private final int MAX_LIVES = 3;
    private final int ROWS = 4;
    private final int COLS = 3;

    private ImageView[][] panel_IMG_obstacles;
    private ImageView[] panel_IMG_taxiCars;
    private ImageView[] panel_IMG_explosions;
    private ImageView[] panel_IMG_hearts;
    private Button panel_BTN_right;
    private Button panel_BTN_left;
    private TextView panel_LBL_score;

    private boolean right, left, center, collision;
    private int startPos, pos;
    private int clock;

    private int score = 0;
    private int lives = MAX_LIVES;
    int[][] matrix=new int[][]{
            {0,0,0},
            {0,0,0},
            {0,0,0},
            {0,0,0}};


    final Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (clock % 2 == 0) {
                startPos = (getRandomObstacles() - 1);
                matrix[0][startPos] = 1;
            }
            runGame();
            panel_LBL_score.setText("" + score);
            handler.postDelayed(runnable, DELAY);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initGame();
        initButtons();

    }

    @Override
    protected void onStart() {
        super.onStart();
        startTicker();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopTicker();
    }

    private void startTicker() {
        handler.postDelayed(runnable, DELAY);
    }

    private void stopTicker() {
        handler.removeCallbacks(runnable);
    }

    private void initButtons() {
        center = true;
        panel_BTN_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (panel_IMG_taxiCars[0].getVisibility() == View.VISIBLE && panel_IMG_obstacles[3][0].getVisibility() == View.INVISIBLE) {
                    panel_IMG_taxiCars[0].setVisibility(View.INVISIBLE);
                    panel_IMG_taxiCars[2].setVisibility(View.VISIBLE);
                    left = false;
                    center = false;
                    right = true;
                }
                if (panel_IMG_taxiCars[1].getVisibility() == View.VISIBLE && panel_IMG_obstacles[3][1].getVisibility() == View.INVISIBLE) {
                    panel_IMG_taxiCars[1].setVisibility(View.INVISIBLE);
                    panel_IMG_taxiCars[0].setVisibility(View.VISIBLE);
                    right = false;
                    center = true;
                    left = false;
                }
            }
        });
        panel_BTN_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (panel_IMG_taxiCars[0].getVisibility() == View.VISIBLE && panel_IMG_obstacles[3][2].getVisibility() == View.INVISIBLE) {
                    panel_IMG_taxiCars[0].setVisibility(View.INVISIBLE);
                    panel_IMG_taxiCars[1].setVisibility(View.VISIBLE);
                    left = true;
                    right = false;
                    center = false;
                }
                if (panel_IMG_taxiCars[2].getVisibility() == View.VISIBLE && panel_IMG_obstacles[3][1].getVisibility() == View.INVISIBLE) {
                    panel_IMG_taxiCars[2].setVisibility(View.INVISIBLE);
                    panel_IMG_taxiCars[0].setVisibility(View.VISIBLE);
                    left = false;
                    right = false;
                    center = true;
                }
            }
        });
    }

    private void initGame() {

        panel_IMG_taxiCars[1].setVisibility(View.INVISIBLE);
        panel_IMG_taxiCars[2].setVisibility(View.INVISIBLE);

        for (int i = 0; i < (panel_IMG_explosions.length); i++) {

            panel_IMG_explosions[i].setVisibility(View.INVISIBLE);

        }

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                panel_IMG_obstacles[i][j].setVisibility(View.INVISIBLE);
            }
        }


    }

    private void runGame() {
        setDefaultCollisions();
        for (int i = (ROWS - 1); i >= 0; i--) {
            for (int j = (COLS - 1); j >= 0; j--) {

                if (matrix[i][j] == 0 && panel_IMG_obstacles[i][j].getVisibility() == View.VISIBLE) {
                    panel_IMG_obstacles[i][j].setVisibility(View.INVISIBLE);
                }

                if (matrix[i][j] == 1) {
                    panel_IMG_obstacles[i][j].setVisibility(View.VISIBLE);
                    matrix[i][j] = 0;
                    if (i < COLS)
                        matrix[i + 1][j] = 1;
                    else {
                        pos = j + 1;
                        collision = detectCollision(pos);
                        if (!collision)
                            score += 500;
                    }
                }
            }
        }
        if (lives == 0) {
            setDefaultLifeAndScore();
        }

        updateLifeViews();
        clock++;
    }


    private boolean detectCollision(int pos) {

        if (pos == 2 && center) {
            panel_IMG_explosions[0].setVisibility(View.VISIBLE);
            lives--;
            showCollisionToast();
            vibrate();
            return true;

        }
        if (pos == 3 && left) {
            panel_IMG_explosions[1].setVisibility(View.VISIBLE);
            lives--;
            showCollisionToast();
            vibrate();
            return true;

        }
        if (pos == 1 && right) {
            panel_IMG_explosions[2].setVisibility(View.VISIBLE);
            lives--;
            showCollisionToast();

            vibrate();
            return true;

        }
        return false;
    }

    private void updateLifeViews() {

        for (int i = 0; i < (panel_IMG_hearts.length); i++) {
            if ((i) < lives) {
                panel_IMG_hearts[i].setVisibility(View.VISIBLE);
            } else {
                panel_IMG_hearts[i].setVisibility(View.INVISIBLE);
            }


        }
    }

    private int getRandomObstacles() {
        return (int)((Math.random() * (3)) + 1);

    }

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

    private void setDefaultCollisions() {
        panel_IMG_explosions[0].setVisibility(View.INVISIBLE);
        panel_IMG_explosions[1].setVisibility(View.INVISIBLE);
        panel_IMG_explosions[2].setVisibility(View.INVISIBLE);

    }
    private void setDefaultLifeAndScore() {
        lives = 3;
        score = 0;

    }
    private void showCollisionToast() {

        Toast.makeText(this, "CRUSHED!", Toast.LENGTH_SHORT).show();
    }

    private void findViews() {
        panel_BTN_right = findViewById(R.id.panel_BTN_right);
        panel_BTN_left = findViewById(R.id.panel_BTN_left);
        panel_LBL_score = findViewById(R.id.panel_LBL_score);

        panel_IMG_obstacles = new ImageView[][] {
                {
                        findViewById(R.id.img_obstacle00), findViewById(R.id.img_obstacle01),
                        findViewById(R.id.img_obstacle02)
                }, {
                findViewById(R.id.img_obstacle10),
                findViewById(R.id.img_obstacle11),
                findViewById(R.id.img_obstacle12)
        }, {
                findViewById(R.id.img_obstacle20),
                findViewById(R.id.img_obstacle21),
                findViewById(R.id.img_obstacle22)
        }, {
                findViewById(R.id.img_obstacle30),
                findViewById(R.id.img_obstacle31),
                findViewById(R.id.img_obstacle32)
        }

        };
        panel_IMG_taxiCars = new ImageView[] {
                findViewById(R.id.img_taxi),
                findViewById(R.id.img_taxi2),
                findViewById(R.id.img_taxi3),

        };
        panel_IMG_explosions = new ImageView[] {
                findViewById(R.id.img_boom),
                findViewById(R.id.img_boom2),
                findViewById(R.id.img_boom3),

        };

        panel_IMG_hearts = new ImageView[] {
                findViewById(R.id.img_heart1),
                findViewById(R.id.img_heart2),
                findViewById(R.id.img_heart3),

        };
    }
}