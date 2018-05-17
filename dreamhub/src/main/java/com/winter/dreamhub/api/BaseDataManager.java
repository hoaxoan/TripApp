package com.winter.dreamhub.api;


import android.content.Context;
import android.support.annotation.NonNull;

import com.winter.dreamhub.BuildConfig;
import com.winter.dreamhub.api.auth.AuthInterceptor;
import com.winter.dreamhub.api.model.BaseModel;
import com.winter.dreamhub.api.prefs.WinterPrefs;
import com.winter.dreamhub.api.service.WinterService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Base class for loading data; extending types are responsible for providing implementations of
 * {@link #onDataLoaded(Object)} to do something with the data and {@link #cancelLoading()} to
 * cancel any activity.
 */
public abstract class BaseDataManager<T> implements DataLoadingSubject {

    private final AtomicInteger loadingCount;
    private final WinterPrefs winterPrefs;


    private List<DataLoadingSubject.DataLoadingCallbacks> loadingCallbacks;

    public BaseDataManager(@NonNull Context context) {
        loadingCount = new AtomicInteger(0);
        winterPrefs = WinterPrefs.get(context);
    }

    public abstract void onDataLoaded(T data);

    public abstract void cancelLoading();

    @Override
    public boolean isDataLoading() {
        return loadingCount.get() > 0;
    }

    public WinterPrefs getWinterPrefs() {
        return winterPrefs;
    }

    public WinterService getWinterApi() {
        return winterPrefs.getApi();
    }

    @Override
    public void registerCallback(DataLoadingSubject.DataLoadingCallbacks callback) {
        if (loadingCallbacks == null) {
            loadingCallbacks = new ArrayList<>(1);
        }
        loadingCallbacks.add(callback);
    }

    @Override
    public void unregisterCallback(DataLoadingSubject.DataLoadingCallbacks callback) {
        if (loadingCallbacks != null && loadingCallbacks.contains(callback)) {
            loadingCallbacks.remove(callback);
        }
    }

    protected void loadStarted() {
        if (0 == loadingCount.getAndIncrement()) {
            dispatchLoadingStartedCallbacks();
        }
    }

    protected void loadFinished() {
        if (0 == loadingCount.decrementAndGet()) {
            dispatchLoadingFinishedCallbacks();
        }
    }

    protected void resetLoadingCount() {
        loadingCount.set(0);
    }

    protected static void setPage(List<? extends BaseModel> items, int page) {
        for (BaseModel item : items) {
            item.page = page;
        }
    }

    protected static void setDataSource(List<? extends BaseModel> items, String dataSource) {
        for (BaseModel item : items) {
            item.dataSource = dataSource;
        }
    }

    protected void dispatchLoadingStartedCallbacks() {
        if (loadingCallbacks == null || loadingCallbacks.isEmpty()) return;
        for (DataLoadingCallbacks loadingCallback : loadingCallbacks) {
            loadingCallback.dataStartedLoading();
        }
    }

    protected void dispatchLoadingFinishedCallbacks() {
        if (loadingCallbacks == null || loadingCallbacks.isEmpty()) return;
        for (DataLoadingCallbacks loadingCallback : loadingCallbacks) {
            loadingCallback.dataFinishedLoading();
        }
    }

}