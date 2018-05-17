package com.winter.dreamhub.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winter.dreamhub.R;
import com.winter.dreamhub.util.PrefUtils;

/**
 * Created by hoaxoan on 6/11/2016.
 */
public class WelcomeActivity extends AppCompatActivity {

    private static final int NUM_SCREEN = 3;
    private LinearLayout mScreenContainer;
    private LinearLayout mScreenNavigationBar;
    private ViewPager mViewPager;
    private LinearLayout mDotsLayout;
    private ImageView mBackButton, mNextButton;
    private TextView mDoneButton;
    private OnBoarPagerAdapter mPagerAdapter;
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!PrefUtils.hasFistInstall(this)) {
            launchHomeScreen();
            finish();
        }

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.welcome_activity);
        mScreenContainer = (LinearLayout) findViewById(R.id.onboarding_screen_container);
        mScreenNavigationBar = (LinearLayout) findViewById(R.id.onboarding_screen_navigation_bar_container);
        mViewPager = (ViewPager) findViewById(R.id.onboarding_view_pager);
        mDotsLayout = (LinearLayout) findViewById(R.id.onboarding_screen_page_dot_container);
        mBackButton = (ImageView) findViewById(R.id.onboarding_screen_back_button);
        mNextButton = (ImageView) findViewById(R.id.onboarding_screen_next_button);
        mDoneButton = (TextView) findViewById(R.id.onboarding_screen_done_button);

        mPagerAdapter = new OnBoarPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(viewPagerPageChangeListener);


        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();


        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(-1);
                if (current < NUM_SCREEN && current >= 0) {
                    // move to prev screen
                    mViewPager.setCurrentItem(current);
                }
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(+1);
                if (current < NUM_SCREEN) {
                    // move to next screen
                    mViewPager.setCurrentItem(current);
                }
            }
        });

        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });
    }

    private void launchHomeScreen() {
        PrefUtils.markFirstInstall(this, false);
        startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
        finish();
    }

    private int getItem(int i) {
        return mViewPager.getCurrentItem() + i;
    }


    //	viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            if (position == NUM_SCREEN - 1) {
                mBackButton.setVisibility(View.VISIBLE);
                mNextButton.setVisibility(View.GONE);
                mDoneButton.setVisibility(View.VISIBLE);
            } else if (position == 0) {
                mBackButton.setVisibility(View.GONE);
                mNextButton.setVisibility(View.VISIBLE);
                mDoneButton.setVisibility(View.GONE);
            } else {
                mBackButton.setVisibility(View.VISIBLE);
                mNextButton.setVisibility(View.VISIBLE);
                mDoneButton.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };


    private void addBottomDots(int currentPage) {
        switch (currentPage) {
            case 0:
                mScreenContainer.setBackgroundColor(getResources().getColor(R.color.cluster_name_saved));
                mScreenNavigationBar.setBackgroundColor(getResources().getColor(R.color.cluster_name_saved));
                break;
            case 1:
                mScreenContainer.setBackgroundColor(getResources().getColor(R.color.cluster_name_travel));
                mScreenNavigationBar.setBackgroundColor(getResources().getColor(R.color.cluster_name_travel));
                break;
            case 2:
                mScreenContainer.setBackgroundColor(getResources().getColor(R.color.white_text));
                mScreenNavigationBar.setBackgroundColor(getResources().getColor(R.color.cluster_name_finance));
                break;
        }
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dots = new ImageView[NUM_SCREEN];

        mDotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = (ImageView) inflater.inflate(R.layout.onboarding_screen_page_dot, null, false);
            dots[i].setImageResource(R.drawable.ic_oobe_pagedots_off);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int paddingLeft = (int) getResources().getDimension(R.dimen.onboarding_screen_padding);
            layoutParams.setMargins(dots[i].getLeft() + paddingLeft, dots[i].getTop(), dots[i].getRight(), dots[i].getBottom());
            dots[i].setLayoutParams(layoutParams);

            mDotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[currentPage].setImageResource(R.drawable.ic_oobe_pagedots_on);
        }
    }

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * View pager adapter
     */
    public class OnBoarPagerAdapter extends FragmentPagerAdapter {

        public OnBoarPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return OnboarFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return NUM_SCREEN;
        }
    }


}
