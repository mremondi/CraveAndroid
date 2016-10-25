package com.cravings.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.cravings.R;
import com.cravings.data.MenuItem;
import java.util.List;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by mremondi on 9/26/16.
 */
public class MenuSection extends StatelessSection {

    public interface OnItemClickListener {
        void onClick(MenuItem item);
    }

    List<MenuItem> items;
    OnItemClickListener listener;
    String title;

    public MenuSection(String title, List<MenuItem> list, OnItemClickListener listener){
        super(R.layout.menu_section_header, R.layout.menu_item_row);
        this.title = title;
        this.items = list;
        this.listener = listener;
    }
    @Override
    public int getContentItemsTotal() {
        return items.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new SectionItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {
        SectionItemViewHolder sectionItemViewHolder = (SectionItemViewHolder) holder;
        sectionItemViewHolder.tvMenuItemName.setText(items.get(position).getName());
        sectionItemViewHolder.tvMenuItemPrice.setText(items.get(position).getPrice());
        sectionItemViewHolder.tvMenuItemRating.setText("" + Math.round(items.get(position).getRating()
                / items.get(position).getNumberOfRatings() * 10.0)/10.0);
        sectionItemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(items.get(position));
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
        headerHolder.tvSectionHeader.setText(title);
    }


    public static class SectionItemViewHolder extends RecyclerView.ViewHolder{
        private final TextView tvMenuItemName;
        private final TextView tvMenuItemPrice;
        private final TextView tvMenuItemRating;

        public SectionItemViewHolder(View itemView) {
            super(itemView);
            tvMenuItemName = (TextView) itemView.findViewById(R.id.tvMenuViewItemName);
            tvMenuItemPrice = (TextView) itemView.findViewById(R.id.tvMenuViewItemPrice);
            tvMenuItemRating = (TextView) itemView.findViewById(R.id.tvMenuViewItemRating);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder{
        private final TextView tvSectionHeader;

        public HeaderViewHolder(View headerView) {
            super(headerView);
            tvSectionHeader = (TextView) headerView.findViewById(R.id.tvMenuViewSectionHeader);
        }
    }
}
