package com.cravings.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.cravings.R;
import com.cravings.data.Menu;
import com.cravings.data.MenuItem;

import java.util.ArrayList;
import java.util.List;


public class MenuItemRecyclerAdapter extends RecyclerView.Adapter<MenuItemRecyclerAdapter.ViewHolder>{

    public interface OnItemClickListener {
        void onClick(MenuItem item);
    }

    private Context context;
    private ArrayList<MenuItem> items = new ArrayList<>();
    private final OnItemClickListener listener;

    public MenuItemRecyclerAdapter(List<MenuItem> list, Context context, OnItemClickListener listener){
        for (MenuItem item: list){
            this.items.add(item);
        }
        this.context = context;
        this.listener = listener;
    }

    @Override
    public MenuItemRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_row, parent, false);
        return new ViewHolder(rowView, context);
    }

    @Override
    public void onBindViewHolder(MenuItemRecyclerAdapter.ViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        private TextView tvMenuItemName;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            tvMenuItemName = (TextView) itemView.findViewById(R.id.tvMenuViewItemName);
        }

        public void bind(final MenuItem item, final OnItemClickListener listener){
            tvMenuItemName.setText(item.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(item);
                }
            });
        }
    }
}
