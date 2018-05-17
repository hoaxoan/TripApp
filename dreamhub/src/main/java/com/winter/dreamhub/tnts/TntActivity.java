package com.winter.dreamhub.tnts;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.winter.dreamhub.R;
import com.winter.dreamhub.activity.BaseActivity;
import com.winter.dreamhub.api.prefs.WinterPrefs;
import com.winter.dreamhub.api.service.model.Guideline;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hoaxoan on 11/6/2016.
 */

public class TntActivity extends BaseActivity {

    /**
     * The key used to save the position in the {@link #mViewPagerAdapter} for the current {@link
     * TntActivity}s.
     */
    private static final String CURRENT_INFO_TAB_FRAGMENT_POSITION =
            "current_tnt_fragments_position";

    private ViewPager mViewPager;
    private TntSubcategoriesAdapter mViewPagerAdapter;
    private TabLayout mTabLayout;
    private int mCurrentPage;
    private Toolbar mActionBarToolbar;

    private long tripId;
    private String categoryName;
    private WinterPrefs winterPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tnt_activity);
        winterPrefs = WinterPrefs.get(this);

        this.tripId = getIntent().getLongExtra("trip_id", 0);
        this.categoryName = getIntent().getStringExtra("category_name");

        // Toolbar
        mActionBarToolbar = getActionBarToolbar();
        mActionBarToolbar.setNavigationIcon(R.drawable.quantum_ic_arrow_back_grey600_24);
        mActionBarToolbar.setTitleTextColor(getResources().getColor(R.color.quantum_black_secondary_text));
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setTitle(categoryName);

        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        // TabLayout
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(CURRENT_INFO_TAB_FRAGMENT_POSITION)) {
                mCurrentPage = savedInstanceState.getInt(
                        CURRENT_INFO_TAB_FRAGMENT_POSITION);
            }
        }

        this.mTabLayout = ((TabLayout) findViewById(R.id.tabs));
        this.mViewPager = (ViewPager) findViewById(R.id.viewpager);

        // Load moods
        loadMoods();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mViewPagerAdapter != null && mViewPagerAdapter.getFragments() != null) {
           outState.putInt(CURRENT_INFO_TAB_FRAGMENT_POSITION, mViewPager.getCurrentItem());
        }
    }

    final void buildUi(List<Guideline> guidelines) {
        this.mViewPagerAdapter = new TntSubcategoriesAdapter(getSupportFragmentManager(), guidelines, this.tripId, true);
        this.mViewPager.setAdapter(this.mViewPagerAdapter);

        this.mTabLayout.setupWithViewPager(this.mViewPager);

        setCurrentPage();
    }

    private void setCurrentPage() {
        if (mViewPager != null) {
            mViewPager.setCurrentItem(mCurrentPage);
        }
    }

    private void loadMoods() {
        final Call<List<Guideline>> guidelinesCall = winterPrefs.getApi().getParentGuideline(this.tripId, 0);
        guidelinesCall.enqueue(new Callback<List<Guideline>>() {
            @Override
            public void onResponse(Call<List<Guideline>> call, Response<List<Guideline>> response) {
                List<Guideline> guidelines = response.body();
                buildUi(guidelines);
            }

            @Override
            public void onFailure(Call<List<Guideline>> call, Throwable t) {
            }
        });
    }

    @Override
    public final void onPause() {
        super.onPause();
    }

}
