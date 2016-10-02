package com.cravings;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import com.cravings.adapters.BottomBarAdapter;
import com.cravings.adapters.CraveLocationManager;
import com.cravings.fragments.SearchFragment;
import com.roughike.bottombar.BottomBar;
import android.Manifest;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_LOCATION_PERMISSION = 401;
    public static final String FRAGMENT_TAG = "FRAGMENT_TAG";

    CraveLocationManager craveLocationManager;

    private FrameLayout layoutContent;
    private BottomBar mBottomBar;
    private BottomBarAdapter bottomBarAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutContent = (FrameLayout) findViewById(R.id.main_frame_content);

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        this.bottomBarAdapter = new BottomBarAdapter(mBottomBar, this);
        this.bottomBarAdapter.setUpBottomBarMain();


        // display the given fragment
        if(getIntent().getStringExtra(FRAGMENT_TAG) != null){
            this.bottomBarAdapter.showFragment(getIntent().getStringExtra(FRAGMENT_TAG));
        }
        else {
            this.bottomBarAdapter.showFragment(SearchFragment.TAG);
        }

        craveLocationManager = new CraveLocationManager(this);


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
}
