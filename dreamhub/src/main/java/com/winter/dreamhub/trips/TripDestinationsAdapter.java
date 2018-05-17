package com.winter.dreamhub.trips;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.google.common.base.Preconditions;

/**
 * Created by hoaxoan on 10/27/2016.
 */

public class TripDestinationsAdapter extends FragmentStatePagerAdapter {
    private final Fragment[] fragments;

    public TripDestinationsAdapter(FragmentManager paramFragmentManager) {
        super(paramFragmentManager);
        this.fragments = new Fragment[1];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        this.fragments[position] = null;
    }

    @Override
    public Fragment getItem(int position) {
        return new TripHomeFragment();
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TripHomeFragment tripHomeFragment = (TripHomeFragment) super.instantiateItem(container, position);
        if (!tripHomeFragment.isDestinationSet()) {
            if (tripHomeFragment.isResumed()) {
                tripHomeFragment.initUI();
            }
        }
        this.fragments[position] = tripHomeFragment;
        return tripHomeFragment;
    }
}
