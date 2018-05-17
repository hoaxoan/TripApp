package com.winter.dreamhub.savedplaces;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import com.winter.dreamhub.api.service.model.Entity;

import java.util.List;

/**
 * Contains {@link BindingAdapter}s for the {@link Entity} list.
 */
public class SavedPlacesListBindings {
    @SuppressWarnings("unchecked")
    @BindingAdapter("app:items")
    public static void setItems(RecyclerView recyclerView, List<Entity> items) {
        SavePlacesAdapter adapter = (SavePlacesAdapter) recyclerView.getAdapter();
        if (adapter != null)
        {
            adapter.setList(items);
        }
    }
}
