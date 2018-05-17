package com.winter.dreamhub.searchs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.winter.dreamhub.R;
import com.winter.dreamhub.activity.BaseActivity;
import com.winter.dreamhub.api.prefs.WinterPrefs;
import com.winter.dreamhub.api.service.SearchDataManager;
import com.winter.dreamhub.api.service.model.Entity;
import com.winter.dreamhub.api.service.model.EntitySnippet;
import com.winter.dreamhub.api.service.model.EntitySnippets;
import com.winter.dreamhub.widget.recyclerview.InfiniteScrollListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hoaxoan on 12/17/2016.
 */

public class SearchActivity extends BaseActivity {

    private static final String ARG_QUERY = "query";

    private long tripId;
    private long categoryId;

    private ImageView mBackView;
    private ImageView mCloseButton;
    private ImageView mVoiceButton;

    private EditText mSearchView;
    private String mQuery = "";
    private QuickSearchsAdapter searchsAdapter;
    private RecyclerView mSearchResults;
    private LinearLayoutManager layoutManager;


    private SuggestsAdapter suggestsAdapter;
    private RecyclerView mSuggestResults;

    private SearchDataManager dataManager;
    private WinterPrefs winterPrefs;

    private List<EntitySnippet> suggests = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        this.winterPrefs = WinterPrefs.get(this);

        this.mBackView = (ImageView) findViewById(R.id.search_widget_back);
        this.mVoiceButton = (ImageView) findViewById(R.id.search_widget_voice_btn);
        this.mCloseButton = (ImageView) findViewById(R.id.search_widget_close_btn);
        this.mSearchView = (EditText) findViewById(R.id.search_box);
        this.mSearchResults = ((RecyclerView) findViewById(R.id.search_results));
        this.mSuggestResults = ((RecyclerView) findViewById(R.id.suggest_results));

        mBackView.setOnClickListener(mOnClickListener);
        mCloseButton.setOnClickListener(mOnClickListener);
        mVoiceButton.setOnClickListener(mOnClickListener);

        mSearchView.setOnClickListener(mOnClickListener);
        mSearchView.addTextChangedListener(mTextWatcher);
        mSearchView.setOnEditorActionListener(mOnEditorActionListener);
        mSearchView.setOnKeyListener(mTextKeyListener);

        dataManager = new SearchDataManager(this) {
            @Override
            public void onDataLoaded(List<Entity> data) {
                if (data != null && data.size() > 0) {
                    searchsAdapter.addAndResort(data);
                    findViewById(R.id.search_panel).setVisibility(searchsAdapter.getItemCount() > 0 ? View.VISIBLE : View.GONE);
                    findViewById(R.id.suggest_panel).setVisibility(searchsAdapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);
                }
            }
        };
        //dataManager.searchFor("");

        this.searchsAdapter = new QuickSearchsAdapter(this, dataManager);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        this.mSearchResults.setLayoutManager(layoutManager);
        this.mSearchResults.setItemAnimator(new DefaultItemAnimator());
        this.mSearchResults.setAdapter(this.searchsAdapter);
        this.mSearchResults.addOnScrollListener(new InfiniteScrollListener(layoutManager, dataManager) {
            @Override
            public void onLoadMore() {
                dataManager.loadMore();
            }
        });
        this.mSearchResults.setHasFixedSize(true);
        findViewById(R.id.search_panel).setVisibility(searchsAdapter.getItemCount() > 0 ? View.VISIBLE : View.GONE);

