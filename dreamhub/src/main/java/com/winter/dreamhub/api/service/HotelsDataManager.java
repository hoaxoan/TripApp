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

public abstract class HotelsDataManager extends PaginatedDataManager<List<Entity>> {
    private Call loadCall;
    private long tripId;

    public HotelsDataManager(Context context, long tripId) {
        super(context);
        this.tripId = tripId;
    }


    private void loadHotels(final int page) {
        loadCall = getWinterApi()
                .getEntities(tripId, WinterService.TYPE_HOTELS, page, WinterService.PER_PAGE_DEFAULT);

        loadCall.enqueue(new Callback<List<Entity>>() {
            @Override
            public void onResponse(Call<List<Entity>> call, Response<List<Entity>> response) {
                if (response.isSuccessful()) {
                    final List<Entity> hotels = response.body();
                    setPage(hotels, page);
                    onDataLoaded(hotels);
                    loadFinished();
                    moreDataAvailable = hotels.size() == WinterService.PER_PAGE_DEFAULT;
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
        loadHotels(0);
    }

    @Override
    protected void loadData(int page) {
        loadHotels(page);
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