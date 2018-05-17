package com.winter.dreamhub.searchs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.chip.Chip;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.winter.dreamhub.R;
import com.winter.dreamhub.api.service.model.Category;

import java.util.ArrayList;
import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private final Context mHost;
    private final LayoutInflater mInflater;


    // State
    private List<Category> mItems;
    private List<Category> mSelectedItems = new ArrayList<>();

    private FilterEventCallbacks mCallbacks;

    public List<Category> getItems() {
        return mSelectedItems;
    }

    public FilterAdapter(@NonNull Context context,
                         FilterEventCallbacks callbacks,
                         @NonNull final List<Category> items) {
        mHost = context;
        mInflater = LayoutInflater.from(context);
        mItems = items;
        mCallbacks = callbacks;
        setHasStableIds(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final FilterViewHolder holder = new FilterViewHolder(mInflater.inflate(R.layout.item_event_filter, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FilterViewHolder viewHolder = (FilterViewHolder) holder;
        Category category = this.mItems.get(position);
        viewHolder.filterLabel.setChipText(category.name);
        viewHolder.filterLabel.setId((int) category.id);

        viewHolder.filterLabel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    ((Chip) compoundButton).setCloseIconEnabled(true);
                } else {
                    ((Chip) compoundButton).setCloseIconEnabled(false);
                }
                int id = ((Chip) compoundButton).getId();
                Category category = getCategoryById(id);
                if (category != null) {
                    deduplicateAndAdd(category, b);
                }

                if (mCallbacks == null) {
                    return;
                }
                mCallbacks.onFilterClicked(mSelectedItems);
            }
        });

        viewHolder.filterLabel.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Chip) view).setCloseIconEnabled(true);
                int id = ((Chip) view).getId();
                Category category = getCategoryById(id);
                if (category != null) {
                    deduplicateAndAdd(category, false);
                }

                if (mCallbacks == null) {
                    return;
                }
                mCallbacks.onFilterClicked(mSelectedItems);
            }
        });
    }

    /**
     * De-dupe as the same item can be returned by multiple feeds
     */
    private void deduplicateAndAdd(Category newItem, boolean selected) {
        if (selected) {
            final int count = mSelectedItems.size();
            boolean add = true;
            for (int i = 0; i < count; i++) {
                Category existingItem = mSelectedItems.get(i);
                if (existingItem.equals(newItem)) {
                    add = false;
                    break;
                }
            }
            if (add) {
                mSelectedItems.add(newItem);
            }
        } else {
            mSelectedItems.remove(newItem);
        }
    }

    public Category getCategoryById(int id) {
        final int count = getItemCount();
        for (int i = 0; i < count; i++) {
            Category existingItem = mItems.get(i);
            if (existingItem.id == id) {
                return existingItem;
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id;
    }

    private Category getItem(int position) {
        return mItems.get(position);
    }

    public void addAndResort(final List<Category> newItems) {
        this.mItems = newItems;
        notifyDataSetChanged();
    }

    public void clear() {
        this.mItems.clear();
        notifyDataSetChanged();
    }

    private static class FilterViewHolder extends RecyclerView.ViewHolder {

        final Chip filterLabel;

        public FilterViewHolder(View itemView) {
            super(itemView);
            filterLabel = (Chip) itemView.findViewById(R.id.filter_label);
        }
    }
}
