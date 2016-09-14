package com.cravings.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_row_restaurant, parent, false);
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
        private Context context;

        private TextView tvSearchViewRestName;

        public ViewHolder(View restaurantView, Context context) {
            super(restaurantView);
            tvSearchViewRestName = (TextView) restaurantView.findViewById(R.id.tvSearchViewRestName);
            this.context = context;
        }

        public void bind(final Restaurant restaurant, final OnItemClickListener listener){
            tvSearchViewRestName.setText(restaurant.getRestaurantName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(restaurant);
                }
            });
        }
    }
}
