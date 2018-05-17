package com.winter.dreamhub.hotels;

import android.Manifest;
import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.winter.dreamhub.R;
import com.winter.dreamhub.activity.BaseActivity;
import com.winter.dreamhub.reviews.SubmitReviewActivity;
import com.winter.dreamhub.trips.PhotoPagerAdapter;
import com.winter.dreamhub.searchs.ReviewsAdapter;
import com.winter.dreamhub.api.prefs.WinterPrefs;
import com.winter.dreamhub.api.service.WinterService;
import com.winter.dreamhub.api.service.model.AggregateRating;
import com.winter.dreamhub.api.service.model.Amenity;
import com.winter.dreamhub.api.service.model.Entity;
import com.winter.dreamhub.api.service.model.HotelBooking;
import com.winter.dreamhub.api.service.model.OpenHoursForDay;
import com.winter.dreamhub.api.service.model.Review;
import com.winter.dreamhub.api.service.request.VisitedPlacesRequest;
import com.winter.dreamhub.api.service.response.VisitedPlacesResponse;
import com.winter.dreamhub.auth.WinterLogin;
import com.winter.dreamhub.trips.UriIntendingOnClickListener;
import com.winter.dreamhub.util.AnimUtils;
import com.winter.dreamhub.util.MapUtils;
import com.winter.dreamhub.viewmodels.ViewModelFactory;
import com.winter.dreamhub.widget.ElasticDragDismissFrameLayout;
import com.winter.dreamhub.widget.FixedAspectRatioFrameLayout;
import com.winter.dreamhub.widget.InkPageIndicator;
import com.winter.dreamhub.widget.ToggleFrameLayout;
import com.winter.dreamhub.widget.transitions.MorphTransform;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.winter.dreamhub.hotels.HotelsFragment.BOOKMARK_RESULT_OK;
import static com.winter.dreamhub.landmarks.LandmarksAdapter.DECIMAL_FORMAT;
import static com.winter.dreamhub.hotels.AmenitiesActivity.AMENITY_ICON_RES_ID;
import static com.winter.dreamhub.util.AnimUtils.getLinearOutSlowInInterpolator;

/**
 * Created by hoaxoan on 11/19/2016.
 */

