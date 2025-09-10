package com.rgvault.rpawtaps;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
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

    private TextView number_coins;
    private ImageView pngCat;
    private SoundPool touchMeowSound;
    private int meowSoundId;
    private Animation touchAnimation;
    private CoinManager coinManager;

    private Handler autoTapHandler = new Handler();
    private Runnable autoTapRunnable = new Runnable() {
        @Override
        public void run() {
            if (coinManager.isAutoTapActive()) {
                coinManager.addCoins(1); // Auto Tap gives 1 coin/sec
                number_coins.setText(String.valueOf(coinManager.getCoins()));
                autoTapHandler.postDelayed(this, 1000);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        number_coins = view.findViewById(R.id.PawCoins);
        pngCat = view.findViewById(R.id.pngCat);

        coinManager = new CoinManager(requireContext());

        // Display coins
        number_coins.setText(String.valueOf(coinManager.getCoins()));

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

        // Animation
        touchAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);

        // Cat tap logic
        pngCat.setOnClickListener(v -> {
            int baseCoins = 1;

            // If Mega Tap or other future upgrades, adjust here
            int coinsEarned = coinManager.addCoins(baseCoins);
            number_coins.setText(String.valueOf(coinManager.getCoins()));

            touchMeowSound.play(meowSoundId, 1, 1, 0, 0, 1);
            pngCat.startAnimation(touchAnimation);
        });

        // Tutorial on first launch
        boolean isFirstLaunch = requireActivity()
                .getSharedPreferences("PawCoinsPrefs", Context.MODE_PRIVATE)
                .getBoolean("first_launch", true);
        if (isFirstLaunch) {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Welcome to RPawTaps")
                    .setMessage("Tap the cat to earn PawCoins!\n\nEach tap = 1 PawCoin.")
                    .setPositiveButton("Got it!", null)
                    .show();
            requireActivity().getSharedPreferences("PawCoinsPrefs", Context.MODE_PRIVATE)
                    .edit().putBoolean("first_launch", false).apply();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (coinManager.isAutoTapActive()) {
            autoTapHandler.post(autoTapRunnable);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        autoTapHandler.removeCallbacks(autoTapRunnable);
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