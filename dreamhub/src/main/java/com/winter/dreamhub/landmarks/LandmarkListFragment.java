package com.winter.dreamhub.landmarks;

import android.app.ActivityOptions;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;

import com.winter.dreamhub.R;
import com.winter.dreamhub.api.EventCallbacks;
import com.winter.dreamhub.api.prefs.WinterPrefs;
import com.winter.dreamhub.api.service.LandmarksDataManager;
import com.winter.dreamhub.api.service.model.Entity;
import com.winter.dreamhub.api.service.request.VisitedPlacesRequest;
import com.winter.dreamhub.api.service.response.VisitedPlacesResponse;
import com.winter.dreamhub.auth.WinterLogin;
import com.winter.dreamhub.util.DisplayUtils;
import com.winter.dreamhub.util.ImageUtils;
import com.winter.dreamhub.viewmodels.ViewModelFactory;
import com.winter.dreamhub.widget.PreCachedLinearLayoutManager;
import com.winter.dreamhub.widget.VerticalScrollListener;
import com.winter.dreamhub.widget.recyclerview.InfiniteScrollListener;
import com.winter.dreamhub.widget.transitions.MorphTransform;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_FIRST_USER;
import static com.winter.dreamhub.util.LogUtils.makeLogTag;

/**
 * Created by hoaxoan on 10/28/2016.
 */

public class LandmarkListFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, EventCallbacks {
    public static final String TAG = makeLogTag(LandmarkListFragment.class);

    private static final Integer MIN_VISIBILITY_FOR_LOGGING_MS = Integer.valueOf(500);
    private static final int RC_SEARCH = 0;
    private static final int RC_LOGIN_WATCH = 1;
    public static final int BOOKMARK_RESULT_OK = RESULT_FIRST_USER + 200;
    public static final int REQUEST_CODE = 2001;

    private LandmarksAdapter adapter;
    RecyclerView landmarks;
    LinearLayoutManager layoutManager;


    private SwipeRefreshLayout refreshLayout;
    View rootView;

    private long moodId;
    private long tripId;
    private LandmarksDataManager dataManager;
    private ProgressBar loading;
    private WinterPrefs winterPrefs;

    public static LandmarkListFragment newInstance(long moodId, long tripId, ProgressBar loading) {
        Bundle bundle = new Bundle();
        LandmarkListFragment fragment = new LandmarkListFragment();
        fragment.moodId = moodId;
        fragment.tripId = tripId;
        fragment.loading = loading;
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // RecyclerView
        this.landmarks = ((RecyclerView) this.rootView.findViewById(R.id.recycler_view));

        // Refresh
        this.refreshLayout = ((SwipeRefreshLayout) this.rootView.findViewById(R.id.swipe_container));
        this.refreshLayout.setColorSchemeResources(R.color.sunflower_yellow, R.color.neon_blue,
                R.color.lightish_blue, R.color.aqua_marine);
        this.refreshLayout.setOnRefreshListener(this);

        // load Landmarks
        loadLandmarks();
    }

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        winterPrefs = WinterPrefs.get(getActivity());
    }

    @Override
    public final void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //inflater.inflate(R.menu.landmark_list_menu, paramMenu);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.landmark_list_fragment, container, false);
        return this.rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        dataManager.reloadData();
        this.adapter.clear();
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.refreshLayout.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        //mViewModel.start();
    }

    public void loadLandmarks() {
        // load the landmark
        dataManager = new LandmarksDataManager(getActivity(), this.tripId, this.moodId) {
            @Override
            public void onDataLoaded(List<Entity> data) {
                if (refreshLayout != null) {
                    refreshLayout.setRefreshing(false);
                }

                if (data != null && data.size() > 0) {
                    adapter.addAndResort(data);
                    checkEmptyState();
                }
            }
        };

        this.adapter = new LandmarksAdapter(getActivity(), this, dataManager);
        this.layoutManager = new PreCachedLinearLayoutManager(getActivity(), 1, false, new DisplayUtils(getActivity().getWindowManager()));
        this.landmarks.setLayoutManager(this.layoutManager);
        this.landmarks.setAdapter(this.adapter);
        this.landmarks.addOnScrollListener(new InfiniteScrollListener(layoutManager, dataManager) {
            @Override
            public void onLoadMore() {
                dataManager.loadMore();
                checkEmptyState();
            }
        });
        this.landmarks.setHasFixedSize(true);
        dataManager.loadData();
    }

    private void checkEmptyState() {
        if (adapter.getDataItemCount() == 0) {
            // if grid is empty check whether we're loading or if no filters are selected
            loading.setVisibility(View.GONE);
        } else {
            loading.setVisibility(View.GONE);
        }
    }

    @Override
    public void onEntityClicked(Entity entity) {
        Intent intent = new Intent(getActivity(), LandmarkDetailsActivity.class);
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
            VisitedPlacesRequest request = new VisitedPlacesRequest(entity.trip_id, winterPrefs.getUser().id, entity.id, "hotels", null);
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
        }
    }

}
