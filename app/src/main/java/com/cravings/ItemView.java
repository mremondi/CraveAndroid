package com.cravings;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import com.cravings.data.MenuItem;
import com.cravings.fragments.NearMeFragment;
import com.cravings.fragments.SearchFragment;
import com.cravings.network.CraveAPI;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


// TODO: INCLUDE BOTTOM BAR HERE: it has to be initialized in the code...

public class ItemView extends AppCompatActivity {

    private String restaurant_id;
    private String objectID;

    private BottomBar mBottomBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        setUpBottomBar();

        final TextView tvItemViewTitle = (TextView) findViewById(R.id.tvItemViewTitle);
        final TextView tvItemViewRestaurantName = (TextView) findViewById(R.id.tvItemViewRestaurantName);
        final TextView tvItemViewPrice = (TextView) findViewById(R.id.tvItemViewPrice);
        final RatingBar rbItemViewRating = (RatingBar) findViewById(R.id.rbItemViewRating);
        final TextView tvItemViewDescription = (TextView) findViewById(R.id.tvItemViewDescription);
        final TextView tvItemViewTags = (TextView) findViewById(R.id.tvItemViewTags);



        Intent intent = getIntent();
        objectID = intent.getStringExtra("OBJECT ID");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final CraveAPI craveAPI = retrofit.create(CraveAPI.class);

        Call<MenuItem> itemQuery = craveAPI.getItemById(objectID);
        itemQuery.enqueue(new Callback<MenuItem>() {
            @Override
            public void onResponse(Call<MenuItem> call, Response<MenuItem> response) {
                if (response.body() == null) {}
                else {
                    tvItemViewRestaurantName.setText(""+ response.body().getRestaurant_name());
                    tvItemViewTitle.setText("" + response.body().getName());
                    tvItemViewPrice.setText("$" + response.body().getPrice());
                    tvItemViewDescription.setText("" + response.body().getDescription());
                    StringBuilder sb = new StringBuilder();
                    for(String tag: response.body().getDietaryInfo()){
                        sb.append(tag);
                    }
                    tvItemViewTags.setText(sb.toString().trim());
                    rbItemViewRating.setRating(response.body().getRating() / response.body().getNumberOfRatings());
                    Log.d("RATING", ""+response.body().getRating() / response.body().getNumberOfRatings());
                    restaurant_id = response.body().getRestaurantID();
                }
            }
            @Override
            public void onFailure(Call<MenuItem> call, Throwable t) { }
        });

        //TODO: COMMENTED OUT CSRF stuff in web app. Need to put that back in and still have this work
        if (rbItemViewRating != null) {
            rbItemViewRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if (fromUser) {
                        Call<MenuItem> putRating = craveAPI.rateItem(objectID, rating);
                        putRating.enqueue(new Callback<MenuItem>() {
                            @Override
                            public void onResponse(Call<MenuItem> call, Response<MenuItem> response) {
                                rbItemViewRating.setRating(response.body().getRating() / response.body().getNumberOfRatings());
                            }

                            @Override
                            public void onFailure(Call<MenuItem> call, Throwable t) {
                                Log.d("FAILED TO PUT", "RATING");
                            }
                        });
                    }
                }
            });
        }

        if (tvItemViewRestaurantName !=null) {
            tvItemViewRestaurantName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), RestaurantView.class);
                    i.putExtra("RESTAURANT ID", restaurant_id);
                    startActivity(i);
                }
            });
        }
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
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.putExtra(MainActivity.FRAGMENT_TAG, NearMeFragment.TAG);
                    startActivity(i);
                }
                else if(menuItemId == R.id.nav_bar_search) {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.putExtra(MainActivity.FRAGMENT_TAG, SearchFragment.TAG);
                    startActivity(i);
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
