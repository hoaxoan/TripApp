package com.winter.dreamhub.savedplaces;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.winter.dreamhub.R;
import com.winter.dreamhub.activity.BaseActivity;
import com.winter.dreamhub.viewmodels.ViewModelFactory;

/**
 * Created by hoaxoan on 8/9/2017.
 */

public class SavedPlacesActivity extends BaseActivity {

    private SavedPlacesFragment fragment;
    private Toolbar mActionBarToolbar;

    private long tripId;
    private String categoryName;
    private String relatedType;

    private SavePlacesViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_places_activity);

        this.tripId = getIntent().getLongExtra("trip_id", 0);
        this.categoryName = getIntent().getStringExtra("category_name");
        this.relatedType = getIntent().getStringExtra("related_type");

        mActionBarToolbar = getActionBarToolbar();
        mActionBarToolbar.setNavigationIcon(R.drawable.quantum_ic_arrow_back_grey600_24);
        mActionBarToolbar.setTitleTextColor(getResources().getColor(R.color.quantum_black_secondary_text));
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setTitle(categoryName);

        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        this.fragment = SavedPlacesFragment.newInstance(this.tripId, this.relatedType);
        showFragment(R.id.fragment_container, this.fragment);

        mViewModel = obtainViewModel(this);
    }

    public static SavePlacesViewModel obtainViewModel(FragmentActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        SavePlacesViewModel viewModel =
                ViewModelProviders.of(activity, factory).get(SavePlacesViewModel.class);

        return viewModel;
    }
}
