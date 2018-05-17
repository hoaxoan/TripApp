package com.winter.dreamhub.hotels;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.winter.dreamhub.R;
import com.winter.dreamhub.activity.BaseActivity;
import com.winter.dreamhub.savedplaces.SavePlacesViewModel;
import com.winter.dreamhub.viewmodels.ViewModelFactory;

/**
 * Created by hoaxoan on 10/14/2017.
 */

public class HotelsActivity extends BaseActivity {

    private HotelsFragment fragment;
    private Toolbar mActionBarToolbar;
    private long tripId;
    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);

        this.tripId = getIntent().getLongExtra("trip_id", 0);
        this.categoryName = getIntent().getStringExtra("category_name");

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

        this.fragment = HotelsFragment.newInstance(this.tripId, this.mActionBarToolbar);
        showFragment(R.id.fragment_container, this.fragment);
    }

    public static HotelsViewModel obtainViewModel(FragmentActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        HotelsViewModel viewModel =
                ViewModelProviders.of(activity, factory).get(HotelsViewModel.class);

        return viewModel;
    }
}

