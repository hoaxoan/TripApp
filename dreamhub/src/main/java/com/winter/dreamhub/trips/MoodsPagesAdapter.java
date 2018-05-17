package com.winter.dreamhub.trips;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.winter.dreamhub.api.service.model.Mood;
import com.winter.dreamhub.landmarks.LandmarkListFragment;

import java.util.List;

/**
 * Created by hoaxoan on 10/27/2016.
 */

public class MoodsPagesAdapter extends FragmentStatePagerAdapter {

    private final Fragment[] fragments;
    private final List<Mood> moods;
    private final long tripId;
    private final ProgressBar loading;

    public MoodsPagesAdapter(FragmentManager fragmentManager, List<Mood> moods, long tripId,
                             ProgressBar loading) {
        super(fragmentManager);
        this.moods = moods;
        this.fragments = new Fragment[moods.size()];
        this.tripId = tripId;
        this.loading = loading;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        this.fragments[position] = null;
    }

    @Override
    public Fragment getItem(int position) {
        Mood mood = moods.get(position);
        return LandmarkListFragment.newInstance(mood.id, this.tripId, loading);
    }

    @Override
    public int getCount() {
        return this.moods.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        this.fragments[position] = fragment;
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Mood mood = moods.get(position);
        return mood.name;
    }
}
