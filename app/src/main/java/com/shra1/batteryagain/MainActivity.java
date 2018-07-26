package com.shra1.batteryagain;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.shra1.batteryagain.customviews.ShraTextView;
import com.shra1.batteryagain.fragments.DataDetailsFragment;
import com.shra1.batteryagain.fragments.HomeFragment;
import com.shra1.batteryagain.fragments.MapFragment;
import com.shra1.batteryagain.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {
    private static MainActivity INSTANCE = null;
    FrameLayout flMAFrame;
    TabLayout tlTabLayout;
    private Toolbar tbToolbar;
    private ShraTextView stvToolbarTitle;

    public static MainActivity getInstance() {
        return INSTANCE;
    }

    @Override
    protected void onPause() {
        super.onPause();
        INSTANCE = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        INSTANCE = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        INSTANCE = this;

        initViews();

        setSupportActionBar(tbToolbar);
        stvToolbarTitle.setText(getTitle());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 121);
        } else {
            MAIN();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

            } else {
                showToast("Please allow all permissions");
                finish();
                return;
            }
        }
        MAIN();
    }

    private void MAIN() {
        setupTabs();

        changeFragment(HomeFragment.getInstance(), true);
    }

    private void setupTabs() {
        final TabLayout.Tab HomeTab = tlTabLayout.newTab();
        HomeTab.setText("Home");

        final TabLayout.Tab SettingsTab = tlTabLayout.newTab();
        SettingsTab.setText("Settings");

        final TabLayout.Tab MapTab = tlTabLayout.newTab();
        MapTab.setText("Map");

        final TabLayout.Tab DataDetailsTab = tlTabLayout.newTab();
        DataDetailsTab.setText("Data Details");


        tlTabLayout.addTab(HomeTab);
        tlTabLayout.addTab(DataDetailsTab);
        tlTabLayout.addTab(SettingsTab);
        //tlTabLayout.addTab(MapTab);


        tlTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab == HomeTab) {
                    changeFragment(HomeFragment.getInstance(), true);
                } else if (tab == SettingsTab) {
                    changeFragment(SettingsFragment.getInstance(), true);
                } else if (tab == MapTab) {
                    changeFragment(MapFragment.getInstance(), true);
                } else if (tab == DataDetailsTab) {
                    changeFragment(DataDetailsFragment.getInstance(), true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void changeFragment(Fragment fragment, boolean isFirst) {
        if (isFirst) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flMAFrame, fragment)
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flMAFrame, fragment)
                    .addToBackStack("Yes")
                    .commit();
        }
    }

    public void showToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    private void initViews() {
        tbToolbar = (Toolbar) findViewById(R.id.tbToolbar);
        stvToolbarTitle = (ShraTextView) findViewById(R.id.stvToolbarTitle);
        flMAFrame = (FrameLayout) findViewById(R.id.flMAFrame);
        tlTabLayout = (TabLayout) findViewById(R.id.tlTabLayout);
    }
}
