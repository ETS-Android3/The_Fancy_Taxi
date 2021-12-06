package com.example.TheFancyTaxi.activities;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
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
import com.example.TheFancyTaxi.Keys;
import com.example.TheFancyTaxi.db_sp.MSPV3;
import com.example.TheFancyTaxi.R;

public class GameActivity extends AppCompatActivity {


    private final int MAX_LIVES = 3,SCORE_GROWTH_FACTOR= 500 ,DELAY_DECREASE_FACTOR = 10 ,DELAY_PIVOT_FACTOR = 200 ,DELAY_HARD_MODE_FACTOR = 500;
    private final int ROWS = 5,COLS = 5,CONTACT_ROW = 4,BORN_ROW=0;
    private final int HIDE = 0,SHOW = 1,COIN = 2;

    private final int LEFT_POS = 0 , LEFT_CENTER_POS = 1 , CENTER_POS = 2 , RIGHT_CENTER_POS = 3 , RIGHT_POS = 4;
    private final int LEFT_END = 10 , LEFT_CENTER_END = 6 , LEFT_CENTER = 2 , RIGHT_CENTER = -2 , RIGHT_CENTER_END = -6 , RIGHT_END =-10;

    private final String CRASH_TOAST = "CRASHED";

    private ImageView[][] panel_IMG_obstacles,panel_IMG_coins;
    private ImageView[] panel_IMG_taxiCars,panel_IMG_explosions,panel_IMG_hearts;
    private ImageView img_tilt_right,img_tilt_left;
    private TextView panel_LBL_score;
    private Button panel_BTN_right,panel_BTN_left;

    private boolean directions[] = {false,false,false,false,false};
    private boolean collision,coinPick,sensor,gameOverFlag=false;
    private int clock,coinCounter,startPos, pos,delay = 1000,score = 0,lives = MAX_LIVES;

    private SensorManager sensorManager;
    private Sensor accSensor;
    private SensorEventListener accSensorEventListener;
    private MediaPlayer sound;
    private Bundle b;

  private int[][] matrix=new int[][]{
            {0,0,0,0,0},
            {0,0,0,0,0},
            {0,0,0,0,0},
            {0,0,0,0,0},
            {0,0,0,0,0}};


    final Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (clock % 2 == 0) {
                startPos = (getRandomObstacles() - 1);
                if(coinCounter % 5 == 0) {
                    matrix[BORN_ROW][startPos] = COIN;
                }
                else{
                    matrix[BORN_ROW][startPos] = SHOW;
               }
            }
            if(!gameOverFlag)
                runGame();

