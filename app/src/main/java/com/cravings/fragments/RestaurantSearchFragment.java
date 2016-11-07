package com.cravings.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cravings.R;
import com.cravings.RestaurantView;
import com.cravings.adapters.SearchRestaurantAdapter;
import com.cravings.data.Restaurant;
import com.cravings.network.CraveAPI;
import com.cravings.network.CraveConnection;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mremondi on 10/18/16.
 */

public class RestaurantSearchFragment extends Fragment {
    public static final String TITLE = "Restaurant Search";

    private EditText etSearch;
    private Button btnSearch;
    private TextView tvSearchFragmentTitle;
    private LatLng location;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.restaurant_search_fragment, null);

        location = ((SearchFragment) this.getParentFragment()).getLocation();

        final CraveAPI craveAPI = CraveConnection.setUpRetrofit();

        // set up recycler view
        final RecyclerView searchRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        searchRecyclerView.setLayoutManager(linearLayoutManager);

        etSearch = (EditText) rootView.findViewById(R.id.etSearch);
        btnSearch = (Button) rootView.findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etSearch.getText().toString().equals("")) {
                    return;
                }
                // TODO: LOCATION FILTERED SEARCH
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
        });
        return rootView;
    }
}
