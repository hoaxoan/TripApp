package com.winter.dreamhub.restaurants;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;

import com.winter.dreamhub.api.service.RestaurantsDataManager;
import com.winter.dreamhub.api.service.model.Entity;


public class RestaurantsViewModel extends AndroidViewModel {

    public final ObservableList<Entity> items = new ObservableArrayList<>();

    public final ObservableBoolean dataLoading = new ObservableBoolean(false);

    private final Context mContext; // To avoid leaks, this must be an Application Context.
    private RestaurantsDataManager dataManager;

    public void setDataManager(RestaurantsDataManager dataManager) {
        this.dataManager = dataManager;
    }

    public RestaurantsViewModel(
            Application context) {
        super(context);
        this.mContext = context.getApplicationContext(); // Force use of Application Context.
    }

    public void start() {
        if (dataManager == null) return;
        dataManager.loadData();
    }

    public void reloadData() {
        if (dataManager == null) return;
        dataManager.reloadData();
    }

    void handleActivityResult(int requestCode, int resultCode) {

    }
}

