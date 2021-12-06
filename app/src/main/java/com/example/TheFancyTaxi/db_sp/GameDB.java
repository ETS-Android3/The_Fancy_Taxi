package com.example.TheFancyTaxi.db_sp;

import java.util.ArrayList;

public class GameDB {
    private ArrayList<Record> records = new ArrayList<>();

    public GameDB() {
    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public GameDB setRecords(ArrayList<Record> records) {
        this.records = records;
        return this;
    }

}
