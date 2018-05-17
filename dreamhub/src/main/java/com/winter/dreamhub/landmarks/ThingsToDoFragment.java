package com.winter.dreamhub.landmarks;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.winter.dreamhub.R;
import com.winter.dreamhub.trips.MoodsPagesAdapter;
import com.winter.dreamhub.api.service.WinterService;
import com.winter.dreamhub.api.service.model.Mood;
import com.winter.dreamhub.searchs.QuickSearchActivity;

import java.util.List;

import static com.winter.dreamhub.util.LogUtils.makeLogTag;

/**
 * Created by hoaxoan on 10/27/2016.
 */

public class ThingsToDoFragment extends Fragment {
    public static final String TAG = makeLogTag(ThingsToDoFragment.class);
    /**
     * The key used to save the position in the {@link #mViewPagerAdapter} for the current {@link
     * ThingsToDoFragment}s.
     */
    private static final String CURRENT_INFO_TAB_FRAGMENT_POSITION =
            "current_tnt_fragments_position";
    private static final int RC_SEARCH = 0;

    private ProgressBar loading;
    private ViewPager mViewPager;
    private MoodsPagesAdapter mViewPagerAdapter;
    private TabLayout mTabLayout;
    private int mCurrentPage;
    private View rootView;

    private long tripId;
    private long categoryId;
    private List<Mood> moods;
    private Toolbar toolbar;

    public static ThingsToDoFragment newInstance(long tripId, long categoryId, List<Mood> moods, Toolbar toolbar) {
        ThingsToDoFragment fragment = new ThingsToDoFragment();
        fragment.tripId = tripId;
        fragment.categoryId = categoryId;
        fragment.moods = moods;
        fragment.toolbar = toolbar;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Loading
        this.loading = (ProgressBar) this.rootView.findViewById(R.id.loading_spinner);

        // TabLayout
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(CURRENT_INFO_TAB_FRAGMENT_POSITION)) {
                mCurrentPage = savedInstanceState.getInt(
                        CURRENT_INFO_TAB_FRAGMENT_POSITION);
            }
        }

        this.mTabLayout = ((TabLayout) this.rootView.findViewById(R.id.tabs));
        this.mViewPager = (ViewPager) this.rootView.findViewById(R.id.viewpager);

        this.mViewPagerAdapter = new MoodsPagesAdapter(getChildFragmentManager(), this.moods, this.tripId, this.loading);
        this.mViewPager.setAdapter(this.mViewPagerAdapter);

        this.mTabLayout.setupWithViewPager(this.mViewPager);

        setCurrentPage();
    }

    private void setCurrentPage() {
        if (mViewPager != null) {
            mViewPager.setCurrentItem(mCurrentPage);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.things_to_do_fragment, container, false);
        return this.rootView;
    }

    @Override
    public final void onPause() {
        super.onPause();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                // get the icon's location on screen to pass through to the search screen
                View searchMenuView = toolbar.findViewById(R.id.menu_search);
                int[] loc = new int[2];
                searchMenuView.getLocationOnScreen(loc);
                startActivityForResult(QuickSearchActivity.createStartIntent(getActivity(), loc[0], loc[0] +
                        (searchMenuView.getWidth() / 2), this.tripId, WinterService.TYPE_LANDMARKS), RC_SEARCH, ActivityOptions
                        .makeSceneTransitionAnimation(getActivity()).toBundle());
                searchMenuView.setAlpha(0f);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RC_SEARCH:
                // reset the search icon which we hid
                View searchMenuView = toolbar.findViewById(R.id.menu_search);
                if (searchMenuView != null) {
                    searchMenuView.setAlpha(1f);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
