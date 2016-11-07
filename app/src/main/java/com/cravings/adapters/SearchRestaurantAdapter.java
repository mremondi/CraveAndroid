package com.cravings.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cravings.R;
import com.cravings.data.Restaurant;
import java.util.List;


public class SearchRestaurantAdapter extends RecyclerView.Adapter<SearchRestaurantAdapter.ViewHolder>{

    public interface OnItemClickListener {
        void onClick(Restaurant restaurant);
    }

    private Context context;
    private List<Restaurant> searchRestaurants;
    private final OnItemClickListener listener;

    public SearchRestaurantAdapter(List<Restaurant> list, Context context, OnItemClickListener listener){
        this.searchRestaurants = list;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public SearchRestaurantAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_row, parent, false);
        return new ViewHolder(rowView, context);
    }

    @Override
    public void onBindViewHolder(SearchRestaurantAdapter.ViewHolder holder, int position) {
        holder.bind(searchRestaurants.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return searchRestaurants.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        Context context;
        private TextView tvSearchViewRestName;
        private ImageView ivRestaurantRowLogo;

        public ViewHolder(View restaurantView, Context context) {
            super(restaurantView);
            this.context = context;
            tvSearchViewRestName = (TextView) restaurantView.findViewById(R.id.tvSearchViewRestName);
            ivRestaurantRowLogo = (ImageView) restaurantView.findViewById(R.id.ivRestaurantRowLogo);
        }

        public void bind(final Restaurant restaurant, final OnItemClickListener listener){
            tvSearchViewRestName.setText(restaurant.getRestaurantName());
            Glide.with(context).load(restaurant.getRestaurant_logo_URL()).into(ivRestaurantRowLogo);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(restaurant);
                }
            });
        }
    }
}
