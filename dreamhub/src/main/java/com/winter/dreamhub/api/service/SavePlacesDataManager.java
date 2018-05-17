package com.winter.dreamhub.api.service;

import android.content.Context;

import com.winter.dreamhub.api.PaginatedDataManager;
import com.winter.dreamhub.api.service.model.Entity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hoaxoan on 12/11/2016.
 */

public abstract class SavePlacesDataManager extends PaginatedDataManager<List<Entity>> {
    private Call loadCall;
    private long tripId;

    public SavePlacesDataManager(Context context, long tripId) {
        super(context);
        this.tripId = tripId;
    }

    private void loadEntities(final int page) {
        long userId = getWinterPrefs().getUser() != null ? getWinterPrefs().getUser().id : 0;
        loadCall = getWinterApi()
                .getSavePlaces(this.tripId, userId);

        loadCall.enqueue(new Callback<List<Entity>>() {
            @Override
            public void onResponse(Call<List<Entity>> call, Response<List<Entity>> response) {
                if (response.isSuccessful()) {
                    final List<Entity> entities = response.body();
                    setPage(entities, page);
                    onDataLoaded(entities);
                    loadFinished();
                    moreDataAvailable = entities.size() == WinterService.PER_PAGE_DEFAULT;
                    loadCall = null;
                } else {
                    failure();
                }
            }

            @Override
            public void onFailure(Call<List<Entity>> call, Throwable t) {
                failure();
            }
        });
    }

    public void loadMore() {
        loadData();
    }

    public void reloadData() {
        loadEntities(0);
    }

    @Override
    protected void loadData(int page) {
        loadEntities(page);
    }

    @Override
    public void cancelLoading() {
        if (loadCall != null) loadCall.cancel();
    }

    private void failure() {
        loadFinished();
        loadCall = null;
        moreDataAvailable = false;
    }
}