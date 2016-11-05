package com.cravings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import com.cravings.adapters.BottomBarAdapter;
import com.cravings.adapters.MenuSection;
import com.cravings.data.Menu;
import com.cravings.data.MenuItem;
import com.cravings.network.CraveAPI;
import com.cravings.network.CraveConnection;
import com.roughike.bottombar.BottomBar;
import java.util.List;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuView extends AppCompatActivity {

    public static final String MENU_ID = "MENU_ID";

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
        final String menuID = intent.getStringExtra(MENU_ID);

        // set up retrofit
        final CraveAPI craveAPI = CraveConnection.setUpRetrofit();

        Call<Menu> menuQuery = craveAPI.getMenuById(menuID);
        menuQuery.enqueue(new Callback<Menu>() {
            @Override
            public void onResponse(Call<Menu> call, Response<Menu> response) {
                // if there is no menu
                if (response.body() == null){ }
                // there is a menu
                else {
                    testContent.setText(response.body().getMenuName());
                    // if there are sections
                    if (response.body().getSections().length != 0) {
                        for (final String section : response.body().getSections()) {
                            if (section != null) {
                                Call<List<MenuItem>> menuItemBySection = craveAPI.getMenuItemsBySection(menuID, section);
                                menuItemBySection.enqueue(new Callback<List<MenuItem>>() {
                                    @Override
                                    public void onResponse(Call<List<MenuItem>> call, Response<List<MenuItem>> response) {
                                        sectionAdapter.addSection(new MenuSection(section, response.body(), new MenuSection.OnItemClickListener() {
                                            @Override
                                            public void onClick(MenuItem item) {
                                                Intent i = new Intent(getApplicationContext(), ItemView.class);
                                                i.putExtra(ItemView.ITEM_ID, item.getObjectID());
                                                startActivity(i);
                                            }
                                        }));
                                        recyclerView.setAdapter(sectionAdapter);
                                    }

                                    @Override
                                    public void onFailure(Call<List<MenuItem>> call, Throwable t) {

                                    }
                                });
                            }
                        }
                    }
                    // there are no sections
                    else{
                        Call<List<MenuItem>> menuItemBySection = craveAPI.getMenuItems(menuID);
                        menuItemBySection.enqueue(new Callback<List<MenuItem>>() {
                            @Override
                            public void onResponse(Call<List<MenuItem>> call, Response<List<MenuItem>> response) {
                                sectionAdapter.addSection(new MenuSection("", response.body(), new MenuSection.OnItemClickListener() {
                                    @Override
                                    public void onClick(MenuItem item) {
                                        Intent i = new Intent(getApplicationContext(), ItemView.class);
                                        i.putExtra(ItemView.ITEM_ID, item.getObjectID());
                                        startActivity(i);
                                    }
                                }));
                                recyclerView.setAdapter(sectionAdapter);
                            }

                            @Override
                            public void onFailure(Call<List<MenuItem>> call, Throwable t) {

                            }
                        });
                    }
                }
            }
            @Override
            public void onFailure(Call<Menu> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mBottomBar.onSaveInstanceState(outState);
    }
}
