package com.cravings;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import com.cravings.adapters.MenuRecyclerViewAdapter;
import com.cravings.data.Restaurant;
import com.cravings.network.CraveAPI;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantView extends AppCompatActivity {

    private BottomBar mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_view);

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        setUpBottomBar();

        final TextView tvRestViewRestaurantName = (TextView) findViewById(R.id.tvRestViewRestaurantName);
        final TextView tvRestViewTags = (TextView) findViewById(R.id.tvRestViewTags);
        final TextView tvRestViewAddress = (TextView) findViewById(R.id.tvRestViewAddress);
        final TextView tvRestViewURL = (TextView) findViewById(R.id.tvRestViewURL);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewRestView);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        String restaurantID = intent.getStringExtra("RESTAURANT ID");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final CraveAPI craveAPI = retrofit.create(CraveAPI.class);

        Call<Restaurant> restaurantQuery = craveAPI.getRestaurantById(restaurantID);
        restaurantQuery.enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                if (response.body() == null) {}
                else {
                    tvRestViewRestaurantName.setText("" + response.body().getRestaurantName());
                    for (String tag : response.body().getTags()){
                        Log.d("TAGS: ", tag);
                        tvRestViewTags.setText(tvRestViewTags.getText() + ", " + tag);
                    }
                    tvRestViewAddress.setText("" + response.body().getAddress());
                    tvRestViewURL.setText("" + response.body().getRestaurant_URL());


//                     TODO: getting the menu names...
//                     other way
//                     use the object ids to get the menu names... where though
                    // what I will do:::;
                        // create a list of menu Ids
                    // then after those are populated....
                    // do another query of menus


                    MenuRecyclerViewAdapter searchAdapter = new MenuRecyclerViewAdapter(
                            response.body().getMenus(), getApplicationContext(),
                            new MenuRecyclerViewAdapter.OnItemClickListener(){
                                @Override
                                public void onClick(String menu) {
                                    Intent i = new Intent(getApplicationContext(), MenuView.class);
                                    i.putExtra("OBJECT ID", menu);
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

    public void setUpBottomBar(){
        mBottomBar.noNavBarGoodness();
        mBottomBar.setItems(R.menu.bottombar_menu);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.nav_bar_featured) {
                    // go to featured fragment
                }
                else if(menuItemId == R.id.nav_bar_near_me) {

                }
                else if(menuItemId == R.id.nav_bar_search) {

                }
                else if (menuItemId == R.id.nav_bar_favorites) {

                }
                else if (menuItemId == R.id.nav_bar_profile) {

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mBottomBar.onSaveInstanceState(outState);
    }
}
