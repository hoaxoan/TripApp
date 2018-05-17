package com.winter.dreamhub.restaurants;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.common.base.Preconditions;
import com.winter.dreamhub.R;
import com.winter.dreamhub.api.service.RestaurantsDataManager;
import com.winter.dreamhub.api.service.WinterService;
import com.winter.dreamhub.api.service.model.Entity;

import java.util.ArrayList;
import java.util.List;

import static com.winter.dreamhub.landmarks.LandmarksAdapter.DECIMAL_FORMAT;

/**
 * Created by hoaxoan on 11/20/2016.
 */

public class RestaurantOverviewAdapter extends RecyclerView.Adapter<RestaurantOverviewAdapter.RestaurantOverviewViewHolder>{

    // Immutable state
    private final Context mHost;

    private final LayoutInflater mInflater;

    private final boolean mCompactMode;

    private final int mColumns;

    // State
    private long categoryId;
    private long tripId;
    private List<Entity> mItems;

    public RestaurantOverviewAdapter(@NonNull Context context,
                                     long tripId,
                                     long categoryId,
                                     boolean compact,
                                     int columns) {
        mHost = context;
        mInflater = LayoutInflater.from(context);
        mCompactMode = compact;
        mColumns = columns;
        this.tripId = tripId;
        this.categoryId = categoryId;
        this.mItems = new ArrayList<>();
        this.bindEntities(this.tripId, this.categoryId);
    }

    public static RestaurantOverviewAdapter createHorizontal(@NonNull Context context,
                                                             long tripId,
                                                             long categoryId) {
        return new RestaurantOverviewAdapter(context, tripId, categoryId, true, -1);
    }

    public static RestaurantOverviewAdapter createVerticalGrid(@NonNull Context context,
                                                               long tripId,
                                                               long categoryId,
                                                               int columns) {
        return new RestaurantOverviewAdapter(context, tripId, categoryId, false, columns);
    }

    private void bindEntities(long tripId, long categoryId) {
        RestaurantsDataManager dataManager = new RestaurantsDataManager(mHost, tripId, categoryId) {
            @Override
            public void onDataLoaded(List<Entity> data) {
                mItems = data;
                notifyDataSetChanged();
            }
        };
        dataManager.loadData();
    }
    @Override
    public RestaurantOverviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RestaurantOverviewAdapter.RestaurantOverviewViewHolder holder = new RestaurantOverviewAdapter.RestaurantOverviewViewHolder(this.mHost, LayoutInflater.from(parent
                .getContext()).inflate(R.layout.restaurant_overview_layout, parent, false));

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = holder.getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) return;
                final Entity entity = mItems.get(position);
                Intent intent = new Intent(mHost, RestaurantDetailsActivity.class);
                intent.putExtra("id", Preconditions.checkNotNull(entity.id));
                intent.putExtra("name", Preconditions.checkNotNull(entity.name));
                mHost.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RestaurantOverviewViewHolder holder, int position) {
        Entity item = this.mItems.get(position);

        // Title
        holder.title.setText(item.name);

        // Images
        if (item.photo != null) {
            String imgUrl = WinterService.ENDPOINT + item.photo;
            Glide.with(this.mHost).load(imgUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.image);
        }

        // Rate
        holder.ratingBar.setRating(item.ratingHistogram);
        holder.averageRate.setText(DECIMAL_FORMAT.format(item.ratingHistogram));
        holder.totalEvaluations.setText(String.format("(%d)", item.reviewsCount));
    }

    @Override
    public int getItemCount() {
        return getDataItemCount();
    }

    public int getDataItemCount() {
        return mItems.size();
    }

    private Entity getItem(int position) {
        return mItems.get(position);
    }

    private int getLoadingMoreItemPosition() {
        return getItemCount() - 1;
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
        mItems.add(item);
    }


    public static final class RestaurantOverviewViewHolder
            extends RecyclerView.ViewHolder {

        private Context context;

        public LinearLayout itemLayout;
        public LinearLayout ratingLayout;
        public ImageView image;
        public TextView title;

        public TextView averageRate;
        public RatingBar ratingBar;
        public TextView totalEvaluations;

        public RestaurantOverviewViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;

            this.itemLayout = ((LinearLayout) itemView.findViewById(R.id.restaurant_item));
            this.ratingLayout = ((LinearLayout) itemView.findViewById(R.id.restaurant_rating_layout));
            this.image = ((ImageView) itemView.findViewById(R.id.photo));
            this.title = ((TextView) itemView.findViewById(R.id.restaurant_title));
            this.averageRate = ((TextView) itemView.findViewById(R.id.restaurant_average_rate));
            this.ratingBar = ((RatingBar) itemView.findViewById(R.id.restaurant_rating));
            this.totalEvaluations = ((TextView) itemView.findViewById(R.id.restaurant_total_evaluations));
        }
    }
}
