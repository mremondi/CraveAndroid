package com.cravings.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cravings.R;
import com.cravings.data.MenuItem;
import com.cravings.data.ModelObject;
import java.util.List;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{

    public interface OnItemClickListener {
        void onClick(ModelObject item);
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
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_row, parent, false);
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
        private TextView tvItemName;
        private Context context;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            tvItemName = (TextView) itemView.findViewById(R.id.tvItemName);
            this.context = context;
        }

        public void setObjectID(String id){
            this.objectID = id;
        }

        public void bind(final ModelObject item, final OnItemClickListener listener){
            tvItemName.setText(item.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(item);
                }
            });
        }


    }
}
