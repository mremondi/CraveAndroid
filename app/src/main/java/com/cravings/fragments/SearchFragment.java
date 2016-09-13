package com.cravings.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.cravings.ItemView;
import com.cravings.R;
import com.cravings.adapters.SearchAdapter;
import com.cravings.data.Menu;
import com.cravings.data.MenuItem;
import com.cravings.data.ModelObject;
import com.cravings.data.Restaurant;
import com.cravings.network.CraveAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchFragment extends Fragment {
    public static final String TAG = "SearchFragment";

    private EditText etSearch;
    private Button btnSearch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.search_fragment, null, false);

        final RecyclerView searchRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        searchRecyclerView.setLayoutManager(linearLayoutManager);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final CraveAPI craveAPI = retrofit.create(CraveAPI.class);

        etSearch = (EditText) rootView.findViewById(R.id.etSearch);
        btnSearch = (Button) rootView.findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etSearch.getText().toString().equals("")){
                    return;
                }
                Call<List<MenuItem>> itemQuery = craveAPI.searchItems(etSearch.getText().toString());
                itemQuery.enqueue(new Callback<List<MenuItem>>() {
                    @Override
                    public void onResponse(Call<List<MenuItem>> call, Response<List<MenuItem>> response) {
                        if (response.body() == null){}
                        else {
                            SearchAdapter searchAdapter = new SearchAdapter(response.body(), getContext(),
                                    new SearchAdapter.OnItemClickListener(){
                                        @Override
                                        public void onClick(ModelObject item) {
                                            Intent i = new Intent(getContext(), ItemView.class);
                                            i.putExtra("OBJECT ID", item.getObjectID());
                                            Log.d("OBJECTID", item.getObjectID());
                                            startActivity(i);
                                        }
                                    });
                            searchRecyclerView.setAdapter(searchAdapter);
                        }
                    }
                    @Override
                    public void onFailure(Call<List<MenuItem>> call, Throwable t) {}
                });
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
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
