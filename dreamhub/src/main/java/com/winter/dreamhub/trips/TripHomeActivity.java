package com.winter.dreamhub.trips;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.winter.dreamhub.R;
import com.winter.dreamhub.activity.BaseActivity;
import com.winter.dreamhub.trips.TripHomeFragment;
import com.winter.dreamhub.trips.TripsFragment;
import com.winter.dreamhub.util.DisplayUtils;
import com.winter.dreamhub.util.ImageUtils;
import com.winter.dreamhub.widget.ElasticDragDismissFrameLayout;
import com.winter.dreamhub.widget.FadingImageView;
import com.winter.dreamhub.widget.FixedAspectRatioFrameLayout;
import com.winter.dreamhub.widget.TabbedLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hoaxoan on 10/25/2016.
 */

public class TripHomeActivity extends BaseActivity {

    private Toolbar mActionBarToolbar;
    private TripHomeFragment tripsFragment;

    private ElasticDragDismissFrameLayout draggableFrame;
    private ElasticDragDismissFrameLayout.SystemChromeFader chromeFader;


    private long tripId;
    private String tripName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_home_activity_layout);

        // Data
        tripId = getIntent().getLongExtra("trip_id", 0);
        tripName = getIntent().getStringExtra("trip_name");
        if (tripId == 0) {
            finish();
        }

        // ActionBar
        mActionBarToolbar = getActionBarToolbar();
        mActionBarToolbar.setNavigationIcon(R.drawable.quantum_ic_arrow_back_white_24);
        mActionBarToolbar.setTitleTextColor(getResources().getColor(R.color.quantum_white_100));
        mActionBarToolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setTitle(this.tripName);

        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // DragFrame
        draggableFrame = (ElasticDragDismissFrameLayout) findViewById(R.id.draggable_frame);
        chromeFader = new ElasticDragDismissFrameLayout.SystemChromeFader(this) {
            @Override
            public void onDragDismissed() {
                expandImageAndFinish();
            }
        };

        // Fragment
        tripsFragment = TripHomeFragment.newInstance(this.tripId, this.tripName);
        showFragment(R.id.fragment_container, tripsFragment);

    }

    @Override
    protected void onResume() {
        super.onResume();
        draggableFrame.addListener(chromeFader);
    }

    @Override
    protected void onPause() {
        draggableFrame.removeListener(chromeFader);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.trip_home_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void expandImageAndFinish() {
        // if we drag dismiss downward then the default reversal of the enter
        // transition would slide content upward which looks weird. So reverse it.
        if (draggableFrame.getTranslationY() > 0) {
            getWindow().setReturnTransition(
                    TransitionInflater.from(this)
                            .inflateTransition(R.transition.about_return_downward));
        }
        finishAfterTransition();
    }

}
