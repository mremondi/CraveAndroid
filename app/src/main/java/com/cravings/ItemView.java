package com.cravings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import com.cravings.adapters.BottomBarAdapter;
import com.cravings.data.MenuItem;
import com.cravings.network.CraveAPI;
import com.cravings.network.RetrofitConnection;
import com.roughike.bottombar.BottomBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ItemView extends AppCompatActivity {

    public static final String ITEM_ID = "ITEM_ID";

    private String restaurant_id;
    private String itemID;

    private BottomBarAdapter bottomBarAdapter;
    private BottomBar mBottomBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        this.bottomBarAdapter = new BottomBarAdapter(mBottomBar, this);
        this.bottomBarAdapter.setUpBottomBar();

        final TextView tvItemViewTitle = (TextView) findViewById(R.id.tvItemViewTitle);
        final TextView tvItemViewRestaurantName = (TextView) findViewById(R.id.tvItemViewRestaurantName);
        final TextView tvItemViewPrice = (TextView) findViewById(R.id.tvItemViewPrice);
        final RatingBar rbItemViewRating = (RatingBar) findViewById(R.id.rbItemViewRating);
        final TextView tvItemViewDescription = (TextView) findViewById(R.id.tvItemViewDescription);
        final TextView tvItemViewTags = (TextView) findViewById(R.id.tvItemViewTags);

        Intent intent = getIntent();
        itemID = intent.getStringExtra(ITEM_ID);

        // set up retrofit
        final CraveAPI craveAPI = RetrofitConnection.setUpRetrofit();

        Call<MenuItem> itemQuery = craveAPI.getItemById(itemID);
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
                    restaurant_id = response.body().getRestaurantID();
                }
            }
            @Override
            public void onFailure(Call<MenuItem> call, Throwable t) { }
        });

        if (rbItemViewRating != null) {
            rbItemViewRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

                SharedPreferences prefs = getSharedPreferences(LoginActivity.USER_DATA, 0);
                String user_id = prefs.getString(LoginActivity.USER_ID, "");

                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if (fromUser) {
                        Log.d("ITEM ID", itemID);
                        Log.d("USER ID", user_id);
                        Call<MenuItem> putRating = craveAPI.rateItem(itemID, rating, user_id);
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
                    i.putExtra(RestaurantView.RESTAURANT_ID, restaurant_id);
                    startActivity(i);
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mBottomBar.onSaveInstanceState(outState);
    }
}
