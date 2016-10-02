package com.cravings.fragments;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.cravings.ItemView;
import com.cravings.R;
import com.cravings.RestaurantView;
import com.cravings.adapters.SearchAdapter;
import com.cravings.adapters.SearchRestaurantAdapter;
import com.cravings.data.MenuItem;
import com.cravings.data.Restaurant;
import com.cravings.network.CraveAPI;
import com.cravings.network.RetrofitConnection;
import com.google.android.gms.maps.model.LatLng;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    public static final String TAG = "SearchFragment";

    private EditText etSearch;
    private Button btnSearch;
    private TextView tvSearchFragmentTitle;

    private String filter;
    private boolean searchItem = true;

    private LatLng location;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.search_fragment, null, false);

        // Set up spinner
        Spinner spinner = (Spinner) rootView.findViewById(R.id.spFilter);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.filter_choices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setPrompt("Choose a Filter");
        spinner.setOnItemSelectedListener(this);

        // set up recycler view
        final RecyclerView searchRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        searchRecyclerView.setLayoutManager(linearLayoutManager);

        // set up retrofit
        final CraveAPI craveAPI = RetrofitConnection.setUpRetrofit();

        // set up views
        etSearch = (EditText) rootView.findViewById(R.id.etSearch);
        btnSearch = (Button) rootView.findViewById(R.id.btnSearch);
        tvSearchFragmentTitle = (TextView) rootView.findViewById(R.id.tvSearchFragmentTitle);
        tvSearchFragmentTitle.setText(R.string.searchview_mealSearch);
        tvSearchFragmentTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchItem = !searchItem;
                if (searchItem){
                    tvSearchFragmentTitle.setText(R.string.searchview_mealSearch);
                }
                else{
                    tvSearchFragmentTitle.setText(R.string.searchview_restaurantSearch);
                }
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etSearch.getText().toString().equals("")){
                    return;
                }
                if (searchItem) {
                    Call<List<MenuItem>> itemQuery = craveAPI.searchItemsSorted(etSearch.getText().toString(), location.latitude, location.longitude, filter);
                    itemQuery.enqueue(new Callback<List<MenuItem>>() {
                        @Override
                        public void onResponse(Call<List<MenuItem>> call, Response<List<MenuItem>> response) {
                            if (response.body() == null) {
                            } else {
                                SearchAdapter searchAdapter = new SearchAdapter(response.body(), getContext(),
                                        new SearchAdapter.OnItemClickListener() {
                                            @Override
                                            public void onClick(MenuItem item) {
                                                Intent i = new Intent(getContext(), ItemView.class);
                                                i.putExtra(ItemView.ITEM_ID, item.getObjectID());
                                                startActivity(i);
                                            }
                                        });
                                searchRecyclerView.setAdapter(searchAdapter);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<MenuItem>> call, Throwable t) {
                        }
                    });
                }
                else{
                    // TODO: Make it searchable by restaurant cuisine tag AND name. This needs to be done server side
                    Call<List<Restaurant>> restaurantQuery = craveAPI.searchRestaurants(etSearch.getText().toString());
                    restaurantQuery.enqueue(new Callback<List<Restaurant>>() {
                        @Override
                        public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                            if (response.body() == null) {
                            } else {
                                SearchRestaurantAdapter searchRestaurantAdapter = new SearchRestaurantAdapter(response.body(), getContext(),
                                        new SearchRestaurantAdapter.OnItemClickListener() {
                                            @Override
                                            public void onClick(Restaurant restaurant) {
                                                Intent i = new Intent(getContext(), RestaurantView.class);
                                                i.putExtra(RestaurantView.RESTAURANT_ID, restaurant.getObjectID());
                                                startActivity(i);
                                                getActivity().finish();
                                            }
                                        });
                                searchRecyclerView.setAdapter(searchRestaurantAdapter);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                        }
                    });
                }
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (parent.getItemAtPosition(pos).equals("Price: Low -> High")){
            this.filter = "PLH";
        }
        else if (parent.getItemAtPosition(pos).equals("Price: High -> Low")){
            this.filter = "PHL";
        }
        else if (parent.getItemAtPosition(pos).equals("Nearby")){
            this.filter = "NEARBY";
        }
        else if (parent.getItemAtPosition(pos).equals("Rating")){
            this.filter = "RATING";
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
        this.filter = "PLH";
    }

    @Subscribe
    public void onLocationReceived(Location location) {
        this.location = new LatLng(location.getLatitude(), location.getLongitude());

    }
}
