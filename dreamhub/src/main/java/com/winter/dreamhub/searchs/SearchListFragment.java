package com.winter.dreamhub.searchs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winter.dreamhub.R;
import com.winter.dreamhub.api.prefs.WinterPrefs;
import com.winter.dreamhub.api.service.SearchDataManager;
import com.winter.dreamhub.api.service.WinterService;
import com.winter.dreamhub.api.service.model.Category;
import com.winter.dreamhub.api.service.model.Entity;
import com.winter.dreamhub.widget.recyclerview.InfiniteScrollListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.winter.dreamhub.util.LogUtils.makeLogTag;

/**
 * Created by hoaxoan on 12/18/2016.
 */

public class SearchListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, FilterEventCallbacks {
    public static final String TAG = makeLogTag(SearchListFragment.class);
    private View rootView;

    private ImageView mBackView;
    private ImageView mCloseButton;
    private ImageView mVoiceButton;
    private EditText mSearchView;

    private SearchsAdapter adapter;
    private RecyclerView mRecycleView;
    private LinearLayoutManager layoutManager;
    private SearchDataManager dataManager;
    private SwipeRefreshLayout refreshLayout;

    private int type;
    private FloatingActionButton fabFilter;
    private BottomSheetBehavior sheetBehavior;
    private View filterSheet;

    private WinterPrefs winterPrefs;
    public List<Category> selectedItems = new ArrayList<>();
    private SearchFilterFragment fragment;

    /**
     * Create a new instance of the fragment
     */
    public static SearchListFragment newInstance(int type) {
        SearchListFragment fragment = new SearchListFragment();
        fragment.type = type;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        winterPrefs = WinterPrefs.get(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.mBackView = (ImageView) this.rootView.findViewById(R.id.search_widget_back);
        this.mVoiceButton = (ImageView) this.rootView.findViewById(R.id.search_widget_voice_btn);
        this.mCloseButton = (ImageView) this.rootView.findViewById(R.id.search_widget_close_btn);
        this.mSearchView = (EditText) this.rootView.findViewById(R.id.search_box);

        this.mBackView.setOnClickListener(mOnClickListener);
        this.mCloseButton.setOnClickListener(mOnClickListener);
        this.mVoiceButton.setOnClickListener(mOnClickListener);

        this.mSearchView.setOnClickListener(mOnClickListener);
        this.mSearchView.addTextChangedListener(mTextWatcher);
        this.mSearchView.setOnEditorActionListener(mOnEditorActionListener);
        this.mSearchView.setOnKeyListener(mTextKeyListener);

        this.mRecycleView = ((RecyclerView) this.rootView.findViewById(R.id.recycler_view));
        dataManager = new SearchDataManager(getActivity(), 0, type) {
            @Override
            public void onDataLoaded(List<Entity> data) {
                if (data != null && data.size() > 0) {
                    adapter.addAndResort(data);
                }
                refreshLayout.setRefreshing(false);
            }
        };
        dataManager.searchFor("");

        this.adapter = new SearchsAdapter(getActivity(), dataManager);
        this.layoutManager = new LinearLayoutManager(getActivity());
        this.mRecycleView.setLayoutManager(layoutManager);
        this.mRecycleView.setItemAnimator(new DefaultItemAnimator());
        this.mRecycleView.setAdapter(this.adapter);
        this.mRecycleView.addOnScrollListener(new InfiniteScrollListener(layoutManager, dataManager) {
            @Override
            public void onLoadMore() {
                dataManager.loadMore();
            }
        });
        this.mRecycleView.setHasFixedSize(true);

        // Refresh
        this.refreshLayout = (SwipeRefreshLayout) this.rootView.findViewById(R.id.swipe_container);
        this.refreshLayout.setColorSchemeResources(R.color.sunflower_yellow, R.color.neon_blue,
                R.color.lightish_blue, R.color.aqua_marine);
        this.refreshLayout.setOnRefreshListener(this);

        // Filters
        this.fabFilter = (FloatingActionButton) this.rootView.findViewById(R.id.filter_fab);
        this.fabFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBottomSheet();
            }
        });

        this.filterSheet = (View) this.rootView.findViewById(R.id.filter_sheet);
        this.sheetBehavior = BottomSheetBehavior.from(filterSheet);

        setFragment();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.search_list_fragment, container, false);
        return this.rootView;
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


    private void setFragment() {

        this.fragment = SearchFilterFragment.newInstance(this, this.sheetBehavior);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.filter_sheet, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

        if (selectedItems.size() > 0) {
            this.sheetBehavior.setPeekHeight((int) getResources().getDimension(R.dimen.bottom_sheet_peek_height));
            this.fabFilter.setVisibility(View.GONE);
        } else {
            this.sheetBehavior.setPeekHeight(0);
            this.fabFilter.setVisibility(View.VISIBLE);
        }

        this.sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                selectedItems = fragment.selectedItems;
                fragment.tagsAdapter.addAndResort(selectedItems);
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        if (selectedItems.size() > 0) {
                            fragment.setTagInVisibility(true);
                            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            sheetBehavior.setPeekHeight((int) getResources().getDimension(R.dimen.bottom_sheet_peek_height));
                            fabFilter.setVisibility(View.GONE);
                        } else {
                            sheetBehavior.setPeekHeight(0);
                            fabFilter.setVisibility(View.VISIBLE);
                            fragment.setTagInVisibility(false);
                        }
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        fragment.setTagInVisibility(false);
                        if (selectedItems.size() > 0) {
                            sheetBehavior.setPeekHeight((int) getResources().getDimension(R.dimen.bottom_sheet_peek_height));
                            fabFilter.setVisibility(View.GONE);
                        } else {
                            sheetBehavior.setPeekHeight(0);
                            fabFilter.setVisibility(View.VISIBLE);
                        }
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        fragment.setTagInVisibility(true);
                        if (selectedItems.size() > 0) {
                            sheetBehavior.setPeekHeight((int) getResources().getDimension(R.dimen.bottom_sheet_peek_height));
                            fabFilter.setVisibility(View.GONE);
                        } else {
                            sheetBehavior.setPeekHeight(0);
                            fabFilter.setVisibility(View.VISIBLE);
                        }
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    public void toggleBottomSheet() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    private void searchFor(String query) {
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
            clearResults();
            mSearchView.clearFocus();
            dataManager.searchFor(query);
        }
    }

    private void clearResults() {
        adapter.clear();
        dataManager.clear();
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
        getActivity().onBackPressed();
    }

    private void updateCloseButton() {
        final boolean hasText = !TextUtils.isEmpty(mSearchView.getText());
        mCloseButton.setVisibility(hasText ? View.VISIBLE : View.GONE);
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

                        searchFor(mSearchView.getText().toString());
                        return true;
                    }
                }
            }
            return false;
        }
    };

    @Override
    public void onFilterClicked(List<Category> categories) {
        this.selectedItems = categories;
    }

    @Override
    public void onRefresh() {
        adapter.clear();
        dataManager.searchFor(mSearchView.getText().toString());
        refreshLayout.setRefreshing(true);
    }
}

