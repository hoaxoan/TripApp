package com.winter.dreamhub.savedplaces;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.support.v4.widget.SwipeRefreshLayout;

import com.winter.dreamhub.api.service.SavePlacesDataManager;
import com.winter.dreamhub.api.service.model.Entity;

import java.util.List;

/**
 * Exposes the data to be used in the task list screen.
 * <p>
 * {@link BaseObservable} implements a listener registration mechanism which is notified when a
 * property changes. This is done by assigning a {@link Bindable} annotation to the property's
 * getter method.
 */
public class SavePlacesViewModel extends AndroidViewModel {

    public final ObservableList<Entity> items = new ObservableArrayList<>();

    public final ObservableBoolean dataLoading = new ObservableBoolean(false);

    private final Context mContext; // To avoid leaks, this must be an Application Context.
    private SavePlacesDataManager dataManager;

    public void setDataManager(SavePlacesDataManager dataManager) {
        this.dataManager = dataManager;
    }

    public SavePlacesViewModel(
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
