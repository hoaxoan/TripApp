package com.winter.dreamhub.restaurants;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.base.Preconditions;
import com.winter.dreamhub.R;
import com.winter.dreamhub.api.service.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoaxoan on 11/20/2016.
 */

public class RestaurantGroupsAdapter extends RecyclerView.Adapter<RestaurantGroupsAdapter.RestaurantGroupViewHolder> {

    private Context context;
    private  RestaurantOverviewAdapter adapter;
    private List<Category> items;
    private long tripId;

    public RestaurantGroupsAdapter(Context context,
                                   List<Category> categories,
                                   long tripId) {
        this.context = context;
        if (categories == null) {
            this.items = new ArrayList<Category>();
        } else {
            this.items = categories;
        }
        this.tripId = tripId;
    }

    @Override
    public RestaurantGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RestaurantGroupsAdapter.RestaurantGroupViewHolder holder = new RestaurantGroupsAdapter.RestaurantGroupViewHolder(this.context, LayoutInflater.from(parent
                .getContext()).inflate(R.layout.restaurant_group_layout, parent, false));

        holder.groupHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = holder.getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) return;
                final Category category = items.get(position);
                Intent intent = new Intent(context, RestaurantsListActivity.class);
                intent.putExtra("trip_id", (Long) Preconditions.checkNotNull(tripId));
                intent.putExtra("category_id", (Long) Preconditions.checkNotNull(category.id));
                intent.putExtra("category_name", (String) Preconditions.checkNotNull(category.name));
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RestaurantGroupViewHolder holder, int position) {
        final Category item = this.items.get(position);
        holder.groupTitle.setText(item.name);

        this.adapter = RestaurantOverviewAdapter.createHorizontal(context, this.tripId, item.id);
        holder.restaurantsRecyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public void addAndResort(List<Category> newItems) {
        addAll(newItems);
        //sort();
        notifyDataSetChanged();
    }

    private void addAll(List<Category> items) {
        items.addAll(items);
    }

    public static final class RestaurantGroupViewHolder
            extends RecyclerView.ViewHolder {
        private Context context;

        public LinearLayout groupHeader;
        public TextView groupTitle;

        public RecyclerView restaurantsRecyclerView;

        public RestaurantGroupViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;

            this.groupHeader = ((LinearLayout) itemView.findViewById(R.id.restaurant_group_header));
            this.groupTitle = ((TextView) itemView.findViewById(R.id.restaurant_group_title));

            this.restaurantsRecyclerView = (RecyclerView) itemView.findViewById(R.id.restaurants_recycler_view);

        }
    }
}
