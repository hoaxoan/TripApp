package com.winter.dreamhub.api.model;

import java.util.List;

/**
 * Created by hoaxoan on 8/26/2016.
 */
public class Settings {
    private List<Setting> items;
    private String nextPageToken;

    public List<Setting> getItems() {
        return items;
    }

    public void setItems(List<Setting> items) {
        this.items = items;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }
}
