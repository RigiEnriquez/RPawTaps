package com.rgvault.rpawtaps;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ShopFragment extends Fragment {

    private TextView coinsDisplay;
    private Button doubleCoinsBtn, autoTapBtn;
    private CoinManager coinManager;
    private CountDownTimer doubleTimer, autoTapTimer;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        coinsDisplay = view.findViewById(R.id.shopCoins);
        doubleCoinsBtn = view.findViewById(R.id.btnDoubleCoins);
        autoTapBtn = view.findViewById(R.id.btnAutoTap);

        coinManager = new CoinManager(requireContext());

        coinsDisplay.setText("Coins: " + coinManager.getCoins());

        // Double Coins Button
        if (coinManager.isDoubleActive()) disableDoubleButton();
        doubleCoinsBtn.setOnClickListener(v -> {
            if (coinManager.buyDoubleCoins()) {
                coinsDisplay.setText("Coins: " + coinManager.getCoins());
                Toast.makeText(getContext(), "Double Coins Activated for 5 minutes!", Toast.LENGTH_SHORT).show();
                disableDoubleButton();
            } else {
                Toast.makeText(getContext(), "Not enough PawCoins!", Toast.LENGTH_SHORT).show();
            }
        });

        // Auto Tap Button
        if (coinManager.isAutoTapActive()) disableAutoTapButton();
        autoTapBtn.setOnClickListener(v -> {
            if (coinManager.buyAutoTap()) {
                coinsDisplay.setText("Coins: " + coinManager.getCoins());
                Toast.makeText(getContext(), "Auto Tap Activated for 30s!", Toast.LENGTH_SHORT).show();
                disableAutoTapButton();
            } else {
                Toast.makeText(getContext(), "Not enough PawCoins!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void disableDoubleButton() {
        if (doubleTimer != null) doubleTimer.cancel();

        doubleCoinsBtn.setEnabled(false);
        long remaining = coinManager.getDoubleCoinsRemainingTime();

        doubleTimer = new CountDownTimer(remaining, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                doubleCoinsBtn.setText("Active (" + minutes + "m " + seconds + "s)");
            }

            @Override
            public void onFinish() {
                doubleCoinsBtn.setEnabled(true);
                doubleCoinsBtn.setText("Buy Double PawCoins (" + CoinManager.DOUBLE_COINS_COST + ")");
            }
        }.start();
    }

    @SuppressLint("SetTextI18n")
    private void disableAutoTapButton() {
        if (autoTapTimer != null) autoTapTimer.cancel();

        autoTapBtn.setEnabled(false);
        long remaining = coinManager.getAutoTapRemainingTime();

        autoTapTimer = new CountDownTimer(remaining, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                autoTapBtn.setText("Active (" + seconds + "s)");
            }

            @Override
            public void onFinish() {
                autoTapBtn.setEnabled(true);
                autoTapBtn.setText("Buy Auto Tap (" + CoinManager.AUTO_TAP_COST + ")");
            }
        }.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (doubleTimer != null) doubleTimer.cancel();
        if (autoTapTimer != null) autoTapTimer.cancel();
    }
}