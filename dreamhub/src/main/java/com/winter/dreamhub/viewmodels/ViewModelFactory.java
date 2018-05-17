package com.winter.dreamhub.viewmodels;


import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.VisibleForTesting;

import com.winter.dreamhub.hotels.HotelDetailsViewModel;
import com.winter.dreamhub.hotels.HotelsViewModel;
import com.winter.dreamhub.restaurants.RestaurantDetailsViewModel;
import com.winter.dreamhub.restaurants.RestaurantsViewModel;
import com.winter.dreamhub.savedplaces.SavePlacesViewModel;
import com.winter.dreamhub.landmarks.LandmarkDetailsViewModel;

/**
 * A creator is used to inject the product ID into the ViewModel
 * <p>
 * This creator is to showcase how to inject dependencies into ViewModels. It's not
 * actually necessary in this case, as the product ID can be passed in a public method.
 */
public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactory INSTANCE;

    private final Application mApplication;

    public static ViewModelFactory getInstance(Application application) {

        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(application);
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    private ViewModelFactory(Application application) {
        mApplication = application;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LandmarkDetailsViewModel.class)) {
            //noinspection unchecked
            return (T) new LandmarkDetailsViewModel(mApplication);
        } else if (modelClass.isAssignableFrom(HotelDetailsViewModel.class)) {
            //noinspection unchecked
            return (T) new HotelDetailsViewModel(mApplication);
        } else if (modelClass.isAssignableFrom(HotelsViewModel.class)) {
            //noinspection unchecked
            return (T) new HotelsViewModel(mApplication);
        } else if (modelClass.isAssignableFrom(RestaurantDetailsViewModel.class)) {
            //noinspection unchecked
            return (T) new RestaurantDetailsViewModel(mApplication);
        } else if (modelClass.isAssignableFrom(RestaurantsViewModel.class)) {
            //noinspection unchecked
            return (T) new RestaurantsViewModel(mApplication);
        } else if (modelClass.isAssignableFrom(SavePlacesViewModel.class)) {
            //noinspection unchecked
            return (T) new SavePlacesViewModel(mApplication);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}