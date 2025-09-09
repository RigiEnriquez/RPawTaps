package com.rgvault.rpawtaps;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;

public class HomeFragment extends Fragment {

    private int coins = 0;
    private TextView number_coins;
    private ImageView pngCat;
    private SoundPool touchMeowSound;
    private int meowSoundId;
    private Animation touchAnimation;
    private SharedPreferences sharedPreferences;

    private static final String PREFS_NAME = "PawCoinsPrefs";
    private static final String COINS_KEY = "coins";
    private static final String FIRST_LAUNCH_KEY = "first_launch";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        number_coins = view.findViewById(R.id.PawCoins);
        pngCat = view.findViewById(R.id.pngCat);

        sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        coins = sharedPreferences.getInt(COINS_KEY, 0);
        number_coins.setText(String.valueOf(coins));

        // ✅ Show tutorial if first launch
        boolean isFirstLaunch = sharedPreferences.getBoolean(FIRST_LAUNCH_KEY, true);
        if (isFirstLaunch) {
            showTutorialDialog();
            sharedPreferences.edit().putBoolean(FIRST_LAUNCH_KEY, false).apply();
        }

        // Sound setup
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        touchMeowSound = new SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(audioAttributes)
                .build();

        meowSoundId = touchMeowSound.load(getContext(), R.raw.meow_sound, 1);

        // Animation setup
        touchAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);

        // Tap logic
        pngCat.setOnClickListener(v -> {
            int coinsEarned = sharedPreferences.getBoolean("double_coins", false) ? 2 : 1;
            coins += coinsEarned;
            number_coins.setText(String.valueOf(coins));
            sharedPreferences.edit().putInt(COINS_KEY, coins).apply();

            // Play sound and animation
            touchMeowSound.play(meowSoundId, 1, 1, 0, 0, 1);
            pngCat.startAnimation(touchAnimation);
        });

        return view;
    }

    private void showTutorialDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Welcome to RPawTaps")
                .setMessage("Tap the cat to earn PawCoins!\n\nEach tap = 1 PawCoin. Collect as many as you can!")
                .setPositiveButton("Got it!", null)
                .show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (touchMeowSound != null) {
            touchMeowSound.release();
            touchMeowSound = null;
        }
    }
}
