package com.cravings.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.cravings.R;
import com.cravings.data.MenuItem;
import java.util.List;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{

    public interface OnItemClickListener {
        void onClick(MenuItem item);
    }


    private Context context;
    private List<MenuItem> searchItems;
    private final OnItemClickListener listener;

    public SearchAdapter(List<MenuItem> list, Context context, OnItemClickListener listener){
        this.searchItems = list;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new ViewHolder(rowView, context);
    }

    @Override
    public void onBindViewHolder(SearchAdapter.ViewHolder holder, int position) {
        holder.bind(searchItems.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return searchItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        private String objectID;
        private TextView tvSearchRowItemName;
        private TextView tvSearchRowItemPrice;
        private TextView tvSearchRowItemRating;
        private TextView tvSearchItemDescription;
        private Context context;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            tvSearchRowItemName = (TextView) itemView.findViewById(R.id.tvItemRowItemName);
            tvSearchRowItemPrice = (TextView) itemView.findViewById(R.id.tvItemRowPrice);
            tvSearchRowItemRating = (TextView) itemView.findViewById(R.id.tvItemRowRating);
            tvSearchItemDescription = (TextView) itemView.findViewById(R.id.tvItemRowItemDescription);
            this.context = context;
        }

        public void setObjectID(String id){
            this.objectID = id;
        }

        public void bind(final MenuItem item, final OnItemClickListener listener){
            tvSearchRowItemName.setText(item.getName());
            tvSearchItemDescription.setText(item.getDescription());
            tvSearchRowItemPrice.setText("$" + item.getPrice());
            if (item.getRating() > 3.5) {
                tvSearchRowItemRating.getBackground().setColorFilter(
                        new PorterDuffColorFilter(context.getResources().getColor(R.color.ratingBubbleGreen),
                                PorterDuff.Mode.SRC_ATOP));
            }
            else if (item.getRating() > 2.5 && Float.valueOf(item.getPrice()) <= 3.5){
                tvSearchRowItemRating.getBackground().setColorFilter(
                        new PorterDuffColorFilter(context.getResources().getColor(R.color.ratingBubbleOrange),
                                PorterDuff.Mode.SRC_ATOP));
            }
            else{
                tvSearchRowItemRating.getBackground().setColorFilter(
                        new PorterDuffColorFilter(context.getResources().getColor(R.color.ratingBubbleRed),
                                PorterDuff.Mode.SRC_ATOP));
            }
            if (Float.isNaN(item.getRating() / item.getNumberOfRatings()) ){
                tvSearchRowItemRating.setText("No Ratings");
            }
            tvSearchRowItemRating.setText("" + Math.round(item.getRating() / item.getNumberOfRatings() * 10.0)/10.0);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(item);
                }
            });
        }
    }
}
