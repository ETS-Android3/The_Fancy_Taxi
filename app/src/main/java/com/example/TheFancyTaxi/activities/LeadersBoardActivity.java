package com.example.TheFancyTaxi.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.TheFancyTaxi.Keys;
import com.example.TheFancyTaxi.db_sp.GPS;
import com.example.TheFancyTaxi.fragments.Fragment_Map;
import com.example.TheFancyTaxi.R;
import com.example.TheFancyTaxi.callback_interfaces.CallBack_List;
import com.example.TheFancyTaxi.db_sp.GameDB;
import com.example.TheFancyTaxi.db_sp.GameDBManager;
import com.example.TheFancyTaxi.db_sp.Record;
import com.example.TheFancyTaxi.fragments.Fragment_List;

public class LeadersBoardActivity extends AppCompatActivity {
    private Bundle b;
    private GameDB gameDB;
    private Fragment_List fragmentList;
    private Fragment_Map fragmentMap;


    CallBack_List cbList = (i -> {
        Record record       = gameDB.getRecords().get(i);
        String playerName   = record.getName();
        double lat          = record.getLat();
        double lon          = record.getLon();
        String score = ""+record.getScore();
        fragmentMap.setLocation(playerName, score, lat, lon);


    } );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaders_board);
        this.b = getIntent().getBundleExtra(Keys.BUNDLE_IDENTIFIER);

        fragmentList = new Fragment_List();
        fragmentList.setActivity(this);
        fragmentList.setCallBackList(cbList);
        updateDB();

        fragmentMap = new Fragment_Map();
        fragmentMap.setActivity(this);
        getSupportFragmentManager().beginTransaction().add(R.id.frame2, fragmentMap).commit();

    }

    private void updateDB() {
        String playerName=b.getString(Keys.PLAYER_NAME);
        int  score=b.getInt(Keys.SCORE);

        gameDB = GameDBManager.getRecordDBFromSP();

        GPS.getMe().getLocation((lat, lon) -> {
            updateLastRecord(lat, lon, score,playerName );
        });

    }

   private void updateLastRecord(double lat, double lon,int score,String playerName){

       Record r = new Record();
       r.setName(playerName);
       r.setScore(score);
       r.setLat(lat);
       r.setLon(lon);
       GameDBManager.updateRecord(gameDB,r);
       updateList();

   }

   private void updateList(){
       fragmentList.setRecords(gameDB.getRecords());
       getSupportFragmentManager().beginTransaction().add(R.id.frame1, fragmentList).commit();
   }

    @Override
    protected void onStart() {
        super.onStart();
    }

}