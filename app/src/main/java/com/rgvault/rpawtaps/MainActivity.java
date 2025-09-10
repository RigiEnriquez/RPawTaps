package com.rgvault.rpawtaps;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer bgMusic;
    public static AnimationDrawable sharedGradient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup shared animated background
        ConstraintLayout mainLayout = findViewById(R.id.mainLayout);
        sharedGradient = (AnimationDrawable) mainLayout.getBackground();
        sharedGradient.setEnterFadeDuration(4000);
        sharedGradient.setExitFadeDuration(4000);
        sharedGradient.start();

        // Setup BottomNavigation
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);

        // Load HomeFragment by default
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new HomeFragment())
                .commit();

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, new HomeFragment())
                        .commit();
                return true;
            } else if (id == R.id.nav_shop) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, new ShopFragment())
                        .commit();
                return true;
            }
            return false;
        });

        // Setup background music
        bgMusic = MediaPlayer.create(this, R.raw.bg_music);
        bgMusic.setLooping(true);
        bgMusic.setVolume(1.0f, 1.0f);
        bgMusic.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (bgMusic != null && bgMusic.isPlaying()) {
            bgMusic.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bgMusic != null && !bgMusic.isPlaying()) {
            bgMusic.start();
        }
    }

    @Override
    protected void onDestroy() {
        if (bgMusic != null) {
            bgMusic.release();
            bgMusic = null;
        }
        super.onDestroy();
    }
}