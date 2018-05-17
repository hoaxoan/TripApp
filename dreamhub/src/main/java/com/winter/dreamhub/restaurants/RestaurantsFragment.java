package com.winter.dreamhub.restaurants;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.winter.dreamhub.R;
import com.winter.dreamhub.api.prefs.WinterPrefs;
import com.winter.dreamhub.api.service.WinterService;
import com.winter.dreamhub.api.service.model.Category;
import com.winter.dreamhub.searchs.QuickSearchActivity;
import com.winter.dreamhub.util.DisplayUtils;
import com.winter.dreamhub.widget.PreCachedLinearLayoutManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.winter.dreamhub.util.LogUtils.makeLogTag;

/**
 * Created by hoaxoan on 11/19/2016.
 */

public class RestaurantsFragment extends Fragment {
    public static final String TAG = makeLogTag(RestaurantsFragment.class);
    private static final int RC_SEARCH = 0;

    private View rootView;
//    private SwipeRefreshLayout refreshLayout;
    private ProgressBar loading;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    private WinterPrefs winterPrefs;
    private RestaurantGroupsAdapter adapter;

    private long tripId;
    private long categoryId;

    private Toolbar toolbar;

    /**
     * Create a new instance of the fragment
     */
    public static RestaurantsFragment newInstance(long tripId, long categoryId, Toolbar toolbar) {
        RestaurantsFragment fragment = new RestaurantsFragment();
        fragment.tripId = tripId;
        fragment.categoryId = categoryId;
        fragment.toolbar = toolbar;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        winterPrefs = WinterPrefs.get(getActivity());
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.recyclerView = ((RecyclerView) this.rootView.findViewById(R.id.recycler_view));
        this.layoutManager = new PreCachedLinearLayoutManager(getActivity(), 1, false, new DisplayUtils(getActivity().getWindowManager()));

        // Refresh
       /* this.refreshLayout = ((SwipeRefreshLayout) this.rootView.findViewById(R.id.swipe_container));
        this.refreshLayout.setColorSchemeResources(R.color.sunflower_yellow, R.color.neon_blue,
                R.color.lightish_blue, R.color.aqua_marine);*/
        //this.refreshLayout.setOnRefreshListener(this);
//        this.refreshLayout.setRefreshing(false);

        // Loading
        this.loading = (ProgressBar) this.rootView.findViewById(android.R.id.empty);

        // Load data
        loadEntities();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.restaurants_fragment, container, false);
        return this.rootView;
    }

    @Override
    public final void onPause() {
        super.onPause();
    }

    @Override
    public final void onResume() {
        super.onResume();
    }


    private void loadEntities() {
        Call loadCall = winterPrefs.getApi().getCategoryByTypes(WinterService.TYPE_RESTAURANT);
        loadCall.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    final List<Category> categories = response.body();
                    bindEntities(categories);
                    checkEmptyState();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });
    }

    private void bindEntities(List<Category> categories) {
        this.adapter = new RestaurantGroupsAdapter(getActivity(), categories, this.tripId);
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.recyclerView.setAdapter(this.adapter);
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
                        (searchMenuView.getWidth() / 2), this.tripId, WinterService.TYPE_RESTAURANTS), RC_SEARCH, ActivityOptions
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

    private void checkEmptyState() {
        if (adapter.getItemCount() == 0) {
            // if grid is empty check whether we're loading or if no filters are selected
            loading.setVisibility(View.GONE);
            toolbar.setTranslationZ(0f);
        } else {
            loading.setVisibility(View.GONE);
        }
    }
}
