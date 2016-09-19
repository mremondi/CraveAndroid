package com.cravings.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.cravings.R;
import com.cravings.data.Rating;
import com.cravings.data.Restaurant;
import java.util.List;


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

        public ViewHolder(View restaurantView, Context context) {
            super(restaurantView);
            tvFavoritesViewItemName = (TextView) restaurantView.findViewById(R.id.tvFavoritesViewItemName);
            tvFavoritesViewItemRating = (TextView) restaurantView.findViewById(R.id.tvFavoritesViewItemRating);
            this.context = context;
        }

        public void bind(final Rating rating, final OnItemClickListener listener){
            tvFavoritesViewItemName.setText(rating.getItemID());
            tvFavoritesViewItemRating.setText(""+rating.getRating());
        }
    }
}
