package com.winter.dreamhub.restaurants;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.winter.dreamhub.R;
import com.winter.dreamhub.api.EventCallbacks;
import com.winter.dreamhub.api.prefs.WinterPrefs;
import com.winter.dreamhub.api.service.RestaurantsDataManager;
import com.winter.dreamhub.api.service.WinterService;
import com.winter.dreamhub.api.service.model.Entity;
import com.winter.dreamhub.api.service.request.VisitedPlacesRequest;
import com.winter.dreamhub.api.service.response.VisitedPlacesResponse;
import com.winter.dreamhub.auth.WinterLogin;
import com.winter.dreamhub.databinding.RestaurantsListFragmentBinding;
import com.winter.dreamhub.searchs.QuickSearchActivity;
import com.winter.dreamhub.util.DisplayUtils;
import com.winter.dreamhub.widget.PreCachedLinearLayoutManager;
import com.winter.dreamhub.widget.recyclerview.InfiniteScrollListener;
import com.winter.dreamhub.widget.transitions.MorphTransform;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_FIRST_USER;
import static com.winter.dreamhub.util.LogUtils.makeLogTag;

/**
 * Created by hoaxoan on 11/20/2016.
 */

public class RestaurantsListFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, EventCallbacks {
    public static final String TAG = makeLogTag(RestaurantsFragment.class);
    private static final int RC_SEARCH = 0;
    private static final int RC_LOGIN_WATCH = 1;
    public static final int BOOKMARK_RESULT_OK = RESULT_FIRST_USER + 300;
    public static final int REQUEST_CODE = 3001;

    private View rootView;
    private SwipeRefreshLayout refreshLayout;
    private ProgressBar loading;

    private RestaurantsAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    private RestaurantsDataManager dataManager;
    private long tripId;
    private long categoryId;
    private WinterPrefs winterPrefs;

    private Toolbar toolbar;

    private RestaurantsViewModel mViewModel;
    private RestaurantsListFragmentBinding mFragBinding;

    /**
     * Create a new instance of the fragment
     */

    public static RestaurantsListFragment newInstance(long tripId, long categoryId, Toolbar toolbar) {
        RestaurantsListFragment fragment = new RestaurantsListFragment();
        fragment.tripId = tripId;
        fragment.categoryId = categoryId;
        fragment.toolbar = toolbar;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        winterPrefs = WinterPrefs.get(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Load data
        loadRestaurants();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragBinding = RestaurantsListFragmentBinding.inflate(inflater, container, false);
        mViewModel = RestaurantsListActivity.obtainViewModel(getActivity());
        mFragBinding.setViewModel(mViewModel);
        return mFragBinding.getRoot();
    }

    @Override
    public final void onPause() {
        super.onPause();
        this.refreshLayout.setRefreshing(false);
    }

    @Override
    public final void onResume() {
        super.onResume();
    }

    @Override
    public void onRefresh() {
        dataManager.reloadData();
        this.adapter.clear();
        refreshLayout.setRefreshing(true);
    }


    public void loadRestaurants() {
        // load the restaurants
        dataManager = new RestaurantsDataManager(getActivity(), this.tripId, this.categoryId) {
            @Override
            public void onDataLoaded(List<Entity> data) {
                if (data != null && data.size() > 0) {
                    adapter.addAndResort(data);
                    checkEmptyState();
                }
                refreshLayout.setRefreshing(false);
            }
        };
        this.mViewModel.setDataManager(this.dataManager);

        // Loading
        this.loading = (ProgressBar) this.mFragBinding.loading;

        // Refresh
        this.refreshLayout = (SwipeRefreshLayout) this.mFragBinding.swipeContainer;
        this.refreshLayout.setColorSchemeResources(R.color.sunflower_yellow, R.color.neon_blue,
                R.color.lightish_blue, R.color.aqua_marine);
        this.refreshLayout.setOnRefreshListener(this);


        // Adapter
        this.recyclerView = (RecyclerView) mFragBinding.recyclerView;
        this.adapter = new RestaurantsAdapter(getActivity(), this, new ArrayList<Entity>(0), mViewModel);
        this.layoutManager = new LinearLayoutManager(getActivity());
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.addOnScrollListener(new InfiniteScrollListener(layoutManager, dataManager) {
            @Override
            public void onLoadMore() {
                dataManager.loadMore();
                checkEmptyState();
            }
        });
        this.recyclerView.setHasFixedSize(true);
        dataManager.loadData();
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
        handleActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case BOOKMARK_RESULT_OK:
                long id = data.getLongExtra("id", 0);
                int watch = data.getIntExtra("watch", 0);
                adapter.setWatchById(id, watch);
                break;
            case RC_SEARCH:
                // reset the search icon which we hid
                View searchMenuView = toolbar.findViewById(R.id.menu_search);
                if (searchMenuView != null) {
                    searchMenuView.setAlpha(1f);
                }
                break;
        }
    }

    @Override
    public void onEntityClicked(Entity entity) {
        Intent intent = new Intent(getActivity(), RestaurantDetailsActivity.class);
        intent.putExtra("id", entity.id);
        intent.putExtra("name", entity.name);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onBookmarkClicked(Entity entity, boolean isBookmark) {
        if (isBookmark) {
            watchEntity(entity);
        } else {
            unWatchEntity(entity);
        }
    }

    private void watchEntity(Entity entity) {
        if (winterPrefs.isLoggedIn() && winterPrefs.getUser() != null) {
            VisitedPlacesRequest request = new VisitedPlacesRequest(entity.trip_id, winterPrefs.getUser().id, entity.id,"restaurants", null);
            final Call<VisitedPlacesResponse> postWatchCall = winterPrefs.getApi().postWatch(request);
            postWatchCall.enqueue(new Callback<VisitedPlacesResponse>() {
                @Override
                public void onResponse(Call<VisitedPlacesResponse> call, Response<VisitedPlacesResponse> response) {
                    VisitedPlacesResponse visitedPlace = response.body();
                    if (visitedPlace != null) {
                        updateEntity(entity.id, true);
                    }
                }

                @Override
                public void onFailure(Call<VisitedPlacesResponse> call, Throwable t) {

                }
            });

        } else {
            needsLogin(this.rootView, RC_LOGIN_WATCH);
        }
    }

    private void unWatchEntity(Entity entity) {
        if (winterPrefs.isLoggedIn() && winterPrefs.getUser() != null) {
            final Call<Void> postWatchCall = winterPrefs.getApi().postUnWatch(entity.visitedPlace != null ? entity.visitedPlace.id : 0);
            postWatchCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 200) {
                        updateEntity(entity.id, false);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });

        } else {
            needsLogin(this.rootView, RC_LOGIN_WATCH);
        }
    }

    private void updateEntity(Long id, boolean isBookmark) {
        adapter.setWatchById(id, isBookmark);
    }

    private void needsLogin(View triggeringView, int requestCode) {
        Intent login = new Intent(getActivity(), WinterLogin.class);
        MorphTransform.addExtras(login,
                ContextCompat.getColor(getActivity(), R.color.background_light),
                getResources().getDimensionPixelSize(R.dimen.dialog_corners));
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation
                (getActivity(), triggeringView, getString(R.string.transition_dribbble_login));
        startActivityForResult(login, requestCode, options.toBundle());
    }

    private void checkEmptyState() {
        if (adapter.getDataItemCount() == 0) {
            // if grid is empty check whether we're loading or if no filters are selected
            loading.setVisibility(View.GONE);
            toolbar.setTranslationZ(0f);
        } else {
            loading.setVisibility(View.GONE);
        }
    }
}

