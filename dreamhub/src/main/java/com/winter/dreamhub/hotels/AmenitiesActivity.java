package com.winter.dreamhub.hotels;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.winter.dreamhub.R;
import com.winter.dreamhub.activity.BaseActivity;
import com.winter.dreamhub.api.service.model.Amenity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoaxoan on 10/14/2017.
 */

public class AmenitiesActivity extends BaseActivity {

    private AmenitiesFragment fragment;
    private Toolbar mActionBarToolbar;
    private List<Amenity> amenities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);

        this.amenities = getIntent().getParcelableArrayListExtra("amenities");
        if(this.amenities == null){
            this.amenities = new ArrayList<Amenity>();
        }

        mActionBarToolbar = getActionBarToolbar();
        mActionBarToolbar.setNavigationIcon(R.drawable.quantum_ic_close_grey600_24);
        mActionBarToolbar.setTitleTextColor(getResources().getColor(R.color.quantum_black_secondary_text));
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setTitle(getString(R.string.amenities));

        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        this.fragment = AmenitiesFragment.newInstance(this.amenities);
        showFragment(R.id.fragment_container, this.fragment);
    }

    public static final int[] AMENITY_ICON_RES_ID = new int[]{
            R.drawable.n2_ic_am_internet,
            R.drawable.n2_ic_am_familyfriendly,
            R.drawable.n2_ic_am_kitchen,
            R.drawable.n2_ic_am_wifi,
            R.drawable.n2_ic_am_tv,
            R.drawable.n2_ic_am_ac,
            R.drawable.n2_ic_am_heating,
    };
}

