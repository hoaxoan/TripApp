package com.winter.dreamhub.hotels;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.winter.dreamhub.R;
import com.winter.dreamhub.api.service.model.HotelBooking;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by hoaxoan on 11/8/2016.
 */

public class HotelBookingsAdapter extends RecyclerView.Adapter<HotelBookingsAdapter.ViewHolder> {

    private List<HotelBooking> hotelBookings;
    private Context mContext;

    public HotelBookingsAdapter(Context context, List<HotelBooking> hotelBookings) {
        mContext = context;
        this.hotelBookings = hotelBookings;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hotel_bookings_item, parent, false);

        ViewHolder holder = new ViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int position = holder.getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) return;
                final HotelBooking hotelBooking = hotelBookings.get(position);
                if (hotelBooking != null) {
                    String url = hotelBooking.webUrl;
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HotelBooking hotelBooking = hotelBookings.get(position);
        NumberFormat nf = NumberFormat.getInstance();
        holder.image.setImageResource(R.drawable.n2_ic_am_ac);
        holder.pricesFromWebsites.setText(hotelBooking.name);
        holder.metaPrice.setText(nf.format(hotelBooking.prices));
    }

    @Override
    public int getItemCount() {
        return hotelBookings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView metaPrice;
        public TextView pricesFromWebsites;

        public ViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.image);
            pricesFromWebsites = (TextView) view.findViewById(R.id.pricesFromWebsites);
            metaPrice = (TextView) view.findViewById(R.id.metaPrice);
        }
    }

}