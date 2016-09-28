package com.cravings.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mremondi on 9/28/16.
 */
public class RetrofitConnection {

    public static CraveAPI setUpRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://cfc99d57.ngrok.io/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(CraveAPI.class);
    }
}

// .baseUrl("http://10.0.2.2:3000/api/")
