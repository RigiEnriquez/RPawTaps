package com.rgvault.rpawtaps;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

    private int coins;
    private SharedPreferences sharedPreferences;
    private TextView coinsDisplay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        sharedPreferences = getActivity().getSharedPreferences("PawCoinsPrefs", Context.MODE_PRIVATE);
        coins = sharedPreferences.getInt("coins", 0);

        coinsDisplay = view.findViewById(R.id.shopCoins);
        coinsDisplay.setText("Coins: " + coins);

        Button doubleCoinsBtn = view.findViewById(R.id.btnDoubleCoins);
        boolean doubleCoinsPurchased = sharedPreferences.getBoolean("double_coins", false);
        if(doubleCoinsPurchased){
            doubleCoinsBtn.setText("Purchased");
            doubleCoinsBtn.setEnabled(false);
        }

        doubleCoinsBtn.setOnClickListener(v -> {
            if(coins >= 50){
                coins -= 50;
                sharedPreferences.edit()
                        .putInt("coins", coins)
                        .putBoolean("double_coins", true)
                        .apply();
                coinsDisplay.setText("Coins: " + coins);
                doubleCoinsBtn.setText("Purchased");
                doubleCoinsBtn.setEnabled(false);
                Toast.makeText(getContext(), "Double Coins Purchased!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Not enough PawCoins!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
