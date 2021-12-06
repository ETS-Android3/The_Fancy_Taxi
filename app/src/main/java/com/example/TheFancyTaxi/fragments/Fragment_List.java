package com.example.TheFancyTaxi.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.TheFancyTaxi.Keys;
import com.example.TheFancyTaxi.R;
import com.example.TheFancyTaxi.callback_interfaces.CallBack_List;
import com.example.TheFancyTaxi.db_sp.Record;

import java.util.ArrayList;

public class Fragment_List extends Fragment {

    private AppCompatActivity activity;
    private CallBack_List callBackList;
    private ArrayList<Record> records;
    private LinearLayout[]  list_rows ;
    private TextView[] list_players,list_scores;

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }
    public void setCallBackList(CallBack_List callBackList) {
        this.callBackList = callBackList;
    }
    public void setRecords(ArrayList<Record> records) {
        this.records = records;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);
        initViews();
        updateListView();


        return view;
    }

    private void initViews() {
        for (int i = 0; i < Keys.MAX_RECORDS; i++) {
            list_rows[i].setOnClickListener(setOnMap(i));
        }
    }

    private View.OnClickListener setOnMap(int index) {
        return view -> {
            callBackList.func(index);
        };
    }


    private void updateListView() {

        int i = 0;
        for (Record record: this.records) {
            list_players[i].setText(record.getName());
            list_scores[i].setText("  "+record.getScore());
            list_rows[i].setVisibility(View.VISIBLE);
            i++;
        }
    }

    private void findViews(View view) {
        list_players = new TextView[]{
                view.findViewById(R.id.list_player_player0),
                view.findViewById(R.id.list_player_player1),
                view.findViewById(R.id.list_player_player2),
                view.findViewById(R.id.list_player_player3),
                view.findViewById(R.id.list_player_player4),
                view.findViewById(R.id.list_player_player5),
                view.findViewById(R.id.list_player_player6),
                view.findViewById(R.id.list_player_player7),
                view.findViewById(R.id.list_player_player8),
                view.findViewById(R.id.list_player_player9),
        };
        list_scores = new TextView[]{
                view.findViewById(R.id.list_score_score0),
                view.findViewById(R.id.list_score_score1),
                view.findViewById(R.id.list_score_score2),
                view.findViewById(R.id.list_score_score3),
                view.findViewById(R.id.list_score_score4),
                view.findViewById(R.id.list_score_score5),
                view.findViewById(R.id.list_score_score6),
                view.findViewById(R.id.list_score_score7),
                view.findViewById(R.id.list_score_score8),
                view.findViewById(R.id.list_score_score9),
        };
        list_rows = new LinearLayout[]{
                view.findViewById(R.id.list_player_row0),
                view.findViewById(R.id.list_player_row1),
                view.findViewById(R.id.list_player_row2),
                view.findViewById(R.id.list_player_row3),
                view.findViewById(R.id.list_player_row4),
                view.findViewById(R.id.list_player_row5),
                view.findViewById(R.id.list_player_row6),
                view.findViewById(R.id.list_player_row7),
                view.findViewById(R.id.list_player_row8),
                view.findViewById(R.id.list_player_row9),
        };
    }

}