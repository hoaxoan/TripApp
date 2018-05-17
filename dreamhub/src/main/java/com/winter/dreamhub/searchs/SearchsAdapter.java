package com.winter.dreamhub.searchs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.common.base.Preconditions;
import com.winter.dreamhub.R;
import com.winter.dreamhub.api.service.WinterService;
import com.winter.dreamhub.api.service.model.Entity;
import com.winter.dreamhub.hotels.HotelDetailsActivity;
import com.winter.dreamhub.restaurants.RestaurantDetailsActivity;
import com.winter.dreamhub.landmarks.LandmarkDetailsActivity;
import com.winter.dreamhub.api.DataLoadingSubject;
import com.winter.dreamhub.util.TypefaceSpan;
import com.winter.dreamhub.util.Typefaces;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoaxoan on 12/17/2016.
 */

public class SearchsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
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

    public SearchsAdapter(Activity context,
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
                bindSearchsViewHolder((Entity) getItem(position), (SearchsViewHolder) holder);
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

    private void bindSearchsViewHolder(final Entity item, SearchsViewHolder holder) {
        holder.entityName.setText(item.name);
        holder.entitySummary.setText(item.description);

        if (item.photo != null) {
            String imgUrl = WinterService.ENDPOINT + item.photo;
            Glide.with(this.context).load(imgUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.entityImage);
        }

    }

    private void bindLoadingViewHolder(LoadingMoreHolder holder) {
        // only show the infinite load progress spinner if there are already items in the
        // grid i.e. it's not the first item & data is being loaded
        holder.progress.setVisibility((holder.getAdapterPosition() > 0
                && dataLoading.isDataLoading()) ? View.VISIBLE : View.INVISIBLE);
    }

    @NonNull
    private SearchsViewHolder createSearchsHolder(ViewGroup parent) {
        final SearchsViewHolder holder = new SearchsViewHolder(this.context, LayoutInflater.from(parent
                .getContext()).inflate(R.layout.landmark_item, parent, false));
        holder.entityImage.setOnClickListener(new View.OnClickListener() {
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

    /* package */ public static final class SearchsViewHolder
            extends RecyclerView.ViewHolder {
        private Context context;

        public ImageView entityImage;
        public TextView entityName;

        // Info
        public View entityInfoView;
        public LinearLayout entityInfoFirstLine;
        public LinearLayout entityInfoHolder;
        public ImageView fullStarImage;
        public ImageView emptyStarImage;

        // RateView
        private final View ratingView;
        private final RatingBar landmarkRatingBar;
        private final TextView landmarkAverageRate;
        private final TextView landmarkTotalEvaluations;

        // DateTime
        private final View openHoursView;
        public TextView distanceText;
        private final TextView landmarkOpenHours;

        // Summary
        private final View summaryView;
        public TextView entitySummary;

        public SearchsViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;

            this.entityImage = ((ImageView) itemView.findViewById(R.id.entity_image));
            this.entityName = ((TextView) itemView.findViewById(R.id.entity_name));

            // Info
            this.entityInfoHolder = ((LinearLayout) itemView.findViewById(R.id.entity_info_holder));
            this.entityInfoView = LayoutInflater.from(this.context).inflate(R.layout.landmark_item_info, this.entityInfoHolder, false);
            this.entityInfoFirstLine = ((LinearLayout) this.entityInfoView.findViewById(R.id.entity_info_first_line));
            this.fullStarImage = ((ImageView) this.entityInfoView.findViewById(R.id.entity_full_star));
            this.emptyStarImage = ((ImageView) this.entityInfoView.findViewById(R.id.entity_empty_star));

            // RateView
            this.ratingView = LayoutInflater.from(this.context).inflate(R.layout.landmark_item_rating, null, false);
            this.landmarkRatingBar = ((RatingBar) this.ratingView.findViewById(R.id.landmark_rating));
            this.landmarkAverageRate = ((TextView) this.ratingView.findViewById(R.id.landmark_average_rate));
            this.landmarkTotalEvaluations = ((TextView) this.ratingView.findViewById(R.id.landmark_total_evaluations));

            // DateTime
            this.openHoursView = LayoutInflater.from(this.context).inflate(R.layout.landmark_item_open_hours, null, false);
            this.distanceText = ((TextView) this.openHoursView.findViewById(R.id.distance));
            this.landmarkOpenHours = ((TextView) this.openHoursView.findViewById(R.id.landmark_open_hours));

            // Summary
            this.summaryView = LayoutInflater.from(this.context).inflate(R.layout.landmark_item_summary, null, false);
            this.entitySummary = ((TextView) this.summaryView.findViewById(R.id.entity_summary_text));

            addViews();
        }

        public final void addViews() {
            this.entityInfoHolder.removeAllViews();
            this.entityInfoFirstLine.removeAllViews();

            // Status
            updateStarVisibility(0);
            this.entityInfoHolder.addView(this.entityInfoView);

            // Rate
            this.landmarkRatingBar.setRating((float) 4.4);
            this.landmarkAverageRate.setText(DECIMAL_FORMAT.format(4.4));
            this.landmarkTotalEvaluations.setText(this.context.getResources().getQuantityString(R.plurals.reviews, 468, new Object[]{Integer.valueOf(468)}));
            this.entityInfoFirstLine.addView(this.ratingView);

            // DateTime
            SpannableStringBuilder openHours = new SpannableStringBuilder();
            openHours.append(this.context.getString(R.string.place_open_until).toUpperCase());
            openHours.append(" 11:9PM");
            openHours.setSpan(new ForegroundColorSpan(this.context.getResources().getColor(R.color.quantum_googgreen500)), 0, openHours.length(), 0);
            openHours.setSpan(new TypefaceSpan(Typefaces.getTypeface(this.context, "ROBOTO_MEDIUM", 1)), 0, openHours.length(), 0);

            String distance = this.context.getResources().getString(R.string.distance_to_entity_text_km, new Object[]{DECIMAL_FORMAT.format(10)});

            this.distanceText.setText((CharSequence) distance);
            this.landmarkOpenHours.setText(openHours);
            //this.entityInfoHolder.addView(this.openHoursView, -1, -2);

            // Summary
            this.entityInfoHolder.addView(this.summaryView, -1, -2);

        }

        public void updateStarVisibility(int status) {
            if (status == 0) {
                this.fullStarImage.setVisibility(View.GONE);
                this.emptyStarImage.setVisibility(View.VISIBLE);
            } else {
                this.fullStarImage.setVisibility(View.VISIBLE);
                this.emptyStarImage.setVisibility(View.GONE);
            }
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
