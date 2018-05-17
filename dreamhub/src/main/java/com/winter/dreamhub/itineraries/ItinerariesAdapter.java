package com.winter.dreamhub.itineraries;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.winter.dreamhub.R;
import com.winter.dreamhub.searchs.SightsAdapter;
import com.winter.dreamhub.widget.ItinerarySummaryView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoaxoan on 11/8/2016.
 */

public class ItinerariesAdapter extends RecyclerView.Adapter<ItinerariesAdapter.ItinerariesViewHolder> {

    private Context context;
    private List<String> datas = new ArrayList<>();

    public ItinerariesAdapter(Context context) {
        this.context = context;
        this.datas.add("Ba Dinh & Around");
        this.datas.add("Hoan Kiem & Around");

    }

    @Override
    public ItinerariesAdapter.ItinerariesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ItinerariesViewHolder holder = new ItinerariesViewHolder(LayoutInflater.from(parent
                .getContext()).inflate(R.layout.itinerary_card, parent, false));

        return holder;
    }

    @Override
    public void onBindViewHolder(ItinerariesAdapter.ItinerariesViewHolder holder, int position) {

        String title = this.datas.get(position);
        holder.itineraryTitle.setText(title);
        holder.itineraryText.setText("5 Sights");

        // Sights
        List<String> items = new ArrayList<>();
        items.add("https://tinhte.vn/store/2016/11/xengallery_photos_l_76878_9f75bd4bd3eeb13d24088e0a826a27ff.jpg");
        items.add("https://tinhte.vn/store/2016/11/xengallery_photos_l_76862_57e6b8e13b777c7abd719735d34991e5.jpg");
        items.add("https://tinhte.vn/store/2016/11/xengallery_photos_l_76850_efc6cdc4d9bd4abfae484d6de9537da9.jpg");
        items.add("https://tinhte.vn/store/2016/11/xengallery_photos_l_76851_806c73110c81bf8d611fc3fff97f23d9.jpg");
        items.add("https://tinhte.vn/store/2016/11/xengallery_photos_l_76824_f68db9094b9347b03bd870971e9f9c64.jpg");
        items.add("https://tinhte.vn/store/2016/11/xengallery_photos_l_76912_24685e0514d3a21ad55208c8d1febeb7.jpg");
        SightsAdapter sightsAdapter = SightsAdapter.createHorizontal(context, items);
        holder.sightList.setAdapter(sightsAdapter);


        holder.openingHoursWarning1.setText(this.context.getString(R.string.itinerary_open_hours_open_everyday));
        holder.openingHoursWarning2.setText(this.context.getString(R.string.itinerary_open_hours_open_everyday));

        ItineraryEventAdapter adapter = new ItineraryEventAdapter(context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        holder.summaryRecyclerView2.setLayoutManager(mLayoutManager);
        holder.summaryRecyclerView2.setAdapter(adapter);
        holder.summaryRecyclerView2.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return this.datas.size();
    }

    public static class ItinerariesViewHolder extends RecyclerView.ViewHolder {

        public TextView itineraryTitle;
        public TextView itineraryText;

        public RecyclerView sightList;

        public ItinerarySummaryView itinerarySummaryView;
        public TextView openingHoursWarning1;
        public TextView openingHoursWarning2;

        public RecyclerView summaryRecyclerView1;
        public RecyclerView summaryRecyclerView2;

        public ItinerariesViewHolder(View itemView) {
            super(itemView);
            itineraryTitle = (TextView) itemView.findViewById(R.id.itinerary_title);
            itineraryText = (TextView) itemView.findViewById(R.id.itinerary_text);
            sightList = (RecyclerView) itemView.findViewById(R.id.sights_list);

            itinerarySummaryView = (ItinerarySummaryView) itemView.findViewById(R.id.summary_view);
            openingHoursWarning1 = (TextView) itinerarySummaryView.firstView.findViewById(R.id.opening_hours_warning);
            openingHoursWarning2 = (TextView) itinerarySummaryView.secondView.findViewById(R.id.opening_hours_warning);
            summaryRecyclerView1 = (RecyclerView) itinerarySummaryView.firstView.findViewById(R.id.itinerary_summary_recycler_view);
            summaryRecyclerView2 = (RecyclerView) itinerarySummaryView.secondView.findViewById(R.id.itinerary_summary_recycler_view);
        }
    }
}
