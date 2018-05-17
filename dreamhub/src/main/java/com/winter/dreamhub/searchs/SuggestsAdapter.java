package com.winter.dreamhub.searchs;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.winter.dreamhub.R;
import com.winter.dreamhub.api.service.model.EntitySnippet;

import java.util.List;

/**
 * Created by hoaxoan on 12/17/2016.
 */

public class SuggestsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_SUGGESTS = 0;
    private static final int TYPE_SEARCHS = 1;

    private Activity context;
    private final LayoutInflater layoutInflater;
    private List<EntitySnippet> items;
    private boolean showLoadingMore = false;
    private int searchType;

    public SuggestsAdapter(Activity context,
                           int searchType,
                           List<EntitySnippet> items) {
        this.context = context;
        this.searchType = searchType;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
        setHasStableIds(true);
    }

    @Override
    public int getItemViewType(int position) {
        if (this.searchType == TYPE_SUGGESTS) {
            return TYPE_SUGGESTS;
        }
        return TYPE_SEARCHS;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_SUGGESTS:
                return createSuggestsHolder(parent);
            case TYPE_SEARCHS:
                return createSearchsHolder(parent);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_SUGGESTS:
                bindSuggests((EntitySnippet) getItem(position), (SuggestsAdapter.SuggestsViewHolder) holder);
                break;
            case TYPE_SEARCHS:
                bindSearchs((EntitySnippet) getItem(position), (SuggestsAdapter.SearchsViewHolder) holder);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return getDataItemCount() + (showLoadingMore ? 1 : 0);
    }

    private EntitySnippet getItem(int position) {
        return items.get(position);
    }

    public int getDataItemCount() {
        return items.size();
    }

    private int getLoadingMoreItemPosition() {
        return showLoadingMore ? getItemCount() - 1 : RecyclerView.NO_POSITION;
    }

    public void addAndResort(List<EntitySnippet> newItems) {
        deduplicateAndAdd(newItems);
        //sort();
        notifyDataSetChanged();
    }

    /**
     * De-dupe as the same item can be returned by multiple feeds
     */
    private void deduplicateAndAdd(List<EntitySnippet> newItems) {
        final int count = getDataItemCount();
        for (EntitySnippet newItem : newItems) {
            boolean add = true;
            for (int i = 0; i < count; i++) {
                EntitySnippet existingItem = getItem(i);
                if (existingItem.equals(newItem)) {
                    add = false;
                    break;
                }
            }
            if (add) {
                add(newItem);
            }
        }
    }

    private void add(EntitySnippet item) {
        items.add(item);
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    private void bindSuggests(final EntitySnippet item, SuggestsAdapter.SuggestsViewHolder holder) {
        holder.text1.setText(item.name);
        holder.text2.setText(item.description);
        holder.icon1.setImageResource(R.drawable.ic_access_time);
        holder.icon1.setColorFilter(R.color.blue);

        holder.text1.setVisibility(View.VISIBLE);
        holder.text2.setVisibility(View.VISIBLE);
        holder.icon1.setVisibility(View.VISIBLE);
    }

    private void bindSearchs(final EntitySnippet item, SuggestsAdapter.SearchsViewHolder holder) {
        holder.text1.setText(item.name);
        holder.text2.setText(item.description);
        holder.icon1.setImageResource(R.drawable.ic_access_time);
        holder.icon1.setColorFilter(R.color.lighter_gray);

        holder.text1.setVisibility(View.VISIBLE);
        holder.text2.setVisibility(View.VISIBLE);
        holder.icon1.setVisibility(View.VISIBLE);
    }

    @NonNull
    private SuggestsAdapter.SuggestsViewHolder createSuggestsHolder(ViewGroup parent) {
        final SuggestsAdapter.SuggestsViewHolder holder = new SuggestsAdapter.SuggestsViewHolder(this.context, LayoutInflater.from(parent
                .getContext()).inflate(R.layout.search_dropdown_item_icons_2line, parent, false));

        return holder;
    }

    @NonNull
    private SuggestsAdapter.SearchsViewHolder createSearchsHolder(ViewGroup parent) {
        final SuggestsAdapter.SearchsViewHolder holder = new SuggestsAdapter.SearchsViewHolder(this.context, LayoutInflater.from(parent
                .getContext()).inflate(R.layout.search_dropdown_item_icons_2line, parent, false));

        return holder;
    }

    /* package */ public static final class SuggestsViewHolder
            extends RecyclerView.ViewHolder {
        private Context context;

        public ImageView icon1;
        public TextView text1;
        public ImageView icon2;
        public TextView text2;
        public ImageView editQuery;


        public SuggestsViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;

            this.icon1 = ((ImageView) itemView.findViewById(android.R.id.icon1));
            this.text1 = ((TextView) itemView.findViewById(android.R.id.text1));
            this.icon2 = ((ImageView) itemView.findViewById(android.R.id.icon2));
            this.text2 = ((TextView) itemView.findViewById(android.R.id.text2));
            this.editQuery = ((ImageView) itemView.findViewById(R.id.edit_query));
        }
    }

    /* package */ public static final class SearchsViewHolder
            extends RecyclerView.ViewHolder {
        private Context context;

        public ImageView icon1;
        public TextView text1;
        public ImageView icon2;
        public TextView text2;
        public ImageView editQuery;


        public SearchsViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;

            this.icon1 = ((ImageView) itemView.findViewById(android.R.id.icon1));
            this.text1 = ((TextView) itemView.findViewById(android.R.id.text1));
            this.icon2 = ((ImageView) itemView.findViewById(android.R.id.icon2));
            this.text2 = ((TextView) itemView.findViewById(android.R.id.text2));
            this.editQuery = ((ImageView) itemView.findViewById(R.id.edit_query));
        }

    }
}
