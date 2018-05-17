package com.winter.dreamhub.activity;

import android.os.Bundle;

import com.winter.dreamhub.R;
import com.winter.dreamhub.activity.BaseActivity;

/**
 * Created by hoaxoan on 5/5/2016.
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        overridePendingTransition(0, 0);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_CALENDAR;
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }
}
