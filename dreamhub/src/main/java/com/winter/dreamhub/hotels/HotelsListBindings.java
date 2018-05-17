package com.winter.dreamhub.hotels;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import com.winter.dreamhub.api.service.model.Entity;

import java.util.List;

/**
 * Contains {@link BindingAdapter}s for the {@link Entity} list.
 */
public class HotelsListBindings {
    @SuppressWarnings("unchecked")
    @BindingAdapter("app:hotelItems")
    public static void setHotelItems(RecyclerView recyclerView, List<Entity> items) {
        HotelsAdapter adapter = (HotelsAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.addAndResort(items);
        }
    }
}
