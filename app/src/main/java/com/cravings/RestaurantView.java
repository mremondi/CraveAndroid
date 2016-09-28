package com.cravings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import com.cravings.adapters.BottomBarAdapter;
import com.cravings.adapters.MenuRecyclerViewAdapter;
import com.cravings.data.Restaurant;
import com.cravings.network.CraveAPI;
import com.cravings.network.RetrofitConnection;
import com.roughike.bottombar.BottomBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantView extends AppCompatActivity {

    private BottomBarAdapter bottomBarAdapter;
    private BottomBar mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_view);

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        this.bottomBarAdapter = new BottomBarAdapter(mBottomBar, this);
        this.bottomBarAdapter.setUpBottomBar();

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

        // set up retrofit
        final CraveAPI craveAPI = RetrofitConnection.setUpRetrofit();

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mBottomBar.onSaveInstanceState(outState);
    }
}
