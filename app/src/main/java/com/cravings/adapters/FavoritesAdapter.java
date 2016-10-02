package com.cravings.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.cravings.R;
import com.cravings.data.MenuItem;
import com.cravings.data.Rating;
import com.cravings.network.CraveAPI;
import com.cravings.network.RetrofitConnection;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder>{

    public interface OnItemClickListener {
        void onClick(Rating rating);
    }

    private Context context;
    private List<Rating> favorites;
    private final OnItemClickListener listener;

    public FavoritesAdapter(List<Rating> list, Context context, OnItemClickListener listener){
        this.favorites = list;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public FavoritesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_row, parent, false);
        return new ViewHolder(rowView, context);
    }

    @Override
    public void onBindViewHolder(FavoritesAdapter.ViewHolder holder, int position) {
        holder.bind(favorites.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        private Context context;

        private TextView tvFavoritesViewItemName;
        private TextView tvFavoritesViewItemRating;
        private TextView tvFavoritesViewItemPrice;
        private TextView tvFavoritesViewRestaurantName;

        public ViewHolder(View restaurantView, Context context) {
            super(restaurantView);
            tvFavoritesViewItemName = (TextView) restaurantView.findViewById(R.id.tvFavoritesViewItemName);
            tvFavoritesViewItemRating = (TextView) restaurantView.findViewById(R.id.tvFavoritesViewItemRating);
            tvFavoritesViewItemPrice = (TextView) restaurantView.findViewById(R.id.tvFavoritesViewItemPrice);
            tvFavoritesViewRestaurantName = (TextView) restaurantView.findViewById(R.id.tvFavoritesViewRestaurantName);
            this.context = context;
        }

        public void bind(final Rating rating, final OnItemClickListener listener){
            /* TODO:
                BEGINNING TRY OF SECOND RETROFIT CALL. REMEMBER THIS IS NESTED.
                TRY TO UN-NEST SOMEDAY. ESPECIALLY IF IT IS SLOW ONCE MY SERVER IS NOT LOCAL
            */

            tvFavoritesViewItemRating.setText(" " + rating.getRating());

            // set up retrofit
            final CraveAPI craveAPI = RetrofitConnection.setUpRetrofit();

            Call<MenuItem> itemQuery = craveAPI.getItemById(rating.getItemID());
            itemQuery.enqueue(new Callback<MenuItem>() {
                @Override
                public void onResponse(Call<MenuItem> call, Response<MenuItem> response) {
                    tvFavoritesViewItemName.setText(response.body().getName());
                    tvFavoritesViewItemPrice.setText("$" + response.body().getPrice());
                    tvFavoritesViewRestaurantName.setText(response.body().getRestaurant_name());
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onClick(rating);
                        }
                    });
                }

                @Override
                public void onFailure(Call<MenuItem> call, Throwable t) {
                    Log.d("Failed FavoritesAdapter", t.getMessage());
                }
            });

        }
    }
}
