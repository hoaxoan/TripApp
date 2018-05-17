package com.winter.dreamhub.itineraries;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.winter.dreamhub.R;
import com.winter.dreamhub.activity.BaseActivity;

/**
 * Created by hoaxoan on 11/7/2016.
 */

public class ItinerariesActivity extends BaseActivity {

    private Toolbar mActionBarToolbar;
    private long tripId;
    private long categoryId;
    private String categoryName;
    
    private RecyclerView recyclerView;
    private ItinerariesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itineraries_activity);

        this.tripId = getIntent().getLongExtra("trip_id", 0);
        this.categoryId = getIntent().getLongExtra("category_id", 0);
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

        int toolbarHeight = getResources().getDimensionPixelOffset(R.dimen.toolbar_height);
        recyclerView = (RecyclerView) this.findViewById(R.id.itineraries_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setPadding(recyclerView.getPaddingLeft(), recyclerView.getPaddingTop() + toolbarHeight, recyclerView.getPaddingRight(), recyclerView.getPaddingBottom());

        mAdapter = new ItinerariesAdapter(this);
        recyclerView.setAdapter(mAdapter);

    }
}
