package com.winter.dreamhub.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.winter.dreamhub.R;
import com.winter.dreamhub.activity.BaseActivity;
import com.winter.dreamhub.util.ImageUtils;

/**
 * Created by hoaxoan on 10/27/2016.
 */

public class TabsHeaderScrollingController implements VerticalScrollListener {
    private BaseActivity activity;
    private Context context;
    private ValueAnimator animator;
    private FrameLayout headerHolder;
    private int headerOffset;
    private ImageView illustration;
    int maxScrollableSpaceInHeader;
    private Toolbar toolbar;
    private MenuItem tripEditButton;


    public TabsHeaderScrollingController(BaseActivity context, FrameLayout headerHolder, ImageView illustration, boolean scrollable, ImageUtils imageUtils) {
        this.context = context;
        this.activity = context;
        this.toolbar = context.mActionBarToolbar;
        this.headerHolder = headerHolder;
        this.illustration = illustration;
        this.maxScrollableSpaceInHeader = (imageUtils.getHeaderHeight() - context.getResources().getDimensionPixelSize(R.dimen.toolbar_height));
        if (scrollable) {
            this.maxScrollableSpaceInHeader -= context.getResources().getDimensionPixelSize(R.dimen.tabs_layout_height);
        }
    }

    private final void animatedHeaderSliding(float paramFloat1, float paramFloat2) {
        if ((this.animator != null) && (this.animator.isRunning())) {
            this.animator.cancel();
        }
        this.animator = ValueAnimator.ofFloat(new float[]{paramFloat1, paramFloat2}).setDuration(200L);
        this.animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public final void onAnimationUpdate(ValueAnimator animator) {
                float offset = ((Float) animator.getAnimatedValue()).floatValue();
                TabsHeaderScrollingController.this.scrollHeaderToOffset((int) offset);
            }
        });
        this.animator.start();
    }

    private final int restrictedOffset(int headerOffset) {
        return Math.max(Math.min(this.maxScrollableSpaceInHeader, headerOffset), 0);
    }


    public final void onPause() {
        if ((this.animator != null) && (this.animator.isRunning())) {
            this.animator.cancel();
            this.animator = null;
        }
    }

    final void scrollHeaderToOffset(int headerOffset) {
        this.headerOffset = headerOffset;

        headerOffset = Math.max(Math.min(headerOffset, this.maxScrollableSpaceInHeader), 0);
        int alpha = 0;
        if (this.maxScrollableSpaceInHeader != 0) {
            alpha = (this.maxScrollableSpaceInHeader - headerOffset) * 255 / this.maxScrollableSpaceInHeader;
        }

        if (this.illustration != null) {
            this.illustration.setImageAlpha(alpha);
        }

        this.headerHolder.setTranslationY(-headerOffset);

        if (this.toolbar != null) {
            this.toolbar.setBackgroundColor(this.context.getResources().getColor(R.color.quantum_white_100));
            this.activity.setStatusBarColorIfNeeded(R.color.quantum_grey400);
            this.toolbar.setTitleTextColor(this.context.getResources().getColor(R.color.quantum_black_secondary_text));
            this.toolbar.setSubtitleTextColor(this.context.getResources().getColor(R.color.quantum_black_secondary_text));
            this.toolbar.setNavigationIcon(R.drawable.quantum_ic_arrow_back_grey600_24);
            this.toolbar.setOverflowIcon(this.context.getResources().getDrawable(R.drawable.quantum_ic_drawer_grey600_24));
        }
    }

    @Override
    public void onScrolled(int position) {
        if ((this.animator != null) && (this.animator.isRunning())) {
            this.animator.cancel();
        }
        scrollHeaderToOffset(restrictedOffset(position));
    }

    @Override
    public void slideHeaderOut() {
        animatedHeaderSliding(this.headerOffset, this.maxScrollableSpaceInHeader);
    }

    @Override
    public void slideHeaderToFirstCardTop(int position) {
        animatedHeaderSliding(this.headerOffset, restrictedOffset(position));
    }
}
