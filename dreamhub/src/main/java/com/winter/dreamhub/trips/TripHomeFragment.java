package com.winter.dreamhub.trips;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.common.base.Preconditions;
import com.winter.dreamhub.R;
import com.winter.dreamhub.activity.BaseActivity;
import com.winter.dreamhub.api.service.WinterService;
import com.winter.dreamhub.api.service.model.Mood;
import com.winter.dreamhub.hotels.HotelsActivity;
import com.winter.dreamhub.landmarks.ThingsToDoActivity;
import com.winter.dreamhub.restaurants.RestaurantsActivity;
import com.winter.dreamhub.api.prefs.WinterPrefs;
import com.winter.dreamhub.api.service.model.Category;
import com.winter.dreamhub.api.service.model.Trip;
import com.winter.dreamhub.savedplaces.SavedPlacesActivity;
import com.winter.dreamhub.tnts.TntActivity;
import com.winter.dreamhub.widget.FadingImageView;
import com.winter.dreamhub.widget.FixedAspectRatioFrameLayout;
import com.winter.dreamhub.widget.ObservableScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.winter.dreamhub.util.LogUtils.makeLogTag;

/**
 * Created by hoaxoan on 10/27/2016.
 */

public class TripHomeFragment extends ScrollableFragment
        implements ObservableScrollView.OnScrollChangeListener {
    public static final String TAG = makeLogTag(TripHomeFragment.class);

    private View contentView;
    LinearLayout tripHeaderOverlay;
    private FadingImageView tripHeroImage;

    private LinearLayout leftColumn;

    ProgressBar progress;
    private LinearLayout rightColumn;
    boolean showLimited;

    private Toolbar toolbar;
    private FixedAspectRatioFrameLayout photoHolder;

    private WinterPrefs winterPrefs;
    private long tripId;
    private Trip trip;
    private String tripName;

    private Map<Long, CardView> tntTiles = new HashMap();
    private static Map<Long, Integer> tntTileThumbnails = new HashMap() {

    };

    static {
        tntTileThumbnails.put(1l, R.drawable.ic_your_places);
        tntTileThumbnails.put(2l, R.drawable.ic_things_to_do);
        tntTileThumbnails.put(3l, R.drawable.ic_reservations);
        tntTileThumbnails.put(4l, R.drawable.ic_reservations);
        tntTileThumbnails.put(5l, R.drawable.ic_your_places);
    }

    /**
     * Create a new instance of the fragment
     */
    public static TripHomeFragment newInstance(long tripId, String tripName) {
        TripHomeFragment fragment = new TripHomeFragment();
        fragment.tripId = tripId;
        fragment.tripName = tripName;
        return fragment;
    }

    private final void defaultCardsSetup() {
        if (!this.showLimited) {
            this.progress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        winterPrefs = WinterPrefs.get(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.toolbar = ((BaseActivity) getActivity()).mActionBarToolbar;

        this.contentView = LayoutInflater.from(getActivity()).inflate(R.layout.trip_home_fragment, this.itemsContainer, false);

        this.photoHolder = (FixedAspectRatioFrameLayout) this.contentView.findViewById(R.id.trip_hero_image_frame);
        this.photoHolder.setAspectRatio(this.imageUtils.getHeroImageAspectRatio());
        ((ImageView) this.contentView.findViewById(R.id.trip_hero_image_overlay)).setImageResource(R.drawable.top_gradient_shape);
        this.tripHeroImage = ((FadingImageView) this.contentView.findViewById(R.id.trip_hero_image));
        this.tripHeaderOverlay = ((LinearLayout) this.contentView.findViewById(R.id.trip_header_overlay));

        this.photosHolderHeight = this.photoHolder.getHeight();
        this.toolbarHolderHeight = this.photoHolder.getHeight();
        this.underscoreTabHeight = this.getResources().getDimensionPixelSize(R.dimen.bottom_shadow_height);

        this.progress = ((ProgressBar) this.contentView.findViewById(R.id.spinner));
        this.leftColumn = ((LinearLayout) this.contentView.findViewById(R.id.left_column));
        this.rightColumn = ((LinearLayout) this.contentView.findViewById(R.id.right_column));

        this.itemsContainer.addView(this.contentView);

        loadTripDetails();

        this.rootView.setOnScrollListener(this);

    }

    public final boolean isDestinationSet() {
        return true;
    }

    private void loadTripDetails() {
        final Call<Trip> tripCall = winterPrefs.getApi().getTrip(tripId);
        tripCall.enqueue(new Callback<Trip>() {
            @Override
            public void onResponse(Call<Trip> call, Response<Trip> response) {
                trip = response.body();
                bindTrip();
            }

            @Override
            public void onFailure(Call<Trip> call, Throwable t) {
            }
        });
    }

    private void bindTrip() {
        if (trip != null && trip.photo != null) {
            String imgUrl = WinterService.ENDPOINT + trip.photo;

            Glide.with(this).load(imgUrl)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(this.tripHeroImage);
        }


        if (isDestinationSet()) {
            initUI();
        }

    }

    final void initUI() {
        if (trip == null || trip.categories == null) {
            return;
        }
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        float aspectRation = 1.0F;
        for (int i = 0; i < trip.categories.size(); i++) {
            Category category = trip.categories.get(i);
            LinearLayout viewGroup = this.leftColumn;

            if (i % 2 == 1) {
                viewGroup = this.leftColumn;
            } else {
                viewGroup = this.rightColumn;
            }

            if ((i + 1) % 4 >= 2) {
                aspectRation = 1.0F;
            } else {
                aspectRation = 0.7276F;
            }
            final CardView cardView = (CardView) layoutInflater.inflate(R.layout.static_tile, (ViewGroup) viewGroup, false);
            this.tntTiles.put(category.id, cardView);
            viewGroup.addView(cardView);

            ImageView tileThumbnail = ((ImageView) cardView.findViewById(R.id.static_tile_thumbnail));
            TextView tileTitle = ((TextView) cardView.findViewById(R.id.static_tile_title));
            FixedAspectRatioFrameLayout aspectRatioFrameLayout = ((FixedAspectRatioFrameLayout) cardView.findViewById(R.id.holder));

            int iconResId = tntTileThumbnails.get(category.id);
            tileThumbnail.setImageResource(iconResId);
            tileTitle.setText(category.name);
            aspectRatioFrameLayout.setAspectRatio(aspectRation);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(trip.id, category.id, category.name, trip.moods);
                }
            });

        }
    }

    private void startActivity(long tripId, long categoryId, String categoryName, List<Mood> moods) {
        switch ((int) categoryId) {
            case 1:
                Intent savedPlaces = new Intent(getActivity(), SavedPlacesActivity.class);
                savedPlaces.putExtra("trip_id", (long) Preconditions.checkNotNull(tripId));
                savedPlaces.putExtra("category_id", (long) Preconditions.checkNotNull(categoryId));
                savedPlaces.putExtra("category_name", Preconditions.checkNotNull(categoryName));
                savedPlaces.putParcelableArrayListExtra("moods", new ArrayList<>(moods));
                startActivity(savedPlaces);
                break;
            case 2:
                Intent thingsToDo = new Intent(getActivity(), ThingsToDoActivity.class);
                thingsToDo.putExtra("trip_id", (long) Preconditions.checkNotNull(tripId));
                thingsToDo.putExtra("category_id", (long) Preconditions.checkNotNull(categoryId));
                thingsToDo.putExtra("category_name", Preconditions.checkNotNull(categoryName));
                thingsToDo.putParcelableArrayListExtra("moods", new ArrayList<>(moods));
                startActivity(thingsToDo);
                break;
            case 3:
                Intent foodDrink = new Intent(getActivity(), RestaurantsActivity.class);
                foodDrink.putExtra("trip_id", (long) Preconditions.checkNotNull(tripId));
                foodDrink.putExtra("category_id", (long) Preconditions.checkNotNull(categoryId));
                foodDrink.putExtra("category_name", Preconditions.checkNotNull(categoryName));
                foodDrink.putParcelableArrayListExtra("moods", new ArrayList<>(moods));
                startActivity(foodDrink);
                break;
            case 4:
                Intent hotels = new Intent(getActivity(), HotelsActivity.class);
                hotels.putExtra("trip_id", (long) Preconditions.checkNotNull(tripId));
                hotels.putExtra("category_id", (long) Preconditions.checkNotNull(categoryId));
                hotels.putExtra("category_name", Preconditions.checkNotNull(categoryName));
                startActivity(hotels);
                break;
            case 5:
                Intent tnt = new Intent(getActivity(), TntActivity.class);
                tnt.putExtra("trip_id", (long) Preconditions.checkNotNull(tripId));
                tnt.putExtra("category_id", (long) Preconditions.checkNotNull(categoryId));
                tnt.putExtra("category_name", Preconditions.checkNotNull(categoryName));
                tnt.putParcelableArrayListExtra("moods", new ArrayList<>(moods));
                startActivity(tnt);
                break;
            default:
                Intent intent = new Intent(getActivity(), TntActivity.class);
                intent.putExtra("trip_id", (long) Preconditions.checkNotNull(tripId));
                intent.putExtra("category_id", (long) Preconditions.checkNotNull(categoryId));
                intent.putExtra("category_name", Preconditions.checkNotNull(categoryName));
                intent.putParcelableArrayListExtra("moods", new ArrayList<>(moods));
                startActivity(intent);
                break;
        }
    }

    private final void setupStaticTile(CardView cardView, int titleResId, int iconResId, float aspectRation) {
        Preconditions.checkNotNull(cardView);
        ((ImageView) cardView.findViewById(R.id.static_tile_thumbnail)).setImageResource(iconResId);
        ((TextView) cardView.findViewById(R.id.static_tile_title)).setText(getResources().getString(titleResId));
        ((FixedAspectRatioFrameLayout) cardView.findViewById(R.id.holder)).setAspectRatio(aspectRation);
    }

    @Override
    public final void onPause() {
        super.onPause();
    }

    @Override
    public final void onResume() {
        super.onResume();
    }

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (t >= this.photosHolderHeight - this.toolbarHolderHeight + this.underscoreTabHeight) {
            setSolidBackOverflowStatusbar();
        } else {
            setTransparentBackOverflowStatusbar();
        }
    }

    private int photosHolderHeight;
    private int toolbarHolderHeight;
    private int underscoreTabHeight;

    private final void setSolidBackOverflowStatusbar() {
        this.toolbar.setNavigationIcon(R.drawable.quantum_ic_arrow_back_grey600_24);
        this.toolbar.setOverflowIcon(this.getResources().getDrawable(R.drawable.quantum_ic_drawer_grey600_24));
        this.toolbar.setBackgroundColor(this.getResources().getColor(android.R.color.white));
        ((BaseActivity) getActivity()).setStatusBarColorIfNeeded(R.color.quantum_grey400);
        this.toolbar.setTitleTextColor(this.getResources().getColor(android.R.color.black));
    }

    private final void setTransparentBackOverflowStatusbar() {
        this.toolbar.setNavigationIcon(R.drawable.quantum_ic_arrow_back_white_24);
        this.toolbar.setOverflowIcon(this.getResources().getDrawable(R.drawable.quantum_ic_drawer_white_24));
        this.toolbar.setBackgroundColor(this.getResources().getColor(android.R.color.transparent));
        ((BaseActivity) getActivity()).setStatusBarColorIfNeeded(R.color.half_transparent_status_bar);
        this.toolbar.setTitleTextColor(this.getResources().getColor(android.R.color.white));
    }


}
