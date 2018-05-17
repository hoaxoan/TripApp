package com.winter.dreamhub.trips;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.winter.dreamhub.R;
import com.winter.dreamhub.api.service.TripsDataManager;
import com.winter.dreamhub.api.service.model.Trip;
import com.winter.dreamhub.util.ImeUtils;

import java.util.List;

import static com.winter.dreamhub.util.LogUtils.makeLogTag;

/**
 * Created by hoaxoan on 10/24/2016.
 */

public class TripsFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = makeLogTag(TripsFragment.class);

    private TripsAdapter adapter;

    private View rootView;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView trips;
    private EditText searchView;
    private ProgressBar progress;

    private TripsDataManager dataManager;

    /**
     * Create a new instance of the fragment
     */
    public static TripsFragment newInstance() {
        TripsFragment fragment = new TripsFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.refreshLayout = (SwipeRefreshLayout) this.rootView.findViewById(R.id.trips_swipe_container);
        this.refreshLayout.setColorSchemeResources(R.color.sunflower_yellow, R.color.neon_blue,
                R.color.lightish_blue, R.color.aqua_marine);
        this.refreshLayout.setOnRefreshListener(this);

        // RecyclerView
        this.trips = (RecyclerView) this.rootView.findViewById(R.id.list);
        this.progress = (ProgressBar) this.rootView.findViewById(android.R.id.empty);
        this.searchView = this.rootView.findViewById(R.id.search_box);
        this.searchView.addTextChangedListener(mTextWatcher);
        this.searchView.setOnEditorActionListener(mOnEditorActionListener);
        this.searchView.setOnKeyListener(mTextKeyListener);
        bindEntity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.trip_list, container, false);
        return this.rootView;
    }

    @Override
    public final void onPause() {
        super.onPause();
        refreshLayout.setRefreshing(false);
    }

    @Override
    public final void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (dataManager != null) {
            dataManager.cancelLoading();
        }
        super.onDestroy();
    }


    @Override
    public void onRefresh() {
        adapter.clear();
        dataManager.searchFor(searchView.getText().toString());
        refreshLayout.setRefreshing(true);
        /*if (ConnectivityUtils.isConnected(BaseActivity.this)) {
            requestDataRefresh();
        } else {
            refreshLayout.setRefreshing(false);
        }*/
    }

    public void bindEntity() {
        // load the entity
        dataManager = new TripsDataManager(getActivity()) {
            @Override
            public void onDataLoaded(List<Trip> data) {
                progress.setVisibility(View.GONE);
                if (data != null && data.size() > 0) {
                    adapter.addAndResort(data);
                }
                refreshLayout.setRefreshing(false);
            }
        };
        this.adapter = new TripsAdapter(getActivity(), dataManager);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        this.trips.setLayoutManager(layoutManager);
        this.trips.setItemAnimator(new DefaultItemAnimator());
        this.trips.setAdapter(this.adapter);

        dataManager.loadData();
    }

    private void searchFor(String query, boolean hideIme) {
        progress.setVisibility(View.VISIBLE);
        if(hideIme) {
            ImeUtils.hideIme(searchView);
            searchView.clearFocus();
        }
        this.dataManager.searchFor(query);
        this.adapter.clear();
    }

    /**
     * Callback to watch the text field for empty/non-empty
     */
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int before, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int after) {
            searchFor(s.toString(), false);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private final TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {

        /**
         * Called when the input method default action key is pressed.
         */
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            searchFor(v.getText().toString(), true);
            return true;
        }
    };

    /**
     * React to the user typing "enter" or other hardwired keys while typing in
     * the search box. This handles these special keys while the edit box has
     * focus.
     */
    View.OnKeyListener mTextKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            // If there is text in the query box, handle enter, and action keys
            // The search key is handled by the dialog's onKeyDown().
            if (event.hasNoModifiers()) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        v.cancelLongPress();

                        searchFor(searchView.getText().toString(), true);
                        return true;
                    }
                }
            }
            return false;
        }
    };
    public interface Listener {
        public void onTripSelected(long id, String tripName);
    }
}
