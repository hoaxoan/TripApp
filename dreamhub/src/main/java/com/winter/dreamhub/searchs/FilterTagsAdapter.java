package com.winter.dreamhub.searchs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.winter.dreamhub.R;
import com.winter.dreamhub.api.service.model.Category;

import java.util.List;

public class FilterTagsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mHost;
    private final LayoutInflater mInflater;


    // State
    private List<Category> mItems;

    public FilterTagsAdapter(@NonNull Context context,
                             @NonNull final List<Category> items) {
        mHost = context;
        mInflater = LayoutInflater.from(context);
        mItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final FilterTagsViewHolder holder = new FilterTagsViewHolder(mInflater.inflate(R.layout.item_filter_chip, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FilterTagsViewHolder viewHolder = (FilterTagsViewHolder) holder;
        Category category = this.mItems.get(position);
        viewHolder.title.setText(category.name);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void addAndResort(List<Category> newItems) {
        this.mItems = newItems;
        notifyDataSetChanged();
    }

    public void clear() {
        this.mItems.clear();
        notifyDataSetChanged();
    }

    private static class FilterTagsViewHolder extends RecyclerView.ViewHolder {

        final TextView title;

        public FilterTagsViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.filter_label);
            title.setSelected(true);
            title.setPressed(true);
        }
    }
}
