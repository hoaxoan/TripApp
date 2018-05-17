package com.winter.dreamhub.api.service;

import android.content.Context;

import com.winter.dreamhub.api.PaginatedDataManager;
import com.winter.dreamhub.api.service.model.Entity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hoaxoan on 12/18/2016.
 */

public abstract class SearchDataManager extends PaginatedDataManager<List<Entity>> {

    // state
    private String query = "";
    private long tripId = 0;
    private int type = 0;
    private long moodId = 0;
    private Call loadCall;

    public SearchDataManager(Context context) {
        super(context);
    }

    public SearchDataManager(Context context, long tripId) {
        super(context);
        this.tripId = tripId;
    }

    public SearchDataManager(Context context, long tripId, int type) {
        super(context);
        this.tripId = tripId;
        this.type = type;
    }

    public SearchDataManager(Context context, long tripId, int type, long moodId) {
        super(context);
        this.tripId = tripId;
        this.type = type;
        this.moodId = moodId;
    }

    public void searchFor(String query) {
        searchEntities(query, 0);
    }

    public void loadMore() {
        loadData();
    }

    public void clear() {
        cancelLoading();
        query = "";
        resetLoadingCount();
    }

    @Override
    public void cancelLoading() {
        if (loadCall != null) loadCall.cancel();
    }

    public String getQuery() {
        return query;
    }

    @Override
    protected void loadData(int page) {
        searchEntities(this.query, page);
    }

    private void searchEntities(final String query, final int page) {
        loadCall = getWinterApi().search(
                query, tripId, type, moodId, page, WinterService.PER_PAGE_DEFAULT,
                WinterService.SORT_POPULAR);
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
                    failure(loadCall);
                }
            }

            @Override
            public void onFailure(Call<List<Entity>> call, Throwable t) {
                failure(loadCall);
            }
        });
    }

    private void failure(Call call) {
        loadFinished();
        loadCall = null;
        moreDataAvailable = false;
    }

}
