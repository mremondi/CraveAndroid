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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.cravings.ItemView;
import com.cravings.R;
import com.cravings.adapters.SearchAdapter;
import com.cravings.data.MenuItem;
import com.cravings.network.CraveAPI;
import com.cravings.network.RetrofitConnection;
import com.google.android.gms.maps.model.LatLng;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mremondi on 10/18/16.
 */

public class ItemSearchFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    public static final String TITLE = "Item Search";

    private LatLng location;

    private String filter;

    private EditText etSearch;
    private Button btnSearch;
    private TextView tvSearchFragmentTitle;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_search_fragment, null);

        location = ((SearchFragment) this.getParentFragment()).getLocation();

        // Set up spinner
        final Spinner spinner = (Spinner) rootView.findViewById(R.id.spFilter);
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

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etSearch.getText().toString().equals("")){
                    return;
                }
                if (location != null) {
                    Call<List<MenuItem>> itemQuery = craveAPI.searchItemsSorted(etSearch.getText().toString(), location.latitude, location.longitude, filter);
                    itemQuery.enqueue(new Callback<List<MenuItem>>() {
                        @Override
                        public void onResponse(Call<List<MenuItem>> call, Response<List<MenuItem>> response) {
                            if (response.body() == null) {
                            } else {
                                Log.d("onResponse: ", response.body().toString());
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
                            Log.d("onFailure", t.getMessage());
                        }
                    });
                }
            }
        });


        return rootView;
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

}
