package com.winter.dreamhub.hotels;

import com.winter.dreamhub.api.service.model.Entity;

public interface HotelDetailsCallback {
    /**
     * @param entity         The the entity
     */
    void onBookmarkClicked(Entity entity);
}
