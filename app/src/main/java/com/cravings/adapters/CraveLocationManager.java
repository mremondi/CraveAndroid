package com.cravings.adapters;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by mremondi on 7/28/16.
 */
public class CraveLocationManager implements LocationListener {

    private Context context;
    private LocationManager locationManager;

    public CraveLocationManager(Context context) {
        this.context = context;
    }

    public void startLocationMonitoring() throws SecurityException {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1000, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 1000, this);
    }


    public void stopLocationMonitoring() throws SecurityException {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        EventBus.getDefault().post(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
