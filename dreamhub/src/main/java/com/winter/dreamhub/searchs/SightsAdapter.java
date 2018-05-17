package com.winter.dreamhub.searchs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.winter.dreamhub.R;

import java.util.List;

/**
 * Created by hoaxoan on 11/12/2016.
 */

public class SightsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Immutable state
    private final Context mHost;

    private final LayoutInflater mInflater;

    private final boolean mCompactMode;

    private final int mColumns;

    // State
    private List<String> mItems;

    public SightsAdapter(@NonNull Context context,
                         @NonNull final List<String> items,
                         boolean compact,
                         int columns) {
        mHost = context;
        mInflater = LayoutInflater.from(context);
        mCompactMode = compact;
        mColumns = columns;
        mItems = items;
    }

    public static SightsAdapter createHorizontal(@NonNull Context context,
                                                 @NonNull final List<String> items) {
        return new SightsAdapter(context, items, true, -1);
    }

    public static SightsAdapter createVerticalGrid(@NonNull Context context,
                                                   @NonNull final List<String> items,
                                                   int columns) {
        return new SightsAdapter(context, items, false, columns);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //return createSightsViewHolder(parent);
        final SightViewHolder holder = new SightViewHolder(mInflater.inflate(R.layout.photo_carousel_item_small, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //bindSights((SightsViewHolder) holder, mItems.get(position), position);
        String imgUrl = this.mItems.get(position);
        Glide.with(this.mHost).load(imgUrl)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(((SightViewHolder) holder).image);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public int getSpanSize(int position) {
        if (mCompactMode) {
            return 1;
        } else {
            return mColumns;
        }
    }

    private void bindSights(SightsViewHolder holder,
                            final String title,
                            final int position) {
        holder.title.setText(title);
        if (mCompactMode) {
            bindCompact((CompactViewHolder) holder);
        } else {
            bindDetail((DetailViewHolder) holder);
        }
    }

    private void bindCompact(CompactViewHolder holder) {
        holder.subTitle.setText("Sub Title");
    }

    private void bindDetail(DetailViewHolder holder) {
        holder.description.setText("Description");
    }

    @NonNull
    private SightsViewHolder createSightsViewHolder(final ViewGroup parent) {
        final SightsViewHolder holder = mCompactMode ? new CompactViewHolder(
                mInflater.inflate(R.layout.sight_list_tile, parent, false)) :
                new DetailViewHolder(mInflater.inflate(
                        R.layout.sight_grid_tile, parent, false));

        if (mCompactMode) {
            ViewCompat.setImportantForAccessibility(holder.itemView,
                    ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO);
            ViewCompat.setImportantForAccessibility(holder.title,
                    ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO);
            ViewCompat.setImportantForAccessibility(((CompactViewHolder) holder).subTitle,
                    ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO);
        }
        return holder;
    }

    @NonNull
    private SightsViewHolder createSightsViewHolder2(final ViewGroup parent) {
        final SightsViewHolder holder = mCompactMode ? new CompactViewHolder(
                mInflater.inflate(R.layout.sight_list_tile, parent, false)) :
                new DetailViewHolder(mInflater.inflate(
                        R.layout.sight_grid_tile, parent, false));

        if (mCompactMode) {
            ViewCompat.setImportantForAccessibility(holder.itemView,
                    ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO);
            ViewCompat.setImportantForAccessibility(holder.title,
                    ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO);
            ViewCompat.setImportantForAccessibility(((CompactViewHolder) holder).subTitle,
                    ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO);
        }
        return holder;
    }

    private static class SightViewHolder extends RecyclerView.ViewHolder {

        final ImageView image;

        public SightViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image_view);
        }
    }

    private static abstract class SightsViewHolder extends RecyclerView.ViewHolder {

        final ImageView image;
        final TextView title;

        public SightsViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }

    private static class CompactViewHolder extends SightsViewHolder {
        final TextView subTitle;

        public CompactViewHolder(View itemView) {
            super(itemView);
            subTitle = (TextView) itemView.findViewById(R.id.sub_title);
        }
    }

    private static class DetailViewHolder extends SightsViewHolder {

        final TextView description;

        public DetailViewHolder(View itemView) {
            super(itemView);
            description = (TextView) itemView.findViewById(R.id.description);
        }
    }
}
