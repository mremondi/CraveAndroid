package com.cravings;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cravings.adapters.BottomBarAdapter;
import com.cravings.adapters.MenuRecyclerViewAdapter;
import com.cravings.data.Restaurant;
import com.cravings.network.CraveAPI;
import com.cravings.network.CraveConnection;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.roughike.bottombar.BottomBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantView extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    public static final String RESTAURANT_ID = "RESTAURANT ID";

    private BottomBarAdapter bottomBarAdapter;
    private BottomBar mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_view);


        mBottomBar = BottomBar.attach(this, savedInstanceState);
        this.bottomBarAdapter = new BottomBarAdapter(mBottomBar, this);
        this.bottomBarAdapter.setUpBottomBar();

        final ImageView ivRestViewLogo = (ImageView) findViewById(R.id.ivRestViewLogo);
        final TextView tvRestViewTags = (TextView) findViewById(R.id.tvRestViewTags);
        final TextView tvRestViewAddress = (TextView) findViewById(R.id.tvRestViewAddress);
        final TextView tvRestViewURL = (TextView) findViewById(R.id.tvRestViewURL);
        final TextView tvRestViewPhone = (TextView) findViewById(R.id.tvRestViewPhone);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewRestView);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final AppCompatActivity activity = this;

        Intent intent = getIntent();
        String restaurantID = intent.getStringExtra(RESTAURANT_ID);

        // set up retrofit
        final CraveAPI craveAPI = CraveConnection.setUpRetrofit();

        Call<Restaurant> restaurantQuery = craveAPI.getRestaurantById(restaurantID);
        restaurantQuery.enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, final Response<Restaurant> response) {
                if (response.body() == null) {}
                else {
                    Glide.with(activity).load(response.body().getRestaurant_logo_URL()).into(ivRestViewLogo);
                    for (String tag : response.body().getTags()){
                        tvRestViewTags.setText(tag + "\n");
                    }
                    tvRestViewAddress.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri gmmIntentUri = Uri.parse("geo:" +
                                    response.body().getLoc().getCoordinates()[1] +
                                    "," +
                                    response.body().getLoc().getCoordinates()[0] +
                                    "?q=" +
                                    response.body().getAddress());
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                                startActivity(mapIntent);
                            }
                        }
                    });
                    tvRestViewURL.setText("" + response.body().getRestaurant_URL());

                    Log.d("PHONE NUMBER", "" + response.body().getPhone_number());
                    tvRestViewPhone.setText("" + response.body().getPhone_number());

                    MenuRecyclerViewAdapter searchAdapter = new MenuRecyclerViewAdapter(
                            response.body().getMenus(), getApplicationContext(),
                            new MenuRecyclerViewAdapter.OnItemClickListener(){
                                @Override
                                public void onClick(String menu) {
                                    Intent i = new Intent(getApplicationContext(), MenuView.class);
                                    i.putExtra(MenuView.MENU_ID, menu);
                                    startActivity(i);
                                }
                            });
                    recyclerView.setAdapter(searchAdapter);
                }
            }

            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mBottomBar.onSaveInstanceState(outState);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
