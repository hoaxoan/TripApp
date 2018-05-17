package com.winter.dreamhub.hotels;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.winter.dreamhub.R;
import com.winter.dreamhub.activity.BaseActivity;
import com.winter.dreamhub.api.service.model.HotelBooking;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoaxoan on 10/14/2017.
 */

public class HotelBookingActivity extends BaseActivity {

    private HotelBookingFragment fragment;
    private Toolbar mActionBarToolbar;
    private List<HotelBooking> hotelBookings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);

        this.hotelBookings = getIntent().getParcelableArrayListExtra("hotelBookings");
        if (this.hotelBookings == null) {
            this.hotelBookings = new ArrayList<HotelBooking>();
        }

        mActionBarToolbar = getActionBarToolbar();
        mActionBarToolbar.setNavigationIcon(R.drawable.quantum_ic_close_grey600_24);
        mActionBarToolbar.setTitleTextColor(getResources().getColor(R.color.quantum_black_secondary_text));
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setTitle(getString(R.string.top_deal_label));

        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        this.fragment = HotelBookingFragment.newInstance(this.hotelBookings);
        showFragment(R.id.fragment_container, this.fragment);
    }
}

