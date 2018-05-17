package com.winter.dreamhub.tnts;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.google.common.base.Preconditions;
import com.winter.dreamhub.api.service.model.Guideline;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoaxoan on 11/6/2016.
 */

public class TntSubcategoriesAdapter extends FragmentStatePagerAdapter {

    private final FragmentManager fm;
    private final Fragment[] fragments;
    private final boolean refresh;
    private final List<Guideline> subGuidelines;
    private final long tripId;

    public TntSubcategoriesAdapter(FragmentManager fm, List<Guideline> subGuidelines, long tripId, boolean refresh) {
        super(fm);
        this.fm = fm;
        if (subGuidelines == null) {
            subGuidelines = new ArrayList<>();
        }
        this.subGuidelines = subGuidelines;
        this.fragments = new Fragment[subGuidelines.size()];
        this.tripId = tripId;
        this.refresh = refresh;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        this.fragments[position] = null;
    }

    @Override
    public Fragment getItem(int position) {
        Guideline guideline = subGuidelines.get(position);
        return TntSubcategoryFragment.newInstance(this.tripId, guideline.id);
    }

    @Override
    public int getCount() {
        return this.subGuidelines.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TntSubcategoryFragment fragment = (TntSubcategoryFragment) super.instantiateItem(container, position);
        this.fragments[position] = fragment;
        return fragment;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        super.restoreState(state, loader);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Guideline guideline = subGuidelines.get(position);
        return guideline.name;
    }

    /**
     * @return all the cached {@link Fragment}s used by this Adapter.
     */
    public Fragment[] getFragments() {
        if (fragments == null) {
            // Force creating the fragments
            int count = getCount();
            for (int i = 0; i < count; i++) {
                getItem(i);
            }
        }
        return fragments;
    }
}
