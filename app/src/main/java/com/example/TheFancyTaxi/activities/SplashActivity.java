package com.example.TheFancyTaxi.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.TheFancyTaxi.R;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    final int ANIM_DURATION = 3500;
    private ImageView splash_IMG_logo,splash_IMG_smashed_screen;
    private MediaPlayer musicSound,smashSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        findViews();

        splash_IMG_logo.setVisibility(View.INVISIBLE);
        splash_IMG_smashed_screen.setVisibility(View.INVISIBLE);
        showViewSlideDown(splash_IMG_logo,splash_IMG_smashed_screen);
    }
    public void showViewSlideDown(final View v,final View v1) {
        v.setVisibility(View.VISIBLE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        v.setY(-height / 10);
        v.setScaleY(0.0f);
        v.setScaleX(0.0f);
        v.animate()
                .scaleY(1.0f)
                .scaleX(1.0f)
                .translationY(0)
                .setDuration(ANIM_DURATION)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        playMusicSoundAndToast();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        playSmashSound();
                        v1.setVisibility(View.VISIBLE);
                        animationDone();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }

    private void animationDone() {
        openHomeActivity();
    }

    private void openHomeActivity() {
        Intent intent = new Intent(this, EntryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void playMusicSoundAndToast() {
        musicSound = MediaPlayer.create(this,R.raw.splash_music);
        musicSound.start();
        Toast.makeText(this,"Loading...",Toast.LENGTH_LONG).show();
    }

    private void playSmashSound() {
        musicSound.setVolume(0.3f,0.3f);
        smashSound = MediaPlayer.create(this,R.raw.glass_break);
        smashSound.start();

    }
    private void findViews() {
        splash_IMG_logo = findViewById(R.id.splash_IMG_logo);
        splash_IMG_smashed_screen = findViewById(R.id.splash_IMG_smashed_screen);
    }





}