package com.winter.dreamhub.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.common.base.Preconditions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.winter.dreamhub.R;
import com.winter.dreamhub.gcm.util.NotificationConfigs;
import com.winter.dreamhub.gcm.util.NotificationUtils;
import com.winter.dreamhub.profiles.ProfilesFragment;
import com.winter.dreamhub.searchs.SearchFragment;
import com.winter.dreamhub.trips.TripHomeActivity;
import com.winter.dreamhub.trips.TripsFragment;
import com.winter.dreamhub.util.Utils;
import com.winter.dreamhub.widget.DrawShadowFrameLayout;

import static com.winter.dreamhub.util.LogUtils.LOGE;
import static com.winter.dreamhub.util.LogUtils.makeLogTag;

/**
 * Created by hoaxoan on 5/4/2016.
 */
public class HomeActivity extends BaseActivity implements TripsFragment.Listener{
    private static String TAG = makeLogTag(HomeActivity.class);

    // Notification
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_HUB;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        setupBottomNavigation();
        //trySetUpActionBarSpinner();

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                LOGE(TAG, "Key: " + key + " Value: " + value);
            }
        }
        // [END handle_data_extras]

        // Notification
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(NotificationConfigs.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(NotificationConfigs.TOPIC_GLOBAL);

                } else if (intent.getAction().equals(NotificationConfigs.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        // Configure the fragment's top clearance to take our overlaid controls (Action Bar
        // and spinner box) into account.
        int actionBarSize = Utils.calculateActionBarSize(this);
        DrawShadowFrameLayout drawShadowFrameLayout =
                (DrawShadowFrameLayout) this.findViewById(R.id.main_content);
        if (drawShadowFrameLayout != null) {
            drawShadowFrameLayout.setShadowTopOffset(actionBarSize);
        }
        //setContentTopClearance(actionBarSize);

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(NotificationConfigs.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(NotificationConfigs.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /*private void setContentTopClearance(int clearance) {
        if (currentFragment != null && currentFragment.getView() != null) {
            currentFragment.getView().setPadding(currentFragment.getView().getPaddingLeft(), clearance,
                    currentFragment.getView().getPaddingRight(), currentFragment.getView().getPaddingBottom());
        }
    }*/

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    private void trySetUpActionBarSpinner() {
        Toolbar toolbar = getActionBarToolbar();

        View searchView = LayoutInflater.from(this).inflate(R.layout.search_box,
                toolbar, false);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        toolbar.addView(searchView, lp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private void setupBottomNavigation() {
        // views
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // load Fragment
        loadFragment(new TripsFragment());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_trips:
                    fragment = new TripsFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_searchs:
                    fragment = new SearchFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
                    fragment = new ProfilesFragment();
                    loadFragment(fragment);
                    return true;
            }

            return false;
        }
    };

    /**
     * loading fragment into FrameLayout
     *
     * @param fragment
     */
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onTripSelected(long id, String tripName) {
        startTripHomeActivity(id, tripName);
    }

    private final void startTripHomeActivity(long id, String tripName) {
        Intent intent = new Intent(this, TripHomeActivity.class);
        intent.putExtra("trip_id", (long) Preconditions.checkNotNull(id));
        intent.putExtra("trip_name", Preconditions.checkNotNull(tripName));
        startActivity(intent);
    }
}
