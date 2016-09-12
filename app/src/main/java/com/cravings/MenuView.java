package com.cravings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.cravings.adapters.MenuItemRecyclerAdapter;
import com.cravings.adapters.MenuRecyclerViewAdapter;
import com.cravings.data.Menu;
import com.cravings.data.MenuItem;
import com.cravings.network.CraveAPI;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MenuView extends AppCompatActivity {

    TextView testContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_view);

        testContent = (TextView) findViewById(R.id.test_content);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_menu_view);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        Intent intent = getIntent();
        String menuID = intent.getStringExtra("OBJECT ID");

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
                }
            }
            @Override
            public void onFailure(Call<Menu> call, Throwable t) {}
        });

        Call<List<MenuItem>> menuItemQuery = craveAPI.getMenuItems(menuID);
        menuItemQuery.enqueue(new Callback<List<MenuItem>>() {
            @Override
            public void onResponse(Call<List<MenuItem>> call, Response<List<MenuItem>> response) {
                Log.d("ITEMS", response.body().get(0).getName());

                MenuItemRecyclerAdapter menuItemAdapter = new MenuItemRecyclerAdapter(response.body(), getApplicationContext(),
                        new MenuItemRecyclerAdapter.OnItemClickListener(){
                            @Override
                            public void onClick(MenuItem item) {
                                Intent i = new Intent(getApplicationContext(), ItemView.class);
                                i.putExtra("OBJECT ID", item.getObjectID());
                                startActivity(i);
                            }
                        });
                recyclerView.setAdapter(menuItemAdapter);

                // TODO: get sort by section working... need to do this from CraveWeb first
            }

            @Override
            public void onFailure(Call<List<MenuItem>> call, Throwable t) { Log.d("HERE in fail", t.toString());}
        });
    }
}
