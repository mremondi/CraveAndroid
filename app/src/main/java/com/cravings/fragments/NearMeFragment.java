package com.cravings.fragments;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.cravings.R;
import com.cravings.RestaurantView;
import com.cravings.data.Restaurant;
import com.cravings.network.CraveAPI;
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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mremondi on 7/25/16.
 */
public class NearMeFragment extends Fragment implements OnMapReadyCallback{
    public static final String TAG = "NearMeFragment";

    private HashMap<Marker, Restaurant> markerRestaurantHashMap;

    private MapView mapView;
    private GoogleMap googleMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.near_me_fragment, null, false);

        markerRestaurantHashMap = new HashMap<>();

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
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, (float)14));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final CraveAPI craveAPI = retrofit.create(CraveAPI.class);

        Call<List<Restaurant>> restaurantQuery = craveAPI.getNearbyRestaurants(location.getLatitude(), location.getLongitude()); // (lat, long)
        restaurantQuery.enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                Log.d("onReponse", "NULL? " + response);
                if (response.body() == null) {
                    Log.d("is the body", "Null?");
                } else {
                    for (Restaurant restaurant : response.body()) {
                        addRestaurantMarker(restaurant);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                Log.d("FAILURE", t.getMessage());
            }
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
            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Intent i = new Intent(getContext(), RestaurantView.class);
                    i.putExtra("RESTAURANT ID", markerRestaurantHashMap.get(marker).getObjectID());
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