            panel_LBL_score.setText("" + score);
            adjustDynamicGameSpeed();
            handler.postDelayed(runnable, delay);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        this.b = new Bundle();
        findViews();
        adjustToPreferences();
        initGame();

    }

    private void initSensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        directions[CENTER_POS]=true;
        initSensorView();

         accSensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];


                if (panel_IMG_taxiCars[RIGHT_CENTER_POS].getVisibility() == View.VISIBLE&& panel_IMG_obstacles[CONTACT_ROW][RIGHT_POS].getVisibility() == View.INVISIBLE
                        && x<RIGHT_CENTER_END && x>RIGHT_END) {
                    setOnRightButtonVisibility(RIGHT_CENTER_POS);
                    setDirections(RIGHT_POS);
                }


                if (panel_IMG_taxiCars[CENTER_POS].getVisibility() == View.VISIBLE&& panel_IMG_obstacles[CONTACT_ROW][RIGHT_CENTER_POS].getVisibility() == View.INVISIBLE
                        && x<RIGHT_CENTER && x>RIGHT_CENTER_END) {
                    setOnRightButtonVisibility(CENTER_POS);
                    setDirections(RIGHT_CENTER_POS);
                }


                if (panel_IMG_taxiCars[LEFT_CENTER_POS].getVisibility() == View.VISIBLE&& panel_IMG_obstacles[CONTACT_ROW][CENTER_POS].getVisibility() == View.INVISIBLE
                        && x<LEFT_CENTER && x> RIGHT_CENTER) {
                    setOnRightButtonVisibility(LEFT_CENTER_POS);
                    setDirections(CENTER_POS);
                }


                if (panel_IMG_taxiCars[LEFT_POS].getVisibility() == View.VISIBLE&& panel_IMG_obstacles[CONTACT_ROW][LEFT_CENTER_POS].getVisibility() == View.INVISIBLE
                        && x< LEFT_CENTER_END && x> LEFT_CENTER) {
                    setOnRightButtonVisibility(LEFT_POS);
                    setDirections(LEFT_CENTER_POS);

                }


                if (panel_IMG_taxiCars[LEFT_CENTER_POS].getVisibility() == View.VISIBLE && panel_IMG_obstacles[CONTACT_ROW][LEFT_POS].getVisibility() == View.INVISIBLE
                        && x< LEFT_END && x> LEFT_CENTER_END) {
                    setOnLeftButtonVisibility(LEFT_CENTER_POS);
                    setDirections(LEFT_POS);

                }

                if (panel_IMG_taxiCars[CENTER_POS].getVisibility() == View.VISIBLE&& panel_IMG_obstacles[CONTACT_ROW][LEFT_CENTER_POS].getVisibility() == View.INVISIBLE
                        && x<LEFT_CENTER_END && x> LEFT_CENTER) {
                    setOnLeftButtonVisibility(CENTER_POS);
                    setDirections(LEFT_CENTER_POS);

                }

                if (panel_IMG_taxiCars[RIGHT_CENTER_POS].getVisibility() == View.VISIBLE && panel_IMG_obstacles[CONTACT_ROW][CENTER_POS].getVisibility() == View.INVISIBLE
                        && x< LEFT_CENTER && x> RIGHT_CENTER) {
                    setOnLeftButtonVisibility(RIGHT_CENTER_POS);
                    setDirections(CENTER_POS);
                }


                if (panel_IMG_taxiCars[RIGHT_POS].getVisibility() == View.VISIBLE && panel_IMG_obstacles[CONTACT_ROW][RIGHT_CENTER_POS].getVisibility() == View.INVISIBLE
                    && x< RIGHT_CENTER && x> RIGHT_CENTER_END) {

                    setOnLeftButtonVisibility(RIGHT_POS);
                    setDirections(RIGHT_CENTER_POS);
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    private void initButtons() {
        directions[CENTER_POS] = true;
        panel_BTN_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (panel_IMG_taxiCars[RIGHT_CENTER_POS].getVisibility() == View.VISIBLE&& panel_IMG_obstacles[CONTACT_ROW][RIGHT_POS].getVisibility() == View.INVISIBLE) {
                    setOnRightButtonVisibility(RIGHT_CENTER_POS);
                    setDirections(RIGHT_POS);

                }
                if (panel_IMG_taxiCars[CENTER_POS].getVisibility() == View.VISIBLE&& panel_IMG_obstacles[CONTACT_ROW][RIGHT_CENTER_POS].getVisibility() == View.INVISIBLE) {
                    setOnRightButtonVisibility(CENTER_POS);
                    setDirections(RIGHT_CENTER_POS);
                }

                if (panel_IMG_taxiCars[LEFT_CENTER_POS].getVisibility() == View.VISIBLE&& panel_IMG_obstacles[CONTACT_ROW][CENTER_POS].getVisibility() == View.INVISIBLE) {
                    setOnRightButtonVisibility(LEFT_CENTER_POS);
                    setDirections(CENTER_POS);
                }

                if (panel_IMG_taxiCars[LEFT_POS].getVisibility() == View.VISIBLE&& panel_IMG_obstacles[CONTACT_ROW][LEFT_CENTER_POS].getVisibility() == View.INVISIBLE) {
                    setOnRightButtonVisibility(LEFT_POS);
                    setDirections(LEFT_CENTER_POS);

                }

            }
        });



        panel_BTN_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (panel_IMG_taxiCars[LEFT_CENTER_POS].getVisibility() == View.VISIBLE && panel_IMG_obstacles[CONTACT_ROW][LEFT_POS].getVisibility() == View.INVISIBLE) {
                    setOnLeftButtonVisibility(LEFT_CENTER_POS);
                    setDirections(LEFT_POS);

                }
                if (panel_IMG_taxiCars[CENTER_POS].getVisibility() == View.VISIBLE&& panel_IMG_obstacles[CONTACT_ROW][LEFT_CENTER_POS].getVisibility() == View.INVISIBLE ) {
                    setOnLeftButtonVisibility(CENTER_POS);
                    setDirections(LEFT_CENTER_POS);

                }


                if (panel_IMG_taxiCars[RIGHT_CENTER_POS].getVisibility() == View.VISIBLE && panel_IMG_obstacles[CONTACT_ROW][CENTER_POS].getVisibility() == View.INVISIBLE) {
                    setOnLeftButtonVisibility(RIGHT_CENTER_POS);
                    setDirections(CENTER_POS);
                }

                if (panel_IMG_taxiCars[RIGHT_POS].getVisibility() == View.VISIBLE && panel_IMG_obstacles[CONTACT_ROW][RIGHT_CENTER_POS].getVisibility() == View.INVISIBLE) {
                    setOnLeftButtonVisibility(RIGHT_POS);
                    setDirections(RIGHT_CENTER_POS);
                }

            }
        });
    }

    private void initSensorView(){
        panel_BTN_right.setVisibility(View.INVISIBLE);
        panel_BTN_left.setVisibility(View.INVISIBLE);
        img_tilt_right.setVisibility(View.VISIBLE);
        img_tilt_left.setVisibility(View.VISIBLE);
    }

    private void adjustDynamicGameSpeed(){
        delay -= DELAY_DECREASE_FACTOR;
        if (delay == DELAY_PIVOT_FACTOR){
            delay = DELAY_HARD_MODE_FACTOR;
        }
    }
    private void setOnRightButtonVisibility( int position) {
        panel_IMG_taxiCars[position].setVisibility(View.INVISIBLE);
        panel_IMG_taxiCars[(position+1)].setVisibility(View.VISIBLE);
    }

    private void setOnLeftButtonVisibility( int position) {
        panel_IMG_taxiCars[position].setVisibility(View.INVISIBLE);
        panel_IMG_taxiCars[(position-1)].setVisibility(View.VISIBLE);
    }

    private void setDirections( int position) {

        for (int i = 0; i < directions.length; i++) {
            if(i == position){
                directions[i] = true;

            }
            else {
                directions[i] = false;
            }
        }

    }

    private void runGame() {
        setDefaultCollisions();
        for (int i = (ROWS -1); i >= 0; i--) {
            for (int j = (COLS -1); j >= 0; j--) {

                if (matrix[i][j] == HIDE && (panel_IMG_obstacles[i][j].getVisibility() == View.VISIBLE ||
                        panel_IMG_coins[i][j].getVisibility() == View.VISIBLE)) {
                    panel_IMG_obstacles[i][j].setVisibility(View.INVISIBLE);
                    panel_IMG_coins[i][j].setVisibility(View.INVISIBLE);
                }

                if (matrix[i][j] == SHOW) {
                    panel_IMG_obstacles[i][j].setVisibility(View.VISIBLE);
                    matrix[i][j] = HIDE;
                    if (i < COLS-1)
                        matrix[i + 1][j] = SHOW;
                    else {
                        pos = j;
                        collision = detectCollision(pos);
                        if (!collision)
                            score += SCORE_GROWTH_FACTOR;
                    }
                }

                if (matrix[i][j] == COIN) {
                    panel_IMG_coins[i][j].setVisibility(View.VISIBLE);
                    matrix[i][j] = HIDE;
                    if (i < COLS-1)
                        matrix[i + 1][j] = COIN;
                    else {
                        pos = j;
                        coinPick = detectCoinPick(pos);
                        if (coinPick)
                            score += (SCORE_GROWTH_FACTOR*2);

                    }
                }
            }
        }
        if (lives == 0) {
            gameOver();

        }

        updateLifeViews();
        clock++;
        coinCounter++;
    }

    private void adjustToPreferences() {
        MSPV3 sp = MSPV3.getMe();

        if(sp.getInt(Keys.SENSOR_MODE,1)==1){
            sensor =true;
            initSensor();
        }
        else{
            sensor =false;
            initButtons();
        }

        if(sp.getInt(Keys.GAME_MODE,1)==1){
            delay =500;
        }

    }

    private void gameOver(){

        gameOverFlag=true;
        Intent myIntent = new Intent(this, EndOfGameActivity.class);
        b.putInt(Keys.SCORE,score);
        myIntent.putExtra(Keys.BUNDLE_IDENTIFIER, b);
        startActivity(myIntent);
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensor)
            sensorManager.registerListener(accSensorEventListener, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensor)
            sensorManager.unregisterListener(accSensorEventListener);
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
        handler.postDelayed(runnable, delay);
    }

    private void stopTicker() {
        handler.removeCallbacks(runnable);
    }

    private void initGame() {
        for (int i = 0; i < (panel_IMG_taxiCars.length); i++) {
            if(i!=CENTER_POS)
                panel_IMG_taxiCars[i].setVisibility(View.INVISIBLE);

        }

        for (int i = 0; i < (panel_IMG_explosions.length); i++) {

            panel_IMG_explosions[i].setVisibility(View.INVISIBLE);

        }


        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                panel_IMG_obstacles[i][j].setVisibility(View.INVISIBLE);
                panel_IMG_coins[i][j].setVisibility(View.INVISIBLE);

           }
        }


    }

    private boolean detectCollision(int position) {
        if ((position == LEFT_POS && directions[LEFT_POS]) || (position == LEFT_CENTER_POS && directions[LEFT_CENTER_POS]) || (position == CENTER_POS  && directions[CENTER_POS]) ||
                (position == RIGHT_CENTER_POS  && directions[RIGHT_CENTER_POS]) || (position == RIGHT_POS && directions[RIGHT_POS])) {

            panel_IMG_explosions[position].setVisibility(View.VISIBLE);
            lives--;
            showCollisionToast();
            vibrate();
            return true;

        }

        return false;
    }

    private boolean detectCoinPick(int position) {

        if ((position == LEFT_POS && directions[LEFT_POS]) || (position == LEFT_CENTER_POS && directions[LEFT_CENTER_POS]) || (position == CENTER_POS  && directions[CENTER_POS]) ||
                (position == RIGHT_CENTER_POS  && directions[RIGHT_CENTER_POS]) || (position == RIGHT_POS && directions[RIGHT_POS])) {
                panel_IMG_coins[CONTACT_ROW][position].getLayoutParams().height =30;
                 playCoinSound();
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
        return (int)((Math.random() * (COLS)) + 1);

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
        panel_IMG_explosions[LEFT_POS].setVisibility(View.INVISIBLE);
        panel_IMG_explosions[LEFT_CENTER_POS].setVisibility(View.INVISIBLE);
        panel_IMG_explosions[CENTER_POS].setVisibility(View.INVISIBLE);
        panel_IMG_explosions[RIGHT_CENTER_POS].setVisibility(View.INVISIBLE);
        panel_IMG_explosions[RIGHT_POS].setVisibility(View.INVISIBLE);
    }

    private void showCollisionToast() {
        sound = MediaPlayer.create(this,R.raw.car_crash);
        sound.start();
        Toast.makeText(this, CRASH_TOAST, Toast.LENGTH_SHORT).show();
    }

    private void playCoinSound() {
        sound = MediaPlayer.create(this,R.raw.coin_collect);
        sound.start();
    }

    private void findViews() {
        panel_BTN_right = findViewById(R.id.panel_BTN_right);
        panel_BTN_left = findViewById(R.id.panel_BTN_left);
        panel_LBL_score = findViewById(R.id.panel_LBL_score);
        img_tilt_right = findViewById(R.id.img_tilt_right);
        img_tilt_left = findViewById(R.id.img_tilt_left);

        panel_IMG_obstacles = new ImageView[][] {
                {
                        findViewById(R.id.img_obstacle00),
                        findViewById(R.id.img_obstacle01),
                        findViewById(R.id.img_obstacle02),
                        findViewById(R.id.img_obstacle03),
                        findViewById(R.id.img_obstacle04)
                }, {
                findViewById(R.id.img_obstacle10),
                findViewById(R.id.img_obstacle11),
                findViewById(R.id.img_obstacle12),
                findViewById(R.id.img_obstacle13),
                findViewById(R.id.img_obstacle14)
        }, {
                findViewById(R.id.img_obstacle20),
                findViewById(R.id.img_obstacle21),
                findViewById(R.id.img_obstacle22),
                findViewById(R.id.img_obstacle23),
                findViewById(R.id.img_obstacle24)
        }, {
                findViewById(R.id.img_obstacle30),
                findViewById(R.id.img_obstacle31),
                findViewById(R.id.img_obstacle32),
                findViewById(R.id.img_obstacle33),
                findViewById(R.id.img_obstacle34)
        }, {
                findViewById(R.id.img_obstacle40),
                findViewById(R.id.img_obstacle41),
                findViewById(R.id.img_obstacle42),
                findViewById(R.id.img_obstacle43),
                findViewById(R.id.img_obstacle44)
        }




        };

       panel_IMG_coins= new ImageView[][] {
                {
                        findViewById(R.id.img_coin00),
                        findViewById(R.id.img_coin01),
                        findViewById(R.id.img_coin02),
                        findViewById(R.id.img_coin03),
                        findViewById(R.id.img_coin04)
                }, {
                findViewById(R.id.img_coin10),
                findViewById(R.id.img_coin11),
                findViewById(R.id.img_coin12),
                findViewById(R.id.img_coin13),
                findViewById(R.id.img_coin14)
        }, {
                findViewById(R.id.img_coin20),
                findViewById(R.id.img_coin21),
                findViewById(R.id.img_coin22),
                findViewById(R.id.img_coin23),
                findViewById(R.id.img_coin24)
        }, {
                findViewById(R.id.img_coin30),
                findViewById(R.id.img_coin31),
                findViewById(R.id.img_coin32),
                findViewById(R.id.img_coin33),
                findViewById(R.id.img_coin34)
        }, {
                findViewById(R.id.img_coin40),
                findViewById(R.id.img_coin41),
                findViewById(R.id.img_coin42),
                findViewById(R.id.img_coin43),
                findViewById(R.id.img_coin44)
        }

        };


        panel_IMG_taxiCars = new ImageView[] {
                findViewById(R.id.img_taxi0),
                findViewById(R.id.img_taxi1),
                findViewById(R.id.img_taxi2),
                findViewById(R.id.img_taxi3),
                findViewById(R.id.img_taxi4)

        };
        panel_IMG_explosions = new ImageView[] {
                findViewById(R.id.img_boom0),
                findViewById(R.id.img_boom1),
                findViewById(R.id.img_boom2),
                findViewById(R.id.img_boom3),
                findViewById(R.id.img_boom4),


        };

        panel_IMG_hearts = new ImageView[] {
                findViewById(R.id.img_heart1),
                findViewById(R.id.img_heart2),
                findViewById(R.id.img_heart3),

        };
    }
}