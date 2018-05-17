package com.winter.dreamhub.searchs;

import com.winter.dreamhub.api.service.model.Category;

import java.util.List;

public interface FilterEventCallbacks {
    /**
     * @param categories        The the list category
     */
    void onFilterClicked(List<Category> categories);
}
