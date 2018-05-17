package com.winter.dreamhub.savedplaces;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.base.Preconditions;
import com.winter.dreamhub.R;
import com.winter.dreamhub.api.EventCallbacks;
import com.winter.dreamhub.api.prefs.WinterPrefs;
import com.winter.dreamhub.api.service.SavePlacesDataManager;
import com.winter.dreamhub.api.service.WinterService;
import com.winter.dreamhub.api.service.model.Entity;
import com.winter.dreamhub.api.service.request.VisitedPlacesRequest;
import com.winter.dreamhub.api.service.response.VisitedPlacesResponse;
import com.winter.dreamhub.auth.WinterLogin;
import com.winter.dreamhub.databinding.SavedPlacesFragmentBinding;
import com.winter.dreamhub.hotels.HotelDetailsActivity;
import com.winter.dreamhub.restaurants.RestaurantDetailsActivity;
import com.winter.dreamhub.landmarks.LandmarkDetailsActivity;
import com.winter.dreamhub.widget.recyclerview.InfiniteScrollListener;
import com.winter.dreamhub.widget.transitions.MorphTransform;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.winter.dreamhub.util.LogUtils.makeLogTag;

/**
 * Created by hoaxoan on 8/9/2017.
 */
public class SavedPlacesFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, EventCallbacks {
    public static final String TAG = makeLogTag(SavedPlacesFragment.class);
    private static final int RC_LOGIN_WATCH = 1;

    private long tripId;
    private String relatedType;
    private SwipeRefreshLayout refreshLayout;

    private SavePlacesAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private SavePlacesDataManager dataManager;

    private WinterPrefs winterPrefs;

    private SavePlacesViewModel mViewModel;

    private SavedPlacesFragmentBinding mFragBinding;

    /**
     * Create a new instance of the fragment
     */
    public static SavedPlacesFragment newInstance(long tripId, String relatedType) {
        SavedPlacesFragment fragment = new SavedPlacesFragment();
        fragment.tripId = tripId;
        fragment.relatedType = relatedType;
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

        // Data
        this.dataManager = new SavePlacesDataManager(getActivity(), this.tripId) {
            @Override
            public void onDataLoaded(List<Entity> data) {
                if (refreshLayout != null) {
                    refreshLayout.setRefreshing(false);
                }

                if (data != null && data.size() > 0) {
                    adapter.addAndResort(data);
                }
            }
        };
        this.mViewModel.setDataManager(this.dataManager);

        // Refresh
        this.refreshLayout = (SwipeRefreshLayout) this.mFragBinding.swipeContainer;
        this.refreshLayout.setColorSchemeResources(R.color.sunflower_yellow, R.color.neon_blue,
                R.color.lightish_blue, R.color.aqua_marine);
        this.refreshLayout.setOnRefreshListener(this);

        // Adapter
        this.recyclerView = (RecyclerView) mFragBinding.recyclerView;
        this.adapter = new SavePlacesAdapter(getActivity(),this, new ArrayList<Entity>(0), mViewModel);
        this.layoutManager = new LinearLayoutManager(getActivity());
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.addOnScrollListener(new InfiniteScrollListener(layoutManager, dataManager) {
            @Override
            public void onLoadMore() {
                dataManager.loadMore();
            }
        });
        this.recyclerView.setHasFixedSize(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragBinding = SavedPlacesFragmentBinding.inflate(inflater, container, false);
        mViewModel = SavedPlacesActivity.obtainViewModel(getActivity());
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
        mViewModel.start();
    }

    @Override
    public void onRefresh() {
        this.adapter.clear();
        this.refreshLayout.setRefreshing(true);
        mViewModel.reloadData();
    }

    @Override
    public void onEntityClicked(Entity entity) {
        startActivityDetails(entity);
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
            needsLogin(mFragBinding.getRoot(), RC_LOGIN_WATCH);
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
            needsLogin(mFragBinding.getRoot(), RC_LOGIN_WATCH);
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

    private void startActivityDetails(Entity entity) {
        Intent intent = null;
        if(entity.type == WinterService.TYPE_LANDMARKS){
            intent = new Intent(getActivity(), LandmarkDetailsActivity.class);
        } else if(entity.type == WinterService.TYPE_HOTELS){
            intent = new Intent(getActivity(), HotelDetailsActivity.class);
        } else if(entity.type == WinterService.TYPE_RESTAURANTS){
            intent = new Intent(getActivity(), RestaurantDetailsActivity.class);
        }

        if(intent == null){
            return;
        }

        intent.putExtra("id", Preconditions.checkNotNull(entity.id));
        intent.putExtra("name", Preconditions.checkNotNull(entity.name));
        getActivity().startActivity(intent);
    }
}
