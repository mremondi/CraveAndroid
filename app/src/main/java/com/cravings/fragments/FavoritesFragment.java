package com.cravings.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cravings.ItemView;
import com.cravings.LoginActivity;
import com.cravings.R;
import com.cravings.adapters.FavoritesAdapter;
import com.cravings.adapters.SearchAdapter;
import com.cravings.data.MenuItem;
import com.cravings.data.Rating;
import com.cravings.network.CraveAPI;
import com.cravings.network.RetrofitConnection;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mremondi on 7/25/16.
 */
public class FavoritesFragment extends Fragment {
    public static final String TAG = "FavoritesFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favorites_fragment, null, false);

        final RecyclerView favoritesRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_favorites);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        favoritesRecyclerView.setLayoutManager(linearLayoutManager);

        SharedPreferences prefs = getActivity().getSharedPreferences(LoginActivity.USER_DATA, 0);
        boolean loggedIn = prefs.getBoolean(LoginActivity.LOGGED_IN, false);
        String user_id = prefs.getString(LoginActivity.USER_ID, "");

        // set up retrofit
        final CraveAPI craveAPI = RetrofitConnection.setUpRetrofit();

        Call<List<Rating>> ratingQuery = craveAPI.getUserFavorites(user_id);
        ratingQuery.enqueue(new Callback<List<Rating>>() {
            @Override
            public void onResponse(Call<List<Rating>> call, Response<List<Rating>> response) {
                if(response.body() == null){}
                else {
                    FavoritesAdapter favoritesAdapter = new FavoritesAdapter(response.body(), getContext(),
                            new FavoritesAdapter.OnItemClickListener() {
                                @Override
                                public void onClick(Rating rating) {
                                    Intent i = new Intent(getActivity(), ItemView.class);
                                    i.putExtra(ItemView.ITEM_ID, rating.getItemID());
                                    startActivity(i);
                                }
                            });
                    favoritesRecyclerView.setAdapter(favoritesAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Rating>> call, Throwable t) {
                Log.d("FAILURE", t.getMessage());
            }
        });
                // USE THE RATINGS TO GET EACH ITEM WITH THE CURRENT USER'S RATING

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
