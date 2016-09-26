package com.cravings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import com.cravings.adapters.BottomBarAdapter;
import com.cravings.adapters.MenuSection;
import com.cravings.data.Menu;
import com.cravings.data.MenuItem;
import com.cravings.network.CraveAPI;
import com.roughike.bottombar.BottomBar;
import java.util.List;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MenuView extends AppCompatActivity {

    TextView testContent;

    private BottomBarAdapter bottomBarAdapter;
    private BottomBar mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_view);


        mBottomBar = BottomBar.attach(this, savedInstanceState);
        this.bottomBarAdapter = new BottomBarAdapter(mBottomBar, this);
        this.bottomBarAdapter.setUpBottomBar();

        testContent = (TextView) findViewById(R.id.test_content);

        final SectionedRecyclerViewAdapter sectionAdapter = new SectionedRecyclerViewAdapter();
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_menu_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        final String menuID = intent.getStringExtra("OBJECT ID");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final CraveAPI craveAPI = retrofit.create(CraveAPI.class);

        Call<Menu> menuQuery = craveAPI.getMenuById(menuID);
        menuQuery.enqueue(new Callback<Menu>() {
            @Override
            public void onResponse(Call<Menu> call, Response<Menu> response) {
                if (response.body() == null){ }
                else {
                    testContent.setText(response.body().getMenuName());
                    for (final String section : response.body().getSections()){
                        if (section != null) {
                            Call<List<MenuItem>> menuItemBySection = craveAPI.getMenuItemsBySection(menuID, section);
                            menuItemBySection.enqueue(new Callback<List<MenuItem>>() {
                                @Override
                                public void onResponse(Call<List<MenuItem>> call, Response<List<MenuItem>> response) {
                                    if (response.body().size() > 0) {
                                        Log.d("MENU ITEMS", response.body().toString());
                                    }
                                    sectionAdapter.addSection(new MenuSection(section, response.body(), new MenuSection.OnItemClickListener() {
                                        @Override
                                        public void onClick(MenuItem item) {
                                            Intent i = new Intent(getApplicationContext(), ItemView.class);
                                            i.putExtra("OBJECT ID", item.getObjectID());
                                            startActivity(i);
                                        }
                                    }));
                                    recyclerView.setAdapter(sectionAdapter);
                                }

                                @Override
                                public void onFailure(Call<List<MenuItem>> call, Throwable t) {
                                    Log.d("SHOULD NOT BEHERE1", t.getMessage());
                                }
                            });
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<Menu> call, Throwable t) {
                Log.d("SHOULD NOT BEHERE", t.getMessage());
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mBottomBar.onSaveInstanceState(outState);
    }
}
