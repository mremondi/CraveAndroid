package com.cravings.fragments;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.cravings.R;
import com.cravings.RestaurantView;
import com.cravings.adapters.CraveLocationManager;
import com.cravings.data.Restaurant;
import com.cravings.network.CraveAPI;
import com.cravings.network.RetrofitConnection;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mremondi on 7/25/16.
 */
public class NearMeFragment extends Fragment implements OnMapReadyCallback{
    public static final String TAG = "NearMeFragment";

    private HashMap<Marker, Restaurant> markerRestaurantHashMap;
    private LatLng location;
    private MapView mapView;
    private GoogleMap googleMap;
    private CraveLocationManager craveLocationManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.near_me_fragment, null, false);

        markerRestaurantHashMap = new HashMap<>();

        craveLocationManager = new CraveLocationManager(this.getContext());
        location = craveLocationManager.getLastKnownLocation();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

    }

    @Subscribe
    public void onLocationReceived(Location location) {
        this.location = new LatLng(location.getLatitude(), location.getLongitude());
        loadRestaurantsByLatLng();
    }

    public void loadRestaurantsByLatLng(){
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(this.location, (float)12));

        // set up retrofit
        final CraveAPI craveAPI = RetrofitConnection.setUpRetrofit();

        Call<List<Restaurant>> restaurantQuery = craveAPI.getNearbyRestaurants(location.latitude,
                                                                                location.longitude);
        restaurantQuery.enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                if (response.body() == null) {
                } else {
                    for (Restaurant restaurant : response.body()) {
                        addRestaurantMarker(restaurant);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {}
        });
    }

    public void addRestaurantMarker(Restaurant restaurant){
        LatLng latLng = new LatLng(restaurant.getLoc().getCoordinates()[1],
                                    restaurant.getLoc().getCoordinates()[0]);
        markerRestaurantHashMap.put(googleMap.addMarker(new MarkerOptions()
            .position(latLng)
            .title(restaurant.getRestaurantName())), restaurant);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        try {
            googleMap.setMyLocationEnabled(true);
            if (location != null){
                loadRestaurantsByLatLng();
            }
            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Intent i = new Intent(getContext(), RestaurantView.class);
                    i.putExtra(RestaurantView.RESTAURANT_ID, markerRestaurantHashMap.get(marker).getObjectID());
                    startActivity(i);
                }
            });
        }catch (SecurityException e){}
    }

    @Override
    public void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