        // load Suggests
        //loadSuggests();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            doEnterAnim();
        }

        overridePendingTransition(0, 0);
    }

    private void searchFor(String query) {
        boolean hasText = !TextUtils.isEmpty(query);
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
            clearResults();
            mSearchView.clearFocus();
            dataManager.searchFor(query);
        }
        mQuery = query;
    }

    private void clearResults() {
        searchsAdapter.clear();
        dataManager.clear();
    }

    private void loadSuggests() {
        final Call loadCall = winterPrefs.getApi().suggest();
        loadCall.enqueue(new Callback<EntitySnippets>() {
            @Override
            public void onResponse(Call<EntitySnippets> call, Response<EntitySnippets> response) {
                if (response.isSuccessful()) {
                    suggests = response.body().getEntitySnippets();
                    bindSuggests();
                }
            }

            @Override
            public void onFailure(Call<EntitySnippets> call, Throwable t) {
            }
        });
    }

    private void bindSuggests() {
        this.suggestsAdapter = new SuggestsAdapter(this, 0, this.suggests);
        LinearLayoutManager suggestLayoutManager = new LinearLayoutManager(this);
        this.mSuggestResults.setLayoutManager(suggestLayoutManager);
        this.mSuggestResults.setItemAnimator(new DefaultItemAnimator());
        this.mSuggestResults.setAdapter(this.suggestsAdapter);
        this.mSuggestResults.setHasFixedSize(true);
        findViewById(R.id.suggest_panel).setVisibility(this.suggestsAdapter.getItemCount() > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onBackPressed() {
        dismiss(null);
    }

    public void dismiss(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            doExitAnim();
        } else {
            ActivityCompat.finishAfterTransition(this);
        }
    }

    /**
     * On Lollipop+ perform a circular reveal animation (an expanding circular mask) when showing
     * the search panel.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void doEnterAnim() {
        // Fade in a background scrim as this is a floating window. We could have used a
        // translucent window background but this approach allows us to turn off window animation &
        // overlap the fade with the reveal animation – making it feel snappier.
        View scrim = findViewById(R.id.scrim);
        scrim.animate()
                .alpha(1f)
                .setDuration(500L)
                .setInterpolator(
                        AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in))
                .start();

        // Next perform the circular reveal on the search panel
        final View searchPanel = findViewById(R.id.search_panel);
        if (searchPanel != null) {
            // We use a view tree observer to set this up once the view is measured & laid out
            searchPanel.getViewTreeObserver().addOnPreDrawListener(
                    new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            searchPanel.getViewTreeObserver().removeOnPreDrawListener(this);
                            // As the height will change once the initial suggestions are delivered by the
                            // loader, we can't use the search panels height to calculate the final radius
                            // so we fall back to it's parent to be safe
                            final ViewGroup searchPanelParent = (ViewGroup) searchPanel.getParent();
                            final int revealRadius = (int) Math.hypot(
                                    searchPanelParent.getWidth(), searchPanelParent.getHeight());
                            // Center the animation on the top right of the panel i.e. near to the
                            // search button which launched this screen.
                            Animator show = ViewAnimationUtils.createCircularReveal(searchPanel,
                                    searchPanel.getRight(), searchPanel.getTop(), 0f, revealRadius);
                            show.setDuration(250L);
                            show.setInterpolator(AnimationUtils.loadInterpolator(SearchActivity.this,
                                    android.R.interpolator.fast_out_slow_in));
                            show.start();
                            return false;
                        }
                    });
        }
    }

    /**
     * On Lollipop+ perform a circular animation (a contracting circular mask) when hiding the
     * search panel.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void doExitAnim() {
        final View searchPanel = findViewById(R.id.search_panel);
        // Center the animation on the top right of the panel i.e. near to the search button which
        // launched this screen. The starting radius therefore is the diagonal distance from the top
        // right to the bottom left
        final int revealRadius = (int) Math.hypot(searchPanel.getWidth(), searchPanel.getHeight());
        // Animating the radius to 0 produces the contracting effect
        Animator shrink = ViewAnimationUtils.createCircularReveal(searchPanel,
                searchPanel.getRight(), searchPanel.getTop(), revealRadius, 0f);
        shrink.setDuration(200L);
        shrink.setInterpolator(AnimationUtils.loadInterpolator(SearchActivity.this,
                android.R.interpolator.fast_out_slow_in));
        shrink.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                searchPanel.setVisibility(View.INVISIBLE);
                ActivityCompat.finishAfterTransition(SearchActivity.this);
            }
        });
        shrink.start();

        // We also animate out the translucent background at the same time.
        findViewById(R.id.scrim).animate()
                .alpha(0f)
                .setDuration(200L)
                .setInterpolator(
                        AnimationUtils.loadInterpolator(SearchActivity.this,
                                android.R.interpolator.fast_out_slow_in))
                .start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(0, 0);
        }
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == mCloseButton) {
                onCloseClicked();
            } else if (v == mBackView) {
                onBackClicked();
            } else if (v == mVoiceButton) {
                //onVoiceClicked();
            } else if (v == mSearchView) {
                searchFor(mSearchView.getText().toString());
            }
        }
    };

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
            searchFor(s.toString());
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
            searchFor(v.getText().toString());
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

                        // Launch as a regular search.
                        searchFor(mSearchView.getText().toString());
                        return true;
                    }
                }
            }
            return false;
        }
    };

    void onCloseClicked() {
        CharSequence text = mSearchView.getText();
        if (TextUtils.isEmpty(text)) {
            updateCloseButton();
        } else {
            mSearchView.setText("");
            mSearchView.requestFocus();
        }
    }

    void onBackClicked() {
        navigateUpOrBack(SearchActivity.this, null);
    }

    private void updateCloseButton() {
        final boolean hasText = !TextUtils.isEmpty(mSearchView.getText());
        mCloseButton.setVisibility(hasText ? View.VISIBLE : View.GONE);
    }
}
