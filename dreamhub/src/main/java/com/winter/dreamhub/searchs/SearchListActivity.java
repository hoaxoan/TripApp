package com.winter.dreamhub.searchs;

import android.os.Bundle;

import com.winter.dreamhub.R;
import com.winter.dreamhub.activity.BaseActivity;
import com.winter.dreamhub.searchs.SearchListFragment;


/**
 * Created by hoaxoan on 12/18/2016.
 */

public class SearchListActivity extends BaseActivity {

    private SearchListFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_list_activity);

        int type = getIntent().getIntExtra("type", 0);

        this.fragment = SearchListFragment.newInstance(type);
        showFragment(R.id.fragment_container, this.fragment);

    }

}
