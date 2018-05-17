package com.winter.dreamhub.restaurants;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.winter.dreamhub.R;
import com.winter.dreamhub.activity.BaseActivity;
import com.winter.dreamhub.viewmodels.ViewModelFactory;

/**
 * Created by hoaxoan on 11/20/2016.
 */

public class RestaurantsListActivity extends BaseActivity {

    RestaurantsListFragment restaurantsListFragment;
    private Toolbar mActionBarToolbar;
    private long tripId;
    private long categoryId;
    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);

        this.tripId = getIntent().getLongExtra("trip_id", 0);
        this.categoryId = getIntent().getLongExtra("category_id", 0);
        this.categoryName = getIntent().getStringExtra("category_name");

        mActionBarToolbar = getActionBarToolbar();
        mActionBarToolbar.setNavigationIcon(R.drawable.quantum_ic_arrow_back_grey600_24);
        mActionBarToolbar.setTitleTextColor(getResources().getColor(R.color.quantum_black_secondary_text));
        mActionBarToolbar.setTitle(this.categoryName);
        setSupportActionBar(mActionBarToolbar);

        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        this.restaurantsListFragment = RestaurantsListFragment.newInstance(this.tripId, this.categoryId, this.mActionBarToolbar);
        showFragment(R.id.fragment_container, this.restaurantsListFragment);
    }

    public static RestaurantsViewModel obtainViewModel(FragmentActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        RestaurantsViewModel viewModel =
                ViewModelProviders.of(activity, factory).get(RestaurantsViewModel.class);

        return viewModel;
    }
}

