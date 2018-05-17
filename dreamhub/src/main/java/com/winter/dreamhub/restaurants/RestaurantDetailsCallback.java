package com.winter.dreamhub.restaurants;

import com.winter.dreamhub.api.service.model.Entity;

public interface RestaurantDetailsCallback {
    /**
     * @param entity         The the entity
     */
    void onBookmarkClicked(Entity entity);
}
