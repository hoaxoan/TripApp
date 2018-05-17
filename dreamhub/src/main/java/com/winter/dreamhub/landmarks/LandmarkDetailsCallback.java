package com.winter.dreamhub.landmarks;

import com.winter.dreamhub.api.service.model.Entity;

public interface LandmarkDetailsCallback {
    /**
     * @param entity         The the entity
     */
    void onBookmarkClicked(Entity entity);
}
