package com.winter.dreamhub.landmarks;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.winter.dreamhub.api.service.model.Entity;
import com.winter.dreamhub.viewmodels.SingleLiveEvent;

public class LandmarkDetailsViewModel extends AndroidViewModel {

    private final SingleLiveEvent<Void> mEntityUpdated = new SingleLiveEvent<>();

    public LandmarkDetailsViewModel(@NonNull Application application) {
        super(application);
    }

    SingleLiveEvent<Void> getEntityUpdatedEvent() {
        return mEntityUpdated;
    }

    public void onBookmarkClicked(Entity entity, boolean isBookmark) {
        mEntityUpdated.call();
    }
}
