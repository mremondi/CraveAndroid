package com.cravings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import com.cravings.data.MenuItem;
import com.cravings.network.CraveAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


// TODO: INCLUDE BOTTOM BAR HERE: it has to be initialized in the code...

public class ItemView extends AppCompatActivity {

    String restaurant_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

        final TextView tvItemViewTitle = (TextView) findViewById(R.id.tvItemViewTitle);
        final TextView tvItemViewRestaurantName = (TextView) findViewById(R.id.tvItemViewRestaurantName);
        final TextView tvItemViewPrice = (TextView) findViewById(R.id.tvItemViewPrice);
        final RatingBar rbItemViewRating = (RatingBar) findViewById(R.id.rbItemViewRating);
        final TextView tvItemViewDescription = (TextView) findViewById(R.id.tvItemViewDescription);
        final TextView tvItemViewTags = (TextView) findViewById(R.id.tvItemViewTags);

        Intent intent = getIntent();
        String objectID = intent.getStringExtra("OBJECT ID");

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
                    tvItemViewTags.setText("" + response.body().getDietaryInfo());
                    rbItemViewRating.setRating(response.body().getRating());
                    restaurant_id = response.body().getRestaurantID();
                }
            }
            @Override
            public void onFailure(Call<MenuItem> call, Throwable t) { }
        });

        rbItemViewRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                // TODO: POST to API and update item rating and increment number of ratings
            }
        });

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
