package com.rgvault.rpawtaps;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }
}
