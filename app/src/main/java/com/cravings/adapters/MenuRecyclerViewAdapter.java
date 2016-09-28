package com.cravings.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.cravings.R;
import com.cravings.data.Menu;
import com.cravings.data.Restaurant;
import com.cravings.network.CraveAPI;
import com.cravings.network.RetrofitConnection;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MenuRecyclerViewAdapter extends RecyclerView.Adapter<MenuRecyclerViewAdapter.ViewHolder>{

    public interface OnItemClickListener {
        void onClick(String menu);
    }

    private Context context;
    private ArrayList<String> menus = new ArrayList<>();
    private final OnItemClickListener listener;

    public MenuRecyclerViewAdapter(String[] list, Context context, OnItemClickListener listener){
        for (String l: list){
            this.menus.add(l);
        }
        this.context = context;
        this.listener = listener;
    }

    @Override
    public MenuRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_row, parent, false);
        return new ViewHolder(rowView, context);
    }

    @Override
    public void onBindViewHolder(MenuRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.bind(menus.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        private TextView tvMenuName;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            tvMenuName = (TextView) itemView.findViewById(R.id.tvRestViewMenuName);
        }

        public void bind(final String menu, final OnItemClickListener listener){

            /* TODO:
                BEGINNING TRY OF SECOND RETROFIT CALL. REMEMBER THIS IS NESTED.
                TRY TO UN-NEST SOMEDAY. ESPECIALLY IF IT IS SLOW ONCE MY SERVER IS NOT LOCAL
            */

            // set up retrofit
            final CraveAPI craveAPI = RetrofitConnection.setUpRetrofit();

            Call<Menu> menuCall = craveAPI.getMenuById(menu);
            menuCall.enqueue(new Callback<Menu>() {
                @Override
                public void onResponse(Call<Menu> call, Response<Menu> response) {
                    tvMenuName.setText(response.body().getMenuName());
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onClick(menu);
                        }
                    });
                }

                @Override
                public void onFailure(Call<Menu> call, Throwable t) {

                }
            });
        }
    }
}
