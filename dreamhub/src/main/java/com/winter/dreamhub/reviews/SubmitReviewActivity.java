package com.winter.dreamhub.reviews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.winter.dreamhub.R;
import com.winter.dreamhub.activity.BaseActivity;
import com.winter.dreamhub.api.service.response.ReviewResponse;

import static com.winter.dreamhub.util.LogUtils.makeLogTag;

/**
 * Created by hoaxoan on 11/5/2017.
 */

public class SubmitReviewActivity extends BaseActivity
        implements SubmitReviewFragment.ReviewCallback {
    private static final String TAG = makeLogTag(SubmitReviewActivity.class);

    private SubmitReviewFragment fragment;
    private Toolbar mActionBarToolbar;

    private long id;
    private String name;
    private String userName;
    private float rating;

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.submit_review_activity);


        this.id = getIntent().getLongExtra("id", 0);
        this.name = getIntent().getStringExtra("name");
        this.userName = getIntent().getStringExtra("userName");
        this.rating = getIntent().getFloatExtra("rating", 0);

        mActionBarToolbar = getActionBarToolbar();
        mActionBarToolbar.setNavigationIcon(R.drawable.quantum_ic_close_grey600_24);
        mActionBarToolbar.setTitleTextColor(getResources().getColor(R.color.quantum_black_secondary_text));
        setSupportActionBar(mActionBarToolbar);
        if (!TextUtils.isEmpty(this.name)) {
            mActionBarToolbar.setTitle(this.name);
        } else {
            mActionBarToolbar.setTitle(getString(R.string.reviews_label));
        }

        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        this.fragment = SubmitReviewFragment.newInstance(this, this.id, this.userName, this.rating);
        showFragment(R.id.fragment_container, this.fragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feedback_bar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_feedback:
                fragment.postReview();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPostReviewSuccess(ReviewResponse response) {

    }
}
