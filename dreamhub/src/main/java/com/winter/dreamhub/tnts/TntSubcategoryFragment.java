package com.winter.dreamhub.tnts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winter.dreamhub.R;
import com.winter.dreamhub.api.prefs.WinterPrefs;
import com.winter.dreamhub.api.service.model.Guideline;
import com.winter.dreamhub.api.service.model.Guidelines;
import com.winter.dreamhub.util.DisplayUtils;
import com.winter.dreamhub.util.ImageUtils;
import com.winter.dreamhub.widget.ObservableScrollView;
import com.winter.dreamhub.widget.TntAccordion;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hoaxoan on 11/6/2016.
 */

public class TntSubcategoryFragment extends Fragment
        implements ObservableScrollView.OnScrollChangeListener, TntAccordion.OnExpandListener {
    ImageUtils imageUtils;

    ObservableScrollView scrollView;
    LinearLayout itemsContainer;
    View loadingLayout;
    View rootView;

    private long parentId;
    private long tripId;
    private WinterPrefs winterPrefs;

    public static TntSubcategoryFragment newInstance(long tripId, long parentId) {
        Bundle bundle = new Bundle();
        TntSubcategoryFragment fragment = new TntSubcategoryFragment();
        fragment.tripId = tripId;
        fragment.parentId = parentId;
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        winterPrefs = WinterPrefs.get(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.tnt_subcategory_fragment_layout, container, false);
        this.imageUtils = new ImageUtils(getActivity(), new DisplayUtils(getActivity().getWindowManager()));

        this.scrollView = ((ObservableScrollView) this.rootView.findViewById(R.id.scrollable_container));
        this.scrollView.setOnScrollListener(this);
        this.itemsContainer = ((LinearLayout) this.rootView.findViewById(R.id.items_container));
        this.loadingLayout = this.rootView.findViewById(R.id.loading_layout);
        //this.scrollView.setPadding(0, this.imageUtils.getHeaderHeight(), 0, 0);
        this.scrollView.setVisibility(View.VISIBLE);

        // Load SubCategories
        loadSubCategories();

        return this.rootView;
    }

    private void loadSubCategories() {
        final Call<Guidelines> subCategoriesCall = winterPrefs.getApi().getSubGuideline(this.tripId, this.parentId);
        subCategoriesCall.enqueue(new Callback<Guidelines>() {
            @Override
            public void onResponse(Call<Guidelines> call, Response<Guidelines> response) {
                Guidelines guidelines = response.body();
                if (guidelines != null) {
                    setupSubCategoryDescription(guidelines);

                    setupAccordion(guidelines);
                }
            }

            @Override
            public void onFailure(Call<Guidelines> call, Throwable t) {
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    final View getSpacer() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.tnt_accordion_spacer, this.itemsContainer, false);
    }

    final View getSubCategoryDescription() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.tnt_subcategory_description_layout, this.itemsContainer, false);
    }

    public void setupSubCategoryDescription(Guidelines guidelines) {
        View subCategoryDescription = getSubCategoryDescription();
        TextView description = (TextView) subCategoryDescription.findViewById(R.id.tnt_subcategory_description);
        description.setText(guidelines.getDescription());
        this.itemsContainer.addView(subCategoryDescription);
        this.itemsContainer.addView(getSpacer());
    }

    public void setupAccordion(Guidelines guidelines) {
        List<Guideline> subCategories = guidelines.getGuidelines();
        for (int i = 0; i < subCategories.size(); i++) {
            Guideline guideline = subCategories.get(i);
            TntAccordion tntAccordion = new TntAccordion(getActivity());
            ((TextView) tntAccordion.firstView.findViewById(R.id.title)).setText(guideline.name);
            ((TextView) tntAccordion.firstView.findViewById(R.id.sub_title)).setVisibility(View.GONE);
            ((TextView) tntAccordion.firstView.findViewById(R.id.content)).setVisibility(View.GONE);
            tntAccordion.firstView.setPadding(getResources().getDimensionPixelSize(R.dimen.tnt_item_padding_16), getResources().getDimensionPixelSize(R.dimen.tnt_item_padding_16), getResources().getDimensionPixelSize(R.dimen.tnt_item_padding_16), getResources().getDimensionPixelSize(R.dimen.tnt_item_padding_16));

            ((TextView) tntAccordion.secondView.findViewById(R.id.title)).setText(guideline.name);
            ((TextView) tntAccordion.secondView.findViewById(R.id.sub_title)).setVisibility(View.GONE);
            ((TextView) tntAccordion.secondView.findViewById(R.id.content)).setText(guideline.description);

            this.itemsContainer.addView(tntAccordion);
            this.itemsContainer.addView(getSpacer());
        }
    }


    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {

    }

    @Override
    public void onExpand(TntAccordion tntAccordion) {

    }
}
