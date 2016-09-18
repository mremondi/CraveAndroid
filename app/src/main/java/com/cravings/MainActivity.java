package com.cravings;

import android.content.pm.PackageManager;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import com.cravings.adapters.CraveLocationManager;
import com.cravings.fragments.FavoritesFragment;
import com.cravings.fragments.FeaturedFragment;
import com.cravings.fragments.NearMeFragment;
import com.cravings.fragments.ProfileFragment;
import com.cravings.fragments.SearchFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import android.Manifest;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_LOCATION_PERMISSION = 401;

    public static final String FRAGMENT_TAG = "FRAGMENT_TAG";

    CraveLocationManager craveLocationManager;

    private FrameLayout layoutContent;
    private BottomBar mBottomBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutContent = (FrameLayout) findViewById(R.id.main_frame_content);

        // display the given fragment
        if(getIntent().getStringExtra("FRAGMENT_TAG") != null){
            showFragment(getIntent().getStringExtra(FRAGMENT_TAG));
        }
        else {
            showFragment(SearchFragment.TAG);
        }

        craveLocationManager = new CraveLocationManager(this);

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        setUpBottomBar();
        requestNeededPermission();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mBottomBar.onSaveInstanceState(outState);
    }

    public void requestNeededPermission(){
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "Crave requires usage of location services", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        }
        else{
            craveLocationManager.startLocationMonitoring();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_LOCATION_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "FINE_LOC perm granted", Toast.LENGTH_SHORT).show();
                    craveLocationManager.startLocationMonitoring();
                } else {
                    Toast.makeText(this, "FINE_LOC perm NOT granted", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    private void showFragment(String tag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            if (tag.equals(SearchFragment.TAG)) {
                fragment = new SearchFragment();
            }
            else if (tag.equals(FeaturedFragment.TAG)) {
                fragment = new FeaturedFragment();
            }
            else if (tag.equals(FavoritesFragment.TAG)) {
                fragment = new FavoritesFragment();
            }
            else if (tag.equals(NearMeFragment.TAG)) {
                fragment = new NearMeFragment();
            }
            else if (tag.equals(ProfileFragment.TAG)) {
                fragment = new ProfileFragment();
            }
        }

        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        trans.replace(R.id.main_frame_content, fragment, tag);
        trans.addToBackStack(null);
        trans.commit();
    }

    public void setUpBottomBar(){
        mBottomBar.noNavBarGoodness();
        mBottomBar.setItems(R.menu.bottombar_menu);
        mBottomBar.setDefaultTabPosition(2);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.nav_bar_featured) {
                    // go to featured fragment
                }
                else if(menuItemId == R.id.nav_bar_near_me) {
                    showFragment(NearMeFragment.TAG);
                }
                else if(menuItemId == R.id.nav_bar_search) {
                    showFragment(SearchFragment.TAG);
                }
                else if (menuItemId == R.id.nav_bar_favorites) {
                    showFragment(FavoritesFragment.TAG);
                }
                else if (menuItemId == R.id.nav_bar_profile) {
                    // go to profile fragment
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.nav_bar_featured) {
                    //go to featured fragment
                }
                else if(menuItemId == R.id.nav_bar_search) {
                    // go to search fragment
                }
                else if(menuItemId == R.id.nav_bar_near_me) {
                    // go to near me fragment
                }
                else if (menuItemId == R.id.nav_bar_favorites) {
                    // go to favorites fragments
                }
                else if (menuItemId == R.id.nav_bar_profile) {
                    // go to profile fragment
                }
            }
        });
    }
}
