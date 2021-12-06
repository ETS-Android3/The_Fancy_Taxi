package com.example.TheFancyTaxi.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import com.example.TheFancyTaxi.Keys;
import com.example.TheFancyTaxi.db_sp.MSPV3;
import com.example.TheFancyTaxi.R;

public class MenuActivity extends AppCompatActivity {

    private RadioGroup radioModeGroup;
    private RadioButton menu_radioButton_easy_mode,menu_radioButton_hard_mode;
    private Switch menu_switch_sensor;
    private boolean hardMode,accelerometer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        findViews();
        getUserChoice();
    }


    @Override
    public void finish() {
        if(hardMode)
            setGameMode(1);
        else
            setGameMode(0);

        if(accelerometer)
            setControllerMode(1);
        else
            setControllerMode(0);

        super.finish();
    }




    private void configureButtons(){
        if(hardMode) {
            menu_radioButton_easy_mode.setChecked(false);
            menu_radioButton_hard_mode.setChecked(true);
        }
        else {
            menu_radioButton_easy_mode.setChecked(true);
            menu_radioButton_hard_mode.setChecked(false);

        }

    }

    private void setGameMode(int choice){
        MSPV3 sp = MSPV3.getMe();
        sp.putInt(Keys.GAME_MODE,choice);
    }

    private void setControllerMode(int choice){
        MSPV3 sp = MSPV3.getMe();
        sp.putInt(Keys.SENSOR_MODE,choice);
    }

    private void getUserChoice(){
        hardMode = false;
        radioModeGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            switch(i) {
                case R.id.menu_radioButton_easy_mode:
                   hardMode=false;
                   menu_radioButton_easy_mode.setChecked(true);
                    break;
                case R.id.menu_radioButton_hard_mode:
                  hardMode=true;
                  menu_radioButton_hard_mode.setChecked(true);
                    break;
            }
        });


        accelerometer=false;

    menu_switch_sensor.setOnCheckedChangeListener((buttonView, isChecked) -> {
        if(isChecked) {
            accelerometer = true;
            menu_switch_sensor.setChecked(true);
        }
        else{
            menu_switch_sensor.setChecked(false);
             accelerometer=false;}
    });


    }


    private void findViews() {

        radioModeGroup = findViewById(R.id.radioModeGroup);
        menu_radioButton_easy_mode = findViewById(R.id.menu_radioButton_easy_mode);
        menu_radioButton_hard_mode = findViewById(R.id.menu_radioButton_hard_mode);
        menu_switch_sensor = findViewById(R.id.menu_switch_sensor);
    }
}