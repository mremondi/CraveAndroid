package com.cravings.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cravings.R;
import com.cravings.data.User;
import com.cravings.network.CraveAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mremondi on 7/25/16.
 */
public class ProfileFragment extends Fragment {
    public static final String TAG = "ProfileFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_fragment, null, false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final CraveAPI craveAPI = retrofit.create(CraveAPI.class);

        // GET CURRENT USER ID
        SharedPreferences prefs = getActivity().getSharedPreferences("UserData", 0);
        String user_id = prefs.getString("user_id", "");

        Call<User> userCall = craveAPI.getUserProfile(user_id);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                // GET USER INFO AND PLUG IT INTO Textviews
                Log.d("USER?", response.body().toString());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        // Call User:
        // get email
        // change password stuff
        // upload pictures...?


        // other stuff:
        // settings - notifications, etc
        // privacy policy
        // terms of use
        // help
        // about

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
