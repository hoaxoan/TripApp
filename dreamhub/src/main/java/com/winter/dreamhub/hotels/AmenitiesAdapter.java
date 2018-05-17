package com.winter.dreamhub.hotels;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.winter.dreamhub.R;
import com.winter.dreamhub.api.service.model.Amenity;

import java.util.List;

import static com.winter.dreamhub.hotels.AmenitiesActivity.AMENITY_ICON_RES_ID;

/**
 * Created by hoaxoan on 11/8/2016.
 */

public class AmenitiesAdapter extends RecyclerView.Adapter<AmenitiesAdapter.ViewHolder> {

    private List<Amenity> amenities;
    private Context mContext;

    public AmenitiesAdapter(Context context, List<Amenity> amenities) {
        mContext = context;
        this.amenities = amenities;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.amenities_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Amenity amenity = amenities.get(position);
        int itemId = (int) amenity.position;
        if (itemId < 0 || itemId >= AMENITY_ICON_RES_ID.length) {
            itemId = 0;
        }
        holder.image.setImageResource(AMENITY_ICON_RES_ID[itemId]);
        holder.title.setText(amenity.name);
    }

    @Override
    public int getItemCount() {
        return amenities.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;
        public TextView subTitle;

        public ViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.image);
            title = (TextView) view.findViewById(R.id.title);
            subTitle = (TextView) view.findViewById(R.id.subtitle);
        }
    }

}