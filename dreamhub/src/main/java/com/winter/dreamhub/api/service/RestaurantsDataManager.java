package com.winter.dreamhub.api.service;

import android.content.Context;

import com.winter.dreamhub.api.PaginatedDataManager;
import com.winter.dreamhub.api.service.model.Entity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hoaxoan on 12/12/2016.
 */

public abstract class RestaurantsDataManager extends PaginatedDataManager<List<Entity>> {
    private Call loadCall;
    private long tripId;
    private long categoryId;

    public RestaurantsDataManager(Context context, long tripId, long categoryId) {
        super(context);
        this.tripId = tripId;
        this.categoryId = categoryId;
    }

    private void loadRestaurants(final int page) {
        loadCall = getWinterApi()
                .getEntitiesByCategories(tripId, WinterService.TYPE_RESTAURANTS, categoryId, page, WinterService.PER_PAGE_DEFAULT);
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
        loadRestaurants(0);
    }

    @Override
    protected void loadData(int page) {
        loadRestaurants(page);
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