package com.winter.dreamhub.itineraries;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.winter.dreamhub.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoaxoan on 11/8/2016.
 */

public class ItineraryEventAdapter extends RecyclerView.Adapter<ItineraryEventAdapter.ItineraryEventViewHolder> {

    private Context context;
    private List<String> datas = new ArrayList<>();

    public ItineraryEventAdapter(Context context) {
        this.context = context;
        this.datas.add("Ba Dinh Square");
        this.datas.add("Chua Mot Cot");

    }

    @Override
    public ItineraryEventAdapter.ItineraryEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ItineraryEventAdapter.ItineraryEventViewHolder holder = new ItineraryEventAdapter.ItineraryEventViewHolder(LayoutInflater.from(parent
                .getContext()).inflate(R.layout.itinerary_item, parent, false));

        return holder;
    }

    @Override
    public void onBindViewHolder(ItineraryEventAdapter.ItineraryEventViewHolder holder, int position) {

        String title = this.datas.get(position);
        holder.travelMode.setImageResource(R.drawable.quantum_ic_directions_walk_grey600_18);
        holder.travelDuration.setText("5 min by foot");

        holder.itemIndex.setImageResource(R.drawable.ic_itinerary_poi_1);
        holder.name.setText(title);

        holder.openHours.setText("OPEN UNTIL 11:59 PM");
        holder.visitDuration.setText("Visitors typically stay 30 min");

    }

    @Override
    public int getItemCount() {
        return this.datas.size();
    }

    public static class ItineraryEventViewHolder extends RecyclerView.ViewHolder {

        public ImageView travelMode;
        public TextView travelDuration;

        public ImageView itemIndex;
        public TextView name;

        public TextView openHours;
        public TextView visitDuration;

        public ItineraryEventViewHolder(View itemView) {
            super(itemView);
            travelMode = (ImageView) itemView.findViewById(R.id.travel_mode);
            travelDuration = (TextView) itemView.findViewById(R.id.travel_duration);

            itemIndex = (ImageView) itemView.findViewById(R.id.item_index);
            name = (TextView) itemView.findViewById(R.id.name);
            openHours = (TextView) itemView.findViewById(R.id.open_hours);
            visitDuration = (TextView) itemView.findViewById(R.id.visit_duration);

        }
    }
}
