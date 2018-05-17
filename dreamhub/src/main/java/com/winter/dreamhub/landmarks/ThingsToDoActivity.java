package com.winter.dreamhub.landmarks;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.winter.dreamhub.R;
import com.winter.dreamhub.activity.BaseActivity;
import com.winter.dreamhub.api.service.model.Mood;

import java.util.List;

/**
 * Created by hoaxoan on 10/27/2016.
 */

public class ThingsToDoActivity extends BaseActivity {

    ThingsToDoFragment thingsToDoFragment;
    private Toolbar mActionBarToolbar;
    private long tripId;
    private long categoryId;
    private String categoryName;
    private List<Mood> moods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.things_to_do_activity);

        this.tripId = getIntent().getLongExtra("trip_id", 0);
        this.categoryId = getIntent().getLongExtra("category_id", 0);
        this.categoryName = getIntent().getStringExtra("category_name");
        this.moods = getIntent().getParcelableArrayListExtra("moods");

        mActionBarToolbar = getActionBarToolbar();
        mActionBarToolbar.setNavigationIcon(R.drawable.quantum_ic_arrow_back_grey600_24);
        mActionBarToolbar.setTitleTextColor(getResources().getColor(R.color.quantum_black_secondary_text));
        mActionBarToolbar.setBackgroundColor(getResources().getColor(R.color.white_text));
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setTitle(categoryName);

        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        this.thingsToDoFragment = ThingsToDoFragment.newInstance(this.tripId, this.categoryId, this.moods, this.mActionBarToolbar);
        showFragment(R.id.fragment_container, this.thingsToDoFragment);
    }
}
