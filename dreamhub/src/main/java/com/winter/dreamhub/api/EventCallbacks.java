package com.winter.dreamhub.api;

import com.winter.dreamhub.api.service.model.Entity;

public interface EventCallbacks {
    /**
     * @param entity         The the entity
     */
    void onEntityClicked(Entity entity);

    /**
     * @param entity         The the entity
     * @param isBookmark Whether the entity is bookmarked in the backing data
     */
    void onBookmarkClicked(Entity entity, boolean isBookmark);
}
