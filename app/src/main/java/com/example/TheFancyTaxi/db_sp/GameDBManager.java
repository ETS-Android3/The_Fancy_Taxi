package com.example.TheFancyTaxi.db_sp;

import com.example.TheFancyTaxi.Keys;
import com.google.gson.Gson;

import java.util.ArrayList;

public class GameDBManager {


    public static void updateRecord(GameDB gameDB, Record record) {
        ArrayList<Record> records = gameDB.getRecords();
        records.add(record);
        records.sort((a, b) -> (int) (b.getScore() - a.getScore()));
        if(records.size() > Keys.MAX_RECORDS){
            records.remove(Keys.MAX_RECORDS);
        }
        saveRecordDBToSP(gameDB);
    }

    private static void saveRecordDBToSP(GameDB gameDB) {
        MSPV3.getMe().putString(Keys.LEADERBOARD, new Gson().toJson(gameDB));
    }

    public static GameDB getRecordDBFromSP() {
        GameDB gameDB = new Gson().fromJson(MSPV3.getMe().getString(Keys.LEADERBOARD, null), GameDB.class);
        if (gameDB == null){
            gameDB = new GameDB();
        }
        return gameDB;
    }
}
