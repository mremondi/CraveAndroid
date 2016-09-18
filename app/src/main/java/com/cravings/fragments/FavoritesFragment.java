package com.cravings.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cravings.R;
import com.cravings.data.MenuItem;
import com.cravings.data.Rating;
import com.cravings.network.CraveAPI;

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

        // GET CURRENT USER ID
        SharedPreferences prefs = getActivity().getSharedPreferences("UserData", 0);
        String user_id = prefs.getString("user_id", "");

        // CALL and GET THE LIST OF RATINGS
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final CraveAPI craveAPI = retrofit.create(CraveAPI.class);

        Call<List<Rating>> ratingQuery = craveAPI.getUserFavorites(user_id);
        ratingQuery.enqueue(new Callback<List<Rating>>() {
            @Override
            public void onResponse(Call<List<Rating>> call, Response<List<Rating>> response) {

            }

            @Override
            public void onFailure(Call<List<Rating>> call, Throwable t) {

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
