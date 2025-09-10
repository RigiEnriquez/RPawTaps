package com.rgvault.rpawtaps;

import android.content.Context;
import android.content.SharedPreferences;

public class CoinManager {

    private static final String PREFS = "PawCoinsPrefs";

    // Costs and durations
    public static final int DOUBLE_COINS_COST = 150;
    public static final long DOUBLE_DURATION = 5 * 60 * 1000; // 5 minutes

    public static final int AUTO_TAP_COST = 200;
    public static final long AUTO_TAP_DURATION = 30 * 1000; // 30 seconds

    private SharedPreferences prefs;

    public CoinManager(Context context) {
        prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    // Coins
    public int getCoins() {
        return prefs.getInt("coins", 0);
    }

    public void setCoins(int coins) {
        prefs.edit().putInt("coins", coins).apply();
    }

    // Add coins (handles Double Coins)
    public int addCoins(int baseAmount) {
        int finalAmount = isDoubleActive() ? baseAmount * 2 : baseAmount;
        int coins = getCoins() + finalAmount;
        setCoins(coins);
        return finalAmount;
    }

    // Double Coins
    public boolean isDoubleActive() {
        long time = prefs.getLong("double_coins_time", 0);
        return time > 0 && (System.currentTimeMillis() - time < DOUBLE_DURATION);
    }

    public boolean buyDoubleCoins() {
        int coins = getCoins();
        if (coins >= DOUBLE_COINS_COST) {
            coins -= DOUBLE_COINS_COST;
            setCoins(coins);
            prefs.edit().putLong("double_coins_time", System.currentTimeMillis()).apply();
            return true;
        }
        return false;
    }

    public long getDoubleCoinsRemainingTime() {
        long time = prefs.getLong("double_coins_time", 0);
        long elapsed = System.currentTimeMillis() - time;
        long remaining = DOUBLE_DURATION - elapsed;
        return remaining > 0 ? remaining : 0;
    }

    // Auto Tap
    public boolean isAutoTapActive() {
        long time = prefs.getLong("auto_tap_time", 0);
        return time > 0 && (System.currentTimeMillis() - time < AUTO_TAP_DURATION);
    }

    public boolean buyAutoTap() {
        int coins = getCoins();
        if (coins >= AUTO_TAP_COST) {
            coins -= AUTO_TAP_COST;
            setCoins(coins);
            prefs.edit().putLong("auto_tap_time", System.currentTimeMillis()).apply();
            return true;
        }
        return false;
    }

    public long getAutoTapRemainingTime() {
        long time = prefs.getLong("auto_tap_time", 0);
        long elapsed = System.currentTimeMillis() - time;
        long remaining = AUTO_TAP_DURATION - elapsed;
        return remaining > 0 ? remaining : 0;
    }
}