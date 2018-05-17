package com.winter.dreamhub.searchs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.common.base.Preconditions;
import com.winter.dreamhub.R;
import com.winter.dreamhub.api.DataLoadingSubject;
import com.winter.dreamhub.api.service.WinterService;
import com.winter.dreamhub.api.service.model.Entity;
import com.winter.dreamhub.hotels.HotelDetailsActivity;
import com.winter.dreamhub.restaurants.RestaurantDetailsActivity;
import com.winter.dreamhub.landmarks.LandmarkDetailsActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoaxoan on 12/17/2016.
 */

public class QuickSearchsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements DataLoadingSubject.DataLoadingCallbacks {
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.0");

    private static final int TYPE_SEARCHS = 0;
    private static final int TYPE_LOADING_MORE = -1;

    private Activity context;
    private final LayoutInflater layoutInflater;
    private List<Entity> items;
    private boolean showLoadingMore = false;
    private final
    @Nullable
    DataLoadingSubject dataLoading;

    public QuickSearchsAdapter(Activity context,
                               DataLoadingSubject dataLoading) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.dataLoading = dataLoading;
        this.items = new ArrayList<>();
        setHasStableIds(true);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < getDataItemCount()
                && getDataItemCount() > 0) {
            return TYPE_SEARCHS;
        }
        return TYPE_LOADING_MORE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_SEARCHS:
                return createSearchsHolder(parent);
            case TYPE_LOADING_MORE:
                return new LoadingMoreHolder(
                        layoutInflater.inflate(R.layout.infinite_loading, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_SEARCHS:
                bindSearchsViewHolder((Entity) getItem(position), (QuickSearchsViewHolder) holder);
                break;
            case TYPE_LOADING_MORE:
                bindLoadingViewHolder((LoadingMoreHolder) holder);
                break;
        }
    }

    @Override
    public long getItemId(int position) {
        if (getItemViewType(position) == TYPE_LOADING_MORE) {
            return -1L;
        }
        return getItem(position).id;
    }

    @Override
    public int getItemCount() {
        return getDataItemCount() + (showLoadingMore ? 1 : 0);
    }

    private Entity getItem(int position) {
        return items.get(position);
    }

    public int getDataItemCount() {
        return items.size();
    }

    private int getLoadingMoreItemPosition() {
        return showLoadingMore ? getItemCount() - 1 : RecyclerView.NO_POSITION;
    }

    public void addAndResort(List<Entity> newItems) {
        deduplicateAndAdd(newItems);
        //sort();
        notifyDataSetChanged();
    }

    /**
     * De-dupe as the same item can be returned by multiple feeds
     */
    private void deduplicateAndAdd(List<Entity> newItems) {
        final int count = getDataItemCount();
        for (Entity newItem : newItems) {
            boolean add = true;
            for (int i = 0; i < count; i++) {
                Entity existingItem = getItem(i);
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

    private void add(Entity item) {
        items.add(item);
    }

    @Override
    public void dataStartedLoading() {
        if (showLoadingMore) return;
        showLoadingMore = true;
        notifyItemInserted(getLoadingMoreItemPosition());
    }

    @Override
    public void dataFinishedLoading() {
        if (!showLoadingMore) return;
        final int loadingPos = getLoadingMoreItemPosition();
        showLoadingMore = false;
        notifyItemRemoved(loadingPos);
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    private void bindSearchsViewHolder(final Entity item, QuickSearchsViewHolder holder) {
        holder.text1.setText(item.name);
        holder.text2.setText(item.description);
        holder.icon1.setImageResource(R.drawable.ic_access_time);
        holder.icon1.setColorFilter(R.color.blue);

        holder.text1.setVisibility(View.VISIBLE);
        holder.text2.setVisibility(View.VISIBLE);
        holder.icon1.setVisibility(View.VISIBLE);
    }

    private void bindLoadingViewHolder(LoadingMoreHolder holder) {
        // only show the infinite load progress spinner if there are already items in the
        // grid i.e. it's not the first item & data is being loaded
        holder.progress.setVisibility((holder.getAdapterPosition() > 0
                && dataLoading.isDataLoading()) ? View.VISIBLE : View.INVISIBLE);
    }

    @NonNull
    private QuickSearchsViewHolder createSearchsHolder(ViewGroup parent) {
        final QuickSearchsViewHolder holder = new QuickSearchsViewHolder(this.context, LayoutInflater.from(parent
                .getContext()).inflate(R.layout.search_dropdown_item_icons_2line, parent, false));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int position = holder.getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) return;
                final Entity entity = items.get(position);

                startActivityDetails(entity);
            }
        });
        return holder;
    }

    private void startActivityDetails(Entity entity) {
        Intent intent = null;
        if(entity.type == WinterService.TYPE_LANDMARKS){
            intent = new Intent(context, LandmarkDetailsActivity.class);
        } else if(entity.type == WinterService.TYPE_HOTELS){
            intent = new Intent(context, HotelDetailsActivity.class);
        } else if(entity.type == WinterService.TYPE_RESTAURANTS){
            intent = new Intent(context, RestaurantDetailsActivity.class);
        }

        if(intent == null){
            return;
        }

        intent.putExtra("id", Preconditions.checkNotNull(entity.id));
        intent.putExtra("name", Preconditions.checkNotNull(entity.name));
        context.startActivity(intent);
    }

    /* package */ public static final class QuickSearchsViewHolder
            extends RecyclerView.ViewHolder {
        private Context context;

        public ImageView icon1;
        public TextView text1;
        public ImageView icon2;
        public TextView text2;
        public ImageView editQuery;


        public QuickSearchsViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;

            this.icon1 = ((ImageView) itemView.findViewById(android.R.id.icon1));
            this.text1 = ((TextView) itemView.findViewById(android.R.id.text1));
            this.icon2 = ((ImageView) itemView.findViewById(android.R.id.icon2));
            this.text2 = ((TextView) itemView.findViewById(android.R.id.text2));
            this.editQuery = ((ImageView) itemView.findViewById(R.id.edit_query));
        }
    }
    /* package */ static class LoadingMoreHolder extends RecyclerView.ViewHolder {

        ProgressBar progress;

        public LoadingMoreHolder(View itemView) {
            super(itemView);
            progress = (ProgressBar) itemView;
        }

    }


}
