package com.cravings.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.cravings.R;
import java.util.ArrayList;


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
            tvMenuName.setText(menu);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(menu);
                }
            });
        }
    }
}
