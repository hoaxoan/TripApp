package com.winter.dreamhub.restaurants;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.winter.dreamhub.R;
import com.winter.dreamhub.activity.BaseActivity;
import com.winter.dreamhub.restaurants.RestaurantsFragment;

/**
 * Created by hoaxoan on 12/11/2016.
 */

public class RestaurantsActivity extends BaseActivity {

    private RestaurantsFragment fragment;
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
        mActionBarToolbar.setTitle(categoryName);
        setSupportActionBar(mActionBarToolbar);

        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        this.tripId = getIntent().getLongExtra("trip_id", 0);
        this.categoryId = getIntent().getLongExtra("category_id", 0);

        this.fragment = RestaurantsFragment.newInstance(this.tripId, this.categoryId, this.mActionBarToolbar);
        showFragment(R.id.fragment_container, this.fragment);
    }
}
