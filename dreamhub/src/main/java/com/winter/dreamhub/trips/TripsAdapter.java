package com.winter.dreamhub.trips;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.winter.dreamhub.R;
import com.winter.dreamhub.api.DataLoadingSubject;
import com.winter.dreamhub.api.service.WinterService;
import com.winter.dreamhub.api.service.model.Trip;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hoaxoan on 10/25/2016.
 */

public class TripsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements DataLoadingSubject.DataLoadingCallbacks {
    private static final int TYPE_TRIPS = 0;
    private static final int TYPE_LOADING_MORE = -1;

    private final Activity activity;
    private final LayoutInflater layoutInflater;

    private final
    @Nullable
    DataLoadingSubject dataLoading;
    private List<Trip> items;
    private boolean showLoadingMore = false;

    public TripsAdapter(Activity activity,
                        DataLoadingSubject dataLoading) {
        this.activity = activity;
        this.layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.dataLoading = dataLoading;
        dataLoading.registerCallback(this);
        items = new ArrayList<>();
        setHasStableIds(true);
    }


    public int getDataItemCount() {
        return items.size();
    }

    private int getLoadingMoreItemPosition() {
        return showLoadingMore ? getItemCount() - 1 : RecyclerView.NO_POSITION;
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

    @Override
    public int getItemViewType(int position) {
        if (position < getDataItemCount()
                && getDataItemCount() > 0) {
            return TYPE_TRIPS;
        }
        return TYPE_LOADING_MORE;
    }

    private Trip getItem(int position) {
        return items.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_TRIPS:
                return createTripsHolder(parent);
            case TYPE_LOADING_MORE:
                return new LoadingMoreHolder(
                        layoutInflater.inflate(R.layout.infinite_loading, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_TRIPS:
                bindTrips((Trip) getItem(position), (TripsHolder) holder);
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

    public void addAndResort(List<Trip> newItems) {
        deduplicateAndAdd(newItems);
        //sort();
        notifyDataSetChanged();
    }

    /**
     * De-dupe as the same item can be returned by multiple feeds
     */
    private void deduplicateAndAdd(List<Trip> newItems) {
        final int count = getDataItemCount();
        for (Trip newItem : newItems) {
            boolean add = true;
            for (int i = 0; i < count; i++) {
                Trip existingItem = getItem(i);
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

    private void add(Trip item) {
        items.add(item);
    }

   /* private void sort() {
        Collections.sort(items, comparator); // sort by weight
    }*/

    private void bindTrips(final Trip item, TripsHolder holder) {
        holder.title.setText(item.name);
        if (item.photo != null) {
            String imgUrl = WinterService.ENDPOINT + item.photo;
            Glide.with(activity).load(imgUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.image);
        }

        setupDownloadControls(holder.itemView, item.id, item.name);

    }

    private void bindLoadingViewHolder(LoadingMoreHolder holder) {
        // only show the infinite load progress spinner if there are already items in the
        // grid i.e. it's not the first item & data is being loaded
        holder.progress.setVisibility((holder.getAdapterPosition() > 0
                && dataLoading.isDataLoading()) ? View.VISIBLE : View.INVISIBLE);
    }


    private final void setupDownloadControls(View parentView, long tripId, String tripName) {
        LinearLayout linearLayout = (LinearLayout) parentView.findViewById(R.id.destination_download_controls);
        linearLayout.removeAllViews();

        View downloadView = this.layoutInflater.inflate(R.layout.trip_download_bar, linearLayout, false);
        DownloadControl downloadControl = new DownloadControl(tripId, tripName, downloadView);
        downloadControl.setup(0, 0);
        linearLayout.addView(downloadView);
    }

    @NonNull
    private TripsHolder createTripsHolder(ViewGroup parent) {
        final TripsHolder holder = new TripsHolder(layoutInflater.inflate(
                R.layout.trip_item, parent, false));

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = holder.getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) return;
                final Trip trip = items.get(position);
                ((TripsFragment.Listener) TripsAdapter.this.activity).onTripSelected(trip.id, trip.name);
            }
        });
        return holder;
    }

    /* package */ static class TripsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.trip_title_text)
        TextView title;
        @BindView(R.id.trip_dates)
        TextView dates;
        @BindView(R.id.trip_image)
        ImageView image;

        public TripsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /* package */ static class LoadingMoreHolder extends RecyclerView.ViewHolder {

        ProgressBar progress;

        public LoadingMoreHolder(View itemView) {
            super(itemView);
            progress = (ProgressBar) itemView;
        }

    }

    private final class DownloadControl {
        View parentView;
        SwitchCompat switchCompat;
        long tripId;
        String tripName;

        public DownloadControl(long tripId, String tripName, View parentView) {
            this.parentView = parentView;
            this.tripId = tripId;
            this.tripName = tripName;
            this.switchCompat = ((SwitchCompat) parentView.findViewById(R.id.download_switch));
            this.switchCompat.setText(TripsAdapter.this.activity.getString(R.string.destination_download_status, new Object[]{tripName}));

        }


        public final void setup(int status, int progress) {
            this.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    String downloadingStatus = String.format("%s (0%%)", new Object[]{DownloadControl.this.tripName});
                    TripsAdapter.DownloadControl.this.switchCompat.setText(TripsAdapter.this.activity.getString(R.string.destination_downloading_status, new Object[]{downloadingStatus}));
                    TripsAdapter.DownloadControl.this.switchCompat.setEnabled(true);
                }
            });
        }
    }

}
