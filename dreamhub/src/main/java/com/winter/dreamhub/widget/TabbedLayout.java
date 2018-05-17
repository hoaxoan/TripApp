package com.winter.dreamhub.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.util.List;

import com.winter.dreamhub.R;
import com.winter.dreamhub.util.Typefaces;

/**
 * Created by hoaxoan on 10/24/2016.
 */

public class TabbedLayout extends LinearLayout implements SingleChoiceHorizontalLinearLayout.OnSelectListener, SingleChoiceHorizontalLinearLayout.OnUnselectListener {
    static final Rect EMPTY_RECT = new Rect(0, 0, 0, 0);
    boolean enableUnderscore = true;
    Paint highlightPaint;
    private ViewPager.OnPageChangeListener onPageChangeListener;
    int screenWidth;
    UnderscoringHorizontalScrollView scrollableContainer;
    public OnTabSelectListener tabSelectListener;
    public SingleChoiceHorizontalLinearLayout tabsLayout;
    Rect underscoreRect;
    int underscoreRectHeight;
    private ViewPager viewPager;

    public TabbedLayout(Context context) {
        super(context);
    }

    public TabbedLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(LinearLayout.VERTICAL);
        setWillNotDraw(false);
        this.scrollableContainer = new UnderscoringHorizontalScrollView(context);
        addView(this.scrollableContainer, new LinearLayout.LayoutParams(-1, 0, 1.0F));
        this.scrollableContainer.setFillViewport(true);
        this.scrollableContainer.setHorizontalScrollBarEnabled(false);
        this.onPageChangeListener = new DefaultOnPageChangeListener();
        this.tabsLayout = new SingleChoiceHorizontalLinearLayout(context);
        this.tabsLayout.onSelectListener = this;
        this.tabsLayout.onUnselectListener = this;
        this.scrollableContainer.addView(this.tabsLayout, -1, -1);
        this.scrollableContainer.setOverScrollMode(View.OVER_SCROLL_NEVER);
        this.highlightPaint = new Paint();
        this.highlightPaint.setStyle(Paint.Style.FILL);
        this.highlightPaint.setColor(getResources().getColor(R.color.quantum_white_100));
        this.underscoreRectHeight = getResources().getDimensionPixelSize(R.dimen.bottom_shadow_height);
        this.screenWidth = getResources().getDisplayMetrics().widthPixels;
        inflate(context, R.layout.bottom_shadow, this);
    }

    public final void addIconTab(Drawable drawable) {
        this.enableUnderscore = false;
        ImageView imageView = new ImageView(getContext());
        imageView.setTag("ICON_TAB");
        int i = getResources().getDimensionPixelSize(R.dimen.tab_icon_left_right_padding);
        imageView.setPadding(i, 0, i, 0);
        imageView.setImageDrawable(drawable);
        this.tabsLayout.addView(imageView, -2, -2);
    }

    public final void addTextTab(String title, int selected, int unSelected) {
        TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.single_tab_layout, this.tabsLayout, false);
        textView.setTag("TEXT_TAB");
        textView.setTypeface(Typefaces.getTypeface(getContext(), "ROBOTO_MEDIUM", 0));
        textView.setTextColor(new ColorStateList(new int[][]{{android.R.attr.state_selected}, {-android.R.attr.state_selected}}, new int[]{unSelected, selected}));
        textView.setText(title);
        this.tabsLayout.addView(textView);
    }

    public final void centerTabs() {
        this.tabsLayout.setGravity(0);
    }

    public final void disableBottomShadow() {
        findViewById(R.id.shadow).setVisibility(View.GONE);
    }

    @Override
    public void onSelect(View view, int position) {
        if (this.tabSelectListener != null) {
            this.tabSelectListener.onSelect(view, position);
        }
        if ("TEXT_TAB".equals(view.getTag())) {
            view.findViewById(R.id.tab_title).setSelected(true);
        } else {
            view.setSelected(true);
        }

        if (this.viewPager != null) {
            this.viewPager.setCurrentItem(position, true);
        }

    }

    public final void selectTab(int position) {
        this.tabsLayout.selectChild(position);
    }

    public final void setTabsBackgroundColor(int color) {
        this.scrollableContainer.setBackgroundColor(color);
    }

    public final void setUnderscoreColor(int color) {
        this.highlightPaint.setColor(color);
    }

    public final void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        this.viewPager.addOnPageChangeListener(this.onPageChangeListener);
    }


    @Override
    public void unSelect(View view) {
        if ("TEXT_TAB".equals(view.getTag())) {
            view.findViewById(R.id.tab_title).setSelected(false);
        } else {
            view.setSelected(false);
        }
    }

    private final class DefaultOnPageChangeListener
            implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int selectedPosition = TabbedLayout.this.tabsLayout.selectedPosition;
            View selectedChild = TabbedLayout.this.tabsLayout.getSelectedChild();
            float right = 0.0F;
            float left = 0.0F;
            if ((selectedPosition == position) && (position < TabbedLayout.this.tabsLayout.children.size() - 1)) {
                View view = (View) TabbedLayout.this.tabsLayout.children.get(position + 1);
                right = ((View) selectedChild).getRight() + (view.getRight() - ((View) selectedChild).getRight()) * positionOffsetPixels;
                left = ((View) selectedChild).getLeft();
                float offset = (view.getLeft() - ((View) selectedChild).getLeft()) * positionOffsetPixels + left;
            } else if ((selectedPosition == position + 1) && (position >= 0)) {
                View view = (View) TabbedLayout.this.tabsLayout.children.get(position);
                right = ((View) selectedChild).getRight() - (((View) selectedChild).getRight() - view.getRight()) * (1.0F - positionOffsetPixels);
                left = ((View) selectedChild).getLeft() - (((View) selectedChild).getLeft() - view.getLeft()) * (1.0F - positionOffsetPixels);
            } else {
                right = 0.0F;
                left = 0.0F;
                positionOffset = 0;
                positionOffset = 0;
            }

            TabbedLayout.this.underscoreRect = new Rect((int) positionOffsetPixels,
                    Math.max(0, ((View) selectedChild).getBottom() - TabbedLayout.this.underscoreRectHeight),
                    (int) right, ((View) selectedChild).getBottom());
            float x = Math.max(0, (((TabbedLayout) TabbedLayout.this).underscoreRect.right + ((TabbedLayout) TabbedLayout.this).underscoreRect.left) / 2 - ((TabbedLayout) TabbedLayout.this).screenWidth / 2);
            ((TabbedLayout) TabbedLayout.this).scrollableContainer.scrollTo((int) x, 0);
            TabbedLayout.this.scrollableContainer.invalidate();

        }

        @Override
        public void onPageSelected(int position) {
            String.format("select page %d in ViewPager", new Object[]{Integer.valueOf(position)});
            TabbedLayout.this.tabsLayout.selectChild(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public interface OnTabSelectListener {
        void onSelect(View view, int position);
    }

    private final class UnderscoringHorizontalScrollView
            extends HorizontalScrollView {

        public UnderscoringHorizontalScrollView(Context context) {
            super(context);
        }

        public UnderscoringHorizontalScrollView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public UnderscoringHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if ((TabbedLayout.this.tabsLayout.selectedPosition != -1)) {
                View selectedChild = TabbedLayout.this.tabsLayout.getSelectedChild();
                TabbedLayout.this.underscoreRect = new Rect(((View) selectedChild).getLeft(), ((View) selectedChild).getBottom() - TabbedLayout.this.underscoreRectHeight, ((View) selectedChild).getRight(), ((View) selectedChild).getBottom());
                float x = Math.max(0, (((TabbedLayout) TabbedLayout.this).underscoreRect.right + ((TabbedLayout) TabbedLayout.this).underscoreRect.left) / 2 - ((TabbedLayout) TabbedLayout.this).screenWidth / 2);
                ((TabbedLayout) TabbedLayout.this).scrollableContainer.scrollTo((int) x, 0);
            }
            canvas.drawRect(TabbedLayout.this.underscoreRect, TabbedLayout.this.highlightPaint);

        }
    }
}
