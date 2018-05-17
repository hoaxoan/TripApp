package com.winter.dreamhub.searchs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.winter.dreamhub.R;
import com.winter.dreamhub.api.prefs.WinterPrefs;
import com.winter.dreamhub.api.service.WinterService;
import com.winter.dreamhub.api.service.model.Category;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.winter.dreamhub.util.LogUtils.makeLogTag;

public class SearchFilterFragment extends Fragment implements FilterEventCallbacks {

    public static final String TAG = makeLogTag(SearchFilterFragment.class);
    private View rootView;

    private TextView txtTitle;
    private ImageButton btnClearFilters;
    private Button btnReset;
    private ImageButton btnCollapse;
    private View viewExpand;

    private RecyclerView tagsRecyclerView;
    public FilterTagsAdapter tagsAdapter;

    private RecyclerView filtersRecyclerView;
    private FilterAdapter filtersAdapter;

    private BottomSheetBehavior sheetBehavior;
    private List<Category> categories = new ArrayList<>();
    public List<Category> selectedItems = new ArrayList<>();
    private WinterPrefs winterPrefs;

    private FilterEventCallbacks mCallbacks;

    /**
     * Create a new instance of the fragment
     */
    public static SearchFilterFragment newInstance(FilterEventCallbacks callbacks, BottomSheetBehavior sheetBehavior) {
        SearchFilterFragment fragment = new SearchFilterFragment();
        fragment.mCallbacks = callbacks;
        fragment.sheetBehavior = sheetBehavior;
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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.search_filter_fragment, container, false);

        // RecyclerView
        this.tagsRecyclerView = (RecyclerView) this.rootView.findViewById(R.id.filter_description_tags);
        this.tagsAdapter = new FilterTagsAdapter(getActivity(), new ArrayList<>());
        this.tagsRecyclerView.setAdapter(this.tagsAdapter);

        this.filtersRecyclerView = (RecyclerView) this.rootView.findViewById(R.id.recyclerview);
        this.filtersAdapter = new FilterAdapter(getActivity(), this, categories);
        this.filtersRecyclerView.setAdapter(this.filtersAdapter);

        this.txtTitle = (TextView) this.rootView.findViewById(R.id.title);
        this.btnClearFilters = (ImageButton) this.rootView.findViewById(R.id.clear_filters_shortcut);
        this.btnCollapse = (ImageButton) this.rootView.findViewById(R.id.collapse_arrow);
        this.btnReset = (Button) this.rootView.findViewById(R.id.reset);
        this.viewExpand = (View) this.rootView.findViewById(R.id.expand);

        loadCategories();

        if (selectedItems.size() > 0) {
            tagsRecyclerView.setVisibility(View.VISIBLE);
            txtTitle.setVisibility(View.GONE);
            btnCollapse.setVisibility(View.GONE);
            btnClearFilters.setVisibility(View.VISIBLE);
            btnReset.setVisibility(View.GONE);
        } else {
            txtTitle.setText(getResources().getString(R.string.filters));
            tagsRecyclerView.setVisibility(View.GONE);
            txtTitle.setVisibility(View.VISIBLE);
            btnCollapse.setVisibility(View.VISIBLE);
            btnClearFilters.setVisibility(View.GONE);
            btnReset.setVisibility(View.GONE);
        }

        this.tagsRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBottomSheet();
            }
        });

        this.btnCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagsAdapter.addAndResort(selectedItems);
                toggleBottomSheet();

                if (selectedItems.size() > 0) {
                    tagsRecyclerView.setVisibility(View.VISIBLE);
                    txtTitle.setVisibility(View.GONE);
                    btnCollapse.setVisibility(View.GONE);
                    btnClearFilters.setVisibility(View.VISIBLE);
                    btnReset.setVisibility(View.GONE);
                } else {
                    txtTitle.setText(getResources().getString(R.string.filters));
                    tagsRecyclerView.setVisibility(View.GONE);
                    txtTitle.setVisibility(View.VISIBLE);
                    btnCollapse.setVisibility(View.VISIBLE);
                    btnClearFilters.setVisibility(View.GONE);
                    btnReset.setVisibility(View.GONE);
                }

                if (mCallbacks == null) {
                    return;
                }
                mCallbacks.onFilterClicked(selectedItems);
            }
        });

        this.btnClearFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedItems.clear();
                tagsAdapter.clear();
                filtersAdapter = new FilterAdapter(getActivity(), SearchFilterFragment.this, categories);
                filtersRecyclerView.setAdapter(filtersAdapter);
                toggleBottomSheet();
            }
        });

        this.viewExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleBottomSheet();
            }
        });

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


    private void loadCategories() {
        Call loadCall = winterPrefs.getApi().getCategoryByTypes(WinterService.TYPE_RESTAURANT);
        loadCall.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    final List<Category> categories = response.body();
                    bindCategories(categories);
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });
    }


    private void bindCategories(List<Category> categories) {
        this.categories = categories;
        this.filtersAdapter.addAndResort(categories);
    }

    public void toggleBottomSheet() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            setTagInVisibility(false);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            setTagInVisibility(true);
        }
    }

    public void setTagInVisibility(boolean isVisibility) {
        if (isVisibility) {
            tagsRecyclerView.setVisibility(View.VISIBLE);
            txtTitle.setVisibility(View.GONE);
            btnCollapse.setVisibility(View.GONE);
            btnClearFilters.setVisibility(View.VISIBLE);
            btnReset.setVisibility(View.GONE);
        } else {
            txtTitle.setText(getResources().getString(R.string.filters));
            tagsRecyclerView.setVisibility(View.GONE);
            txtTitle.setVisibility(View.VISIBLE);
            btnCollapse.setVisibility(View.VISIBLE);
            btnClearFilters.setVisibility(View.GONE);
            btnReset.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFilterClicked(List<Category> categories) {
        this.selectedItems = categories;
    }
}