public class HotelDetailsActivity extends BaseActivity
        implements OnMapReadyCallback, HotelDetailsCallback {
    public final static String EXTRA_LANDMARK = "EXTRA_LANDMARK";
    private static final int RC_LOGIN_WATCH = 1;
    private static final int MAX_ITEM_AMENITIES = 4;
    public static final int SUBMIT_REVIEW_REQUEST = 1;

    @BindView(R.id.draggable_frame)
    ElasticDragDismissFrameLayout draggableFrame;
    private ElasticDragDismissFrameLayout.SystemChromeFader chromeFader;

    public View headerView;
    private View loadingLayout;
    private View contentLayout;

    // Photo
    private int underscoreTabHeight = 0;
    FrameLayout toolbarHolder;
    FixedAspectRatioFrameLayout photosHolder;
    ViewPager viewPager;
    InkPageIndicator pageIndicator;

    //Title
    TextView title;
    TextView direction;
    TextView phone;
    TextView website;

    // RateView
    private RatingBar ratingBar;
    private TextView averageRate;
    private TextView totalEvaluations;

    // Rate
    View fullStarView;
    View emptyStarView;

    View landmarkDescriptionLayout;

    TextView entityDescription;
    TextView entityDescriptionAttribution;

    // Map
    MapUtils mapUtils = new MapUtils();
    SupportMapFragment mapFragment;

    TextView landmarkPhone;
    TextView landmarkAddress;

    // Amenities
    private LinearLayout amenitiesLayout;

    // Prices
    private LinearLayout pricesLayout;
    private TextView titlePrices;
    private TextView prices;
    private MaterialButton viewDeal;

    private WinterPrefs winterPrefs;
    private long id;
    private Entity entity;

    // Comment
    public ListView commentsList;
    private ReviewsAdapter commentsAdapter;

    // Summary Reviews
    private View reviewSummary;
    private ProgressBar[] chartBars;
    private TextView averageReviewRating;
    private RatingBar summaryReviewRating;
    private TextView evalationReviewRating;

    private RatingBar reviewRatingView;

    private LinearLayout userReviewRatingLayout;
    private TextView userNameReviewRating;
    private RatingBar userReviewRating;

    public Toolbar getToolbar() {
        return this.mActionBarToolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotel_details_activity);
        getWindow().getSharedElementReturnTransition().addListener(shotReturnHomeListener);

        winterPrefs = WinterPrefs.get(this);
        ButterKnife.bind(this);
        underscoreTabHeight = getResources().getDimensionPixelSize(R.dimen.bottom_shadow_height);

        this.id = getIntent().getLongExtra("id", 0);

        mActionBarToolbar = getActionBarToolbar();
        mActionBarToolbar.setNavigationIcon(R.drawable.quantum_ic_arrow_back_grey600_24);
        mActionBarToolbar.setTitleTextColor(getResources().getColor(R.color.quantum_black_secondary_text));
        mActionBarToolbar.setTitle("");
        setSupportActionBar(mActionBarToolbar);

        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        commentsList = (ListView) this.findViewById(R.id.landmark_reviews);
        this.headerView = getLayoutInflater().inflate(R.layout.hotel_details_item,
                commentsList, false);
        commentsList.addHeaderView(this.headerView);
        commentsList.setOnScrollListener(scrollListener);

        chromeFader = new ElasticDragDismissFrameLayout.SystemChromeFader(this) {
            @Override
            public void onDragDismissed() {
                expandImageAndFinish();
            }
        };

        this.loadingLayout = this.findViewById(R.id.loading_layout);
        this.contentLayout = this.findViewById(R.id.content_layout);

        this.toolbarHolder = ((FrameLayout) this.findViewById(R.id.toolbar_holder));
        this.photosHolder = ((FixedAspectRatioFrameLayout) this.headerView.findViewById(R.id.photos_holder));
        this.viewPager = ((ViewPager) this.headerView.findViewById(R.id.image_pages));
        this.pageIndicator = ((InkPageIndicator) this.headerView.findViewById(R.id.indicator));
        this.viewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.spacing_normal));

        this.title = (TextView) this.headerView.findViewById(R.id.title);
        this.direction = ((TextView) this.headerView.findViewById(R.id.directions));
        this.phone = ((TextView) this.headerView.findViewById(R.id.phone));
        this.website = ((TextView) this.headerView.findViewById(R.id.website));

        this.ratingBar = ((RatingBar) this.headerView.findViewById(R.id.landmark_rating_bar));
        this.averageRate = ((TextView) this.headerView.findViewById(R.id.landmark_average_rate));
        this.totalEvaluations = ((TextView) this.headerView.findViewById(R.id.landmark_total_evaluations));

        this.fullStarView = this.headerView.findViewById(R.id.entity_full_star);
        this.emptyStarView = this.headerView.findViewById(R.id.entity_empty_star);

        this.landmarkDescriptionLayout = this.headerView.findViewById(R.id.landmark_description_layout);
        this.entityDescription = ((TextView) this.headerView.findViewById(R.id.entity_description));
        this.entityDescriptionAttribution = ((TextView) this.headerView.findViewById(R.id.entity_description_attribution));

        this.mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.landmark_map);
        //this.mapView = (ImageView) this.headerView.findViewById(R.id.landmark_map);
        this.landmarkAddress = ((TextView) this.headerView.findViewById(R.id.landmark_address));
        this.landmarkPhone = ((TextView) this.headerView.findViewById(R.id.landmark_phone));

        this.amenitiesLayout = (LinearLayout) this.headerView.findViewById(R.id.amenities_layout);
        this.amenitiesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAmenitiesActivity();
            }
        });

        this.pricesLayout = ((LinearLayout) this.findViewById(R.id.prices_container));
        this.titlePrices = ((TextView) this.findViewById(R.id.title_prices));
        this.prices = ((TextView) this.findViewById(R.id.text_prices));
        this.viewDeal = ((MaterialButton) this.findViewById(R.id.view_deal));
        this.viewDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTopDealActivity();
            }
        });

        // Review
        this.reviewRatingView = (RatingBar) this.headerView.findViewById(R.id.review_textbox);

        // Review summary
        this.reviewSummary = this.headerView.findViewById(R.id.review_summary);
        this.averageReviewRating = (TextView) this.headerView.findViewById(R.id.average);
        this.summaryReviewRating = (RatingBar) this.headerView.findViewById(R.id.review_summary_rating_bar);
        this.evalationReviewRating = (TextView) this.headerView.findViewById(R.id.review_summary_total_evaluations);

        this.userReviewRatingLayout = (LinearLayout) this.headerView.findViewById(R.id.review_rate_container);
        this.userNameReviewRating = (TextView) this.headerView.findViewById(R.id.authorname_textbox);
        this.userReviewRating = (RatingBar) this.headerView.findViewById(R.id.review_summary_rating_bar);

        loadEntityDetails(savedInstanceState);

        subscribeEntityChanges();
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

    private void loadEntityDetails(@Nullable Bundle savedInstanceState) {
        final Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_LANDMARK)) {
            entity = intent.getParcelableExtra(EXTRA_LANDMARK);
            bindEntity(true, savedInstanceState != null);
        } else {
            final Call<Entity> landmarkCall = winterPrefs.getApi().getEntity(id);
            landmarkCall.enqueue(new Callback<Entity>() {
                @Override
                public void onResponse(Call<Entity> call, Response<Entity> response) {
                    entity = response.body();
                    bindEntity(false, true);
                }

                @Override
                public void onFailure(Call<Entity> call, Throwable t) {
                }
            });
        }
    }

    private void bindEntity(final boolean postponeEnterTransition, final boolean animateFabManually) {
        if (entity == null) return;
        mapUtils = new MapUtils();

        this.title.setText(entity.name);
        this.entityDescription.setText(entity.description);

        this.viewPager.setAdapter(new PhotoPagerAdapter(this, entity.name, entity.photos));
        if (entity.photos != null && entity.photos.size() > 0) {
            pageIndicator.setViewPager(this.viewPager);
        }

        // Rate
        setRatingHistograms();

        // SavedPlaces
        setVisitedPlaces();

        // Direction
        setDirectionCallWebsites();

        // Map
        setMap();

        // Info
        setLandmarkContacts();

        // Amenities
        setAmenities();

        // Histograms
        setSummaryHistograms();

        // Reviews
        loadReviews();

        // TopDeal
        setTopDeal();
    }

    private void setRatingHistograms() {
        this.ratingBar.setRating(entity.ratingHistogram);
        this.averageRate.setText(DECIMAL_FORMAT.format(entity.ratingHistogram));
        this.totalEvaluations.setText(getResources().getQuantityString(R.plurals.reviews, entity.reviewsCount, new Object[]{Integer.valueOf(entity.reviewsCount)}));
    }

    private void setVisitedPlaces() {
        if (entity.watch == 1) {
            this.fullStarView.setVisibility(View.VISIBLE);
            this.emptyStarView.setVisibility(View.GONE);
        } else {
            this.fullStarView.setVisibility(View.GONE);
            this.emptyStarView.setVisibility(View.VISIBLE);
        }

        this.fullStarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unWatchEntity();
            }
        });
        this.emptyStarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                watchEntity();
            }
        });

    }

    private void setDirectionCallWebsites() {
        this.direction.setOnClickListener(new UriIntendingOnClickListener(this,
                this.mapUtils.getMapIntentUri(entity.latE7, entity.lngE7)));

        String phoneNumber = "tel://".concat(entity.phone);
        this.phone.setOnClickListener(
                new UriIntendingOnClickListener(this, Uri.parse(phoneNumber), "android.intent.action.DIAL"));

        this.website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = entity.website;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });

    }

    private void setLandmarkContacts() {

        this.landmarkAddress.setText(entity.address);
        this.landmarkAddress = ((TextView) this.headerView.findViewById(R.id.landmark_address));
        this.landmarkAddress.setOnClickListener(
                new UriIntendingOnClickListener(this, mapUtils.getMapIntentUri(entity.latE7, entity.lngE7)));

        this.landmarkPhone.setText(entity.phone);
        String phoneNumber = "tel://".concat(entity.phone);
        this.landmarkPhone.setOnClickListener(
                new UriIntendingOnClickListener(this, Uri.parse(phoneNumber), "android.intent.action.DIAL"));

        ToggleFrameLayout toggleFrameLayout = (ToggleFrameLayout) this.headerView.findViewById(R.id.landmark_open_hours_block);
        LinearLayout currentDayOpenHours = (LinearLayout) this.headerView.findViewById(R.id.container_current_day_open_hours);
        TextView currentHoursView = (TextView) this.headerView.findViewById(R.id.landmark_current_day_open_hours);

        List<OpenHoursForDay> openHoursForDays = entity.openHoursForDays;
        String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(new Date().getTime());
        if (openHoursForDays != null && openHoursForDays.size() > 0) {
            String currentHours = String.format("%.2f - %.2f", new Object[]{openHoursForDays.get(0).startHour, openHoursForDays.get(0).endHour});
            currentHoursView.setText(getString(R.string.PLACE_CLOSING_SOON, new Object[]{currentHours}));

            LinearLayout allDaysOpenHours = (LinearLayout) this.headerView.findViewById(R.id.container_all_days_open_hours);
            TableLayout allHoursView = (TableLayout) this.headerView.findViewById(R.id.landmark_all_days_open_hours);
            for (int i = 0; i < openHoursForDays.size(); i++) {
                OpenHoursForDay openHoursForDay = openHoursForDays.get(i);
                TableRow tableRow = (TableRow) LayoutInflater.from(this).inflate(R.layout.landmark_details_open_hours_row, (ViewGroup) allHoursView, false);
                TextView dayView = (TextView) tableRow.findViewById(R.id.day);
                dayView.setText(openHoursForDay.name);

                TextView openHoursView = (TextView) tableRow.findViewById(R.id.open_hours);
                List<String> intervalTime = new ArrayList<String>();
                intervalTime.add(String.format("%.2f - %.2f", new Object[]{openHoursForDay.startHour, openHoursForDay.endHour}));
                openHoursView.setText(TextUtils.join("\n", intervalTime));

                allHoursView.addView(tableRow);

                if (dayOfWeek.equalsIgnoreCase(openHoursForDay.name)) {
                    dayView.setTypeface(null, 1);
                    openHoursView.setTypeface(null, 1);
                }

            }

            toggleFrameLayout.firstView = currentDayOpenHours;
            toggleFrameLayout.secondView = allDaysOpenHours;
            toggleFrameLayout.setVisibility(View.VISIBLE);
        } else {
            toggleFrameLayout.setVisibility(View.GONE);
        }
    }

    private void setAmenities() {
        if (entity.amenities == null || entity.amenities.size() == 0) {
            amenitiesLayout.setVisibility(View.GONE);
            return;
        }

        int numAmenities = MAX_ITEM_AMENITIES;
        amenitiesLayout.setVisibility(View.VISIBLE);
        if (numAmenities > entity.amenities.size()) {
            numAmenities = entity.amenities.size();
        }

        amenitiesLayout.removeAllViews();
        for (int i = 0; i < numAmenities; i++) {
            Amenity amenity = entity.amenities.get(i);
            View view = getLayoutInflater().inflate(R.layout.amenities_item_icon, amenitiesLayout, false);
            ImageView amenityIcon = (ImageView) view.findViewById(R.id.amenity_icon);
            int itemId = (int) amenity.position;
            if (itemId < 0 || itemId >= AMENITY_ICON_RES_ID.length) {
                itemId = 0;
            }
            amenityIcon.setImageResource(AMENITY_ICON_RES_ID[itemId]);
            amenitiesLayout.addView(view);
        }

        if (numAmenities < entity.amenities.size()) {
            View view = getLayoutInflater().inflate(R.layout.amenities_item_more, amenitiesLayout, false);
            TextView amenityIcon = (TextView) view.findViewById(R.id.text_more);
            amenityIcon.setText(String.format("+ %d", entity.amenities.size()));
            amenitiesLayout.addView(view);
        }
    }

    private void setTopDeal() {
        if (entity.hotel == null || entity.hotel.prices <= 0) {
            pricesLayout.setVisibility(View.GONE);
            return;
        }
        pricesLayout.setVisibility(View.VISIBLE);

        NumberFormat nf = NumberFormat.getInstance();
        if (entity.hotel == null || entity.hotel.hotelBookings == null || entity.hotel.hotelBookings.size() <= 0) {
            titlePrices.setText(getString(R.string.price_per_night));
            prices.setText(nf.format(entity.hotel != null ? entity.hotel.prices : null));
            viewDeal.setText(getString(R.string.call_now_label));
        } else {
            titlePrices.setText(getString(R.string.top_deal_label));
            prices.setText(nf.format(entity.hotel.prices));
            viewDeal.setText(getString(R.string.view_deal_label));
        }

    }

    private void startAmenitiesActivity() {
        Intent intent = new Intent(this, AmenitiesActivity.class);
        List<Amenity> amenities = entity.amenities;
        if (amenities == null) {
            amenities = new ArrayList<Amenity>();
        }
        intent.putParcelableArrayListExtra("amenities", new ArrayList<>(amenities));
        startActivity(intent);
    }

    private void startTopDealActivity() {
        if (entity.hotel == null || entity.hotel.hotelBookings == null || entity.hotel.hotelBookings.size() <= 0) {
            String phoneNumber = "tel://".concat(entity.phone);
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(phoneNumber)));
        } else {
            Intent intent = new Intent(this, HotelBookingActivity.class);
            List<HotelBooking> hotelBookings = entity.hotel.hotelBookings;
            if (hotelBookings == null) {
                hotelBookings = new ArrayList<HotelBooking>();
            }
            intent.putParcelableArrayListExtra("hotelBookings", new ArrayList<>(hotelBookings));
            startActivity(intent);
        }
    }

    private void setMap() {
        if (this.mapFragment != null) {
            this.mapFragment.getMapAsync(this);
        }
    }

    private void expandImageAndFinish() {
        // if we drag dismiss downward then the default reversal of the enter
        // transition would slide content upward which looks weird. So reverse it.
        if (draggableFrame.getTranslationY() > 0) {
            getWindow().setReturnTransition(
                    TransitionInflater.from(HotelDetailsActivity.this)
                            .inflateTransition(R.transition.about_return_downward));
        }
        finishAfterTransition();

       /* if (imageView.getOffset() != 0f) {
            Animator expandImage = ObjectAnimator.ofFloat(imageView, ParallaxScrimageView.OFFSET,
                    0f);
            expandImage.setDuration(80);
            expandImage.setInterpolator(getFastOutSlowInInterpolator(this));
            expandImage.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    finishAfterTransition();
                }
            });
            expandImage.start();
        } else {
            finishAfterTransition();
        }*/
    }

    private Transition.TransitionListener shotReturnHomeListener =
            new AnimUtils.TransitionListenerAdapter() {
                @Override
                public void onTransitionStart(Transition transition) {
                    super.onTransitionStart(transition);
                    // fade out the "toolbar" & list as we don't want them to be visible during return
                    // animation
                    /*back.animate()
                            .alpha(0f)
                            .setDuration(100)
                            .setInterpolator(getLinearOutSlowInInterpolator(HotelDetailsActivity.this));*/
                    /*imageView.setElevation(1f);*/
                    /*back.setElevation(0f);*/
                    commentsList.animate()
                            .alpha(0f)
                            .setDuration(50)
                            .setInterpolator(getLinearOutSlowInInterpolator(HotelDetailsActivity.this));
                }
            };


    private void setSummaryHistograms() {
        if (entity.aggregateRating == null) {
            this.reviewSummary.setVisibility(View.GONE);
        } else {
            this.reviewSummary.setVisibility(View.VISIBLE);
        }

        int ratingBar[] = new int[5];
        ratingBar[0] = R.id.ratingBar1;
        ratingBar[1] = R.id.ratingBar2;
        ratingBar[2] = R.id.ratingBar3;
        ratingBar[3] = R.id.ratingBar4;
        ratingBar[4] = R.id.ratingBar5;
        this.chartBars = new ProgressBar[ratingBar.length];
        for (int i = 0; i < ratingBar.length; i++) {
            this.chartBars[i] = ((ProgressBar) this.headerView.findViewById(ratingBar[i]));
        }

        if (entity.aggregateRating != null) {
            AggregateRating aggregateRating = entity.aggregateRating;
            averageReviewRating.setText(String.format("%.1f", new Object[]{Double.valueOf(aggregateRating.starRating)}));
            summaryReviewRating.setRating(aggregateRating.starRating);
            evalationReviewRating.setText(String.format("%d %s", aggregateRating.reviewsCount, getResources().getString(R.string.reviews_label)));

            //Histogram
            long ratingsCount = aggregateRating.ratingsCount > 0 ? aggregateRating.ratingsCount : 1;
            for (int i = 0; i < this.chartBars.length; i++) {
                long rate = 0;
                switch (i) {
                    case 0:
                        rate = aggregateRating.oneStarRatings;
                        break;
                    case 1:
                        rate = aggregateRating.twoStarRatings;
                        break;
                    case 2:
                        rate = aggregateRating.threeStarRatings;
                        break;
                    case 3:
                        rate = aggregateRating.fourStarRatings;
                        break;
                    case 4:
                        rate = aggregateRating.fiveStarRatings;
                        break;
                }
                this.chartBars[i].setProgress(Math.max(1, (int) ((double) rate / Double.valueOf(ratingsCount) * 100.0F)));
            }
        }

        if (winterPrefs.getUser() != null) {
            userNameReviewRating.setText(winterPrefs.getUser().username);
            userReviewRatingLayout.setVisibility(View.VISIBLE);
        } else {
            userReviewRatingLayout.setVisibility(View.GONE);
        }

        this.reviewRatingView.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                final Intent intent = new Intent(HotelDetailsActivity.this, SubmitReviewActivity.class);
                intent.putExtra("id", entity.id);
                intent.putExtra("name", entity.name);
                intent.putExtra("rating", rating);
                if (winterPrefs.getUser() != null) {
                    intent.putExtra("userName", winterPrefs.getUser().username);
                }
                startActivityForResult(intent, SUBMIT_REVIEW_REQUEST);
            }
        });
    }

    private void loadReviews() {
        final Resources res = getResources();
        NumberFormat nf = NumberFormat.getInstance();
        commentsList.setAdapter(getLoadingReviewsAdapter());

        // then load review
        final Call<List<Review>> commentsCall = winterPrefs.getApi().getReviews(entity.id, 0, WinterService.PER_PAGE_MAX);
        commentsCall.enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                final List<Review> reviews = response.body();
                if (reviews != null && !reviews.isEmpty()) {
                    commentsAdapter = new ReviewsAdapter(HotelDetailsActivity.this, R.layout.landmark_details_review_item, reviews);
                    commentsList.setAdapter(commentsAdapter);
                    commentsList.setDivider(HotelDetailsActivity.this.getDrawable(R.drawable.list_divider));
                    commentsList.setDividerHeight(getResources().getDimensionPixelSize(R.dimen
                            .divider_height));
                } else {
                    commentsList.setAdapter(getNoCommentsAdapter());
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
            }
        });
    }

    private void watchEntity() {
        if (winterPrefs.isLoggedIn() && winterPrefs.getUser() != null) {
            VisitedPlacesRequest request = new VisitedPlacesRequest(entity.trip_id, winterPrefs.getUser().id, entity.id, "hotels", null);
            final Call<VisitedPlacesResponse> postWatchCall = winterPrefs.getApi().postWatch(request);
            postWatchCall.enqueue(new Callback<VisitedPlacesResponse>() {
                @Override
                public void onResponse(Call<VisitedPlacesResponse> call, Response<VisitedPlacesResponse> response) {
                    VisitedPlacesResponse visitedPlace = response.body();
                    if (visitedPlace != null) {
                        entity.watch = 1;
                        setVisitedPlaces();
                        mViewModel.onBookmarkClicked(entity, true);
                    }
                }

                @Override
                public void onFailure(Call<VisitedPlacesResponse> call, Throwable t) {

                }
            });

        } else {
            needsLogin(fullStarView, RC_LOGIN_WATCH);
        }
    }

    private void unWatchEntity() {
        if (winterPrefs.isLoggedIn() && winterPrefs.getUser() != null) {
            final Call<Void> postWatchCall = winterPrefs.getApi().postUnWatch(entity.visitedPlace != null ? entity.visitedPlace.id : 0);
            postWatchCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 200) {
                        entity.watch = 0;
                        setVisitedPlaces();
                        mViewModel.onBookmarkClicked(entity, false);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });

        } else {
            needsLogin(emptyStarView, RC_LOGIN_WATCH);
        }
    }

    private void needsLogin(View triggeringView, int requestCode) {
        Intent login = new Intent(this, WinterLogin.class);
        MorphTransform.addExtras(login,
                ContextCompat.getColor(this, R.color.background_light),
                getResources().getDimensionPixelSize(R.dimen.dialog_corners));
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation
                (this, triggeringView, getString(R.string.transition_dribbble_login));
        startActivityForResult(login, requestCode, options.toBundle());
    }

    private ListAdapter getNoCommentsAdapter() {
        String[] noComments = {getString(R.string.no_comments)};
        return new ArrayAdapter<String>(this, R.layout.no_comments, noComments);
    }

    private ListAdapter getLoadingReviewsAdapter() {
        return new BaseAdapter() {
            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return getLayoutInflater().inflate(R.layout.loading, parent,
                        false);
            }
        };
    }

    private AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScroll(AbsListView view, int firstVisibleItemPosition, int
                visibleItemCount, int totalItemCount) {
            if (commentsList.getMaxScrollAmount() > 0
                    && firstVisibleItemPosition == 0
                    && commentsList.getChildAt(0) != null) {
                final int listScroll = -commentsList.getChildAt(0).getTop();
                if (listScroll >= photosHolder.getHeight() - toolbarHolder.getHeight() + underscoreTabHeight) {
                    setSolidBackOverflowStatusbar();
                } else {
                    setTransparentBackOverflowStatusbar();
                }
            }
        }

        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // as we animate the main image's elevation change when it 'pins' at it's min height
            // a fling can cause the title to go over the image before the animation has a chance to
            // run. In this case we short circuit the animation and just jump to state.
        }
    };

    private final void setSolidBackOverflowStatusbar() {
        this.mActionBarToolbar.setNavigationIcon(R.drawable.quantum_ic_arrow_back_grey600_24);
        this.mActionBarToolbar.setOverflowIcon(this.getResources().getDrawable(R.drawable.quantum_ic_drawer_grey600_24));
        this.mActionBarToolbar.setBackgroundColor(this.getResources().getColor(android.R.color.white));
        this.setStatusBarColorIfNeeded(R.color.quantum_grey400);
        this.mActionBarToolbar.setTitle(entity.name);
        setSupportActionBar(mActionBarToolbar);

        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private final void setTransparentBackOverflowStatusbar() {
        this.mActionBarToolbar.setNavigationIcon(R.drawable.quantum_ic_arrow_back_white_24);
        this.mActionBarToolbar.setOverflowIcon(this.getResources().getDrawable(R.drawable.quantum_ic_drawer_white_24));
        this.mActionBarToolbar.setBackgroundColor(this.getResources().getColor(android.R.color.transparent));
        this.setStatusBarColorIfNeeded(R.color.half_transparent_status_bar);
        this.mActionBarToolbar.setTitle("");
        setSupportActionBar(mActionBarToolbar);

        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng sydney = new LatLng(entity.latE7, entity.lngE7);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        googleMap.setMyLocationEnabled(true);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title(entity.title));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17.0F));
    }

    @Override
    public void onBookmarkClicked(Entity entity) {
        Intent intent = new Intent();
        intent.putExtra("id", entity.id);
        intent.putExtra("watch", entity.watch);
        setResult(BOOKMARK_RESULT_OK, intent);
    }

    private HotelDetailsViewModel mViewModel;

    private void subscribeEntityChanges() {
        mViewModel = obtainViewModel(this);

        // The activity observes the navigation events in the ViewModel
        mViewModel.getEntityUpdatedEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void _) {
                HotelDetailsActivity.this.onBookmarkClicked(entity);
            }
        });
    }

    public static HotelDetailsViewModel obtainViewModel(FragmentActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        return ViewModelProviders.of(activity, factory).get(HotelDetailsViewModel.class);
    }

    /*@Override
    public void onPostReviewSuccess(long id, float rating, String reviewText) {
        userReviewRating.setRating(rating);
    }*/


}

