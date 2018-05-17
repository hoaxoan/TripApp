package com.winter.dreamhub.api.service;

import android.content.Context;

import com.winter.dreamhub.api.PaginatedDataManager;
import com.winter.dreamhub.api.service.model.Trip;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hoaxoan on 12/11/2016.
 */

public abstract class TripsDataManager extends PaginatedDataManager<List<Trip>> {

    private String query = "";
    private Call loadCall;

    public TripsDataManager(Context context) {
        super(context);
    }

    public void searchFor(String query) {
        loadTrips(query, 0);
    }

    private void loadTrips(final String query, final int page) {
        loadCall = getWinterApi()
                .getTrips(query, page, WinterService.PER_PAGE_DEFAULT);
        loadCall.enqueue(new Callback<List<Trip>>() {
            @Override
            public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {
                if (response.isSuccessful()) {
                    final List<Trip> trips = response.body();
                    setPage(trips, page);
                    onDataLoaded(trips);
                    loadFinished();
                    moreDataAvailable = trips.size() == WinterService.PER_PAGE_DEFAULT;
                    loadCall = null;
                } else {
                    failure();
                }
            }

            @Override
            public void onFailure(Call<List<Trip>> call, Throwable t) {
                failure();
            }
        });
    }

    @Override
    protected void loadData(int page) {
        loadTrips(this.query, page);
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