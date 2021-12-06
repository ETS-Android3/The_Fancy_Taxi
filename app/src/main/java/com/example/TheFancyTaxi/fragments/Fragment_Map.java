package com.example.TheFancyTaxi.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.TheFancyTaxi.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class Fragment_Map extends Fragment {
    private MapView mv;
    private GoogleMap googleMap;
    private AppCompatActivity activity;

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mv = (MapView) view.findViewById(R.id.mv_view);
        mv.onCreate(savedInstanceState);
        mv.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mv.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap m) {
                googleMap = m;
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mv.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mv.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mv.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mv.onLowMemory();
    }

    public void setLocation(String player, String score, double lat, double lon) {
        googleMap.clear();
        LatLng location = new LatLng(lat, lon);
        score = "score: " + score;
        googleMap.addMarker(new MarkerOptions().position(location).title(player).snippet(score));

        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }
}