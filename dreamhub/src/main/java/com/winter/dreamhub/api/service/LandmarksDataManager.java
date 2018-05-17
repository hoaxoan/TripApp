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

public abstract class LandmarksDataManager extends PaginatedDataManager<List<Entity>> {
    private Call loadCall;
    private long tripId;
    private long moodId;

    public LandmarksDataManager(Context context, long tripId) {
        super(context);
        this.tripId = tripId;
    }

    public LandmarksDataManager(Context context, long tripId, long moodId) {
        super(context);
        this.tripId = tripId;
        this.moodId = moodId;
    }

    private void loadLandmarks(final int page) {
        if(this.moodId > 0){
            loadCall = getWinterApi()
                    .getEntitiesByMoods(tripId, WinterService.TYPE_LANDMARKS, moodId, page, WinterService.PER_PAGE_DEFAULT);
        } else {
            loadCall = getWinterApi()
                    .getEntities(tripId, WinterService.TYPE_LANDMARKS, page, WinterService.PER_PAGE_DEFAULT);
        }

        loadCall.enqueue(new Callback<List<Entity>>() {
            @Override
            public void onResponse(Call<List<Entity>> call, Response<List<Entity>> response) {
                if (response.isSuccessful()) {
                    final List<Entity> landmarks = response.body();
                    setPage(landmarks, page);
                    onDataLoaded(landmarks);
                    loadFinished();
                    moreDataAvailable = landmarks.size() == WinterService.PER_PAGE_DEFAULT;
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
        loadLandmarks(0);
    }

    @Override
    protected void loadData(int page) {
        loadLandmarks(page);
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