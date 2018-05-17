package com.winter.dreamhub.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.winter.dreamhub.util.Utils;

import java.util.ArrayList;

public class LayoutDirectionAwareViewPager extends ViewPager {
    private final ArrayList<DirectionAwareOnPageChangeListener> mDirectionAwareListeners = new ArrayList(1);
    DirectionAwarePagerAdapter mDirectionAwarePagerAdapter;
    private boolean mIgnoreLayoutDirection = false;
    private boolean mUsesRtl = false;

    public LayoutDirectionAwareViewPager(Context context) {
        super(context);
    }

    public LayoutDirectionAwareViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int getDirectionAwarePosition(int position) {
        int direction = position;
        if (this.mDirectionAwarePagerAdapter != null) {
            direction = this.mDirectionAwarePagerAdapter.getDirectionAwarePosition(position);
        }
        return direction;
    }

    @Override
    public void addOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        DirectionAwareOnPageChangeListener changeListener = new DirectionAwareOnPageChangeListener(listener);
        this.mDirectionAwareListeners.add(changeListener);
        super.addOnPageChangeListener(changeListener);
    }

    @Override
    public PagerAdapter getAdapter() {
        PagerAdapter adapter = super.getAdapter();
        if (adapter != null) {
            return ((DirectionAwarePagerAdapter) adapter).directionUnawarePagerAdapter;
        }
        return null;
    }

    @Override
    public int getCurrentItem() {
        return getDirectionAwarePosition(super.getCurrentItem());
    }

    protected boolean isRtl() {
        if (this.mIgnoreLayoutDirection) {
            return false;
        }
        return Utils.isLayoutDirectionRtl(getContext());
    }

    @Override
    public void onRtlPropertiesChanged(int rtl) {
        updateForRtl();
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        if (this.mDirectionAwarePagerAdapter != null) {
            this.mDirectionAwarePagerAdapter.destroy();
            this.mDirectionAwarePagerAdapter = null;
        }
        if (adapter != null) {
            this.mDirectionAwarePagerAdapter = new DirectionAwarePagerAdapter(adapter);
            this.mDirectionAwarePagerAdapter.setUsesRtl(this.mUsesRtl);
        }
        super.setAdapter(this.mDirectionAwarePagerAdapter);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(getDirectionAwarePosition(item));
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(getDirectionAwarePosition(item), smoothScroll);
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        super.setOnPageChangeListener(new DirectionAwareOnPageChangeListener(listener));
    }

    protected void updateForRtl() {
        boolean bool = isRtl();
        if (bool != this.mUsesRtl) {
            int i = getCurrentItem();
            this.mUsesRtl = bool;
            if (this.mDirectionAwarePagerAdapter != null) {
                this.mDirectionAwarePagerAdapter.setUsesRtl(bool);
            }
            setCurrentItem(i, false);
        }
    }

    private final class DirectionAwareOnPageChangeListener
            implements ViewPager.OnPageChangeListener {
        private final ViewPager.OnPageChangeListener directionUnawareListener;

        public DirectionAwareOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
            this.directionUnawareListener = listener;
        }

        private float getDirectionAwarePositionOffset(float offset) {
            float positionOffset = offset;
            if (LayoutDirectionAwareViewPager.this.mUsesRtl) {
                positionOffset = -offset;
            }
            return positionOffset;
        }

        private int getDirectionAwarePositionOffsetPixels(int positionOffsetPixels) {
            int positionOffset = positionOffsetPixels;
            if (LayoutDirectionAwareViewPager.this.mUsesRtl) {
                positionOffset = -positionOffsetPixels;
            }
            return positionOffset;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            this.directionUnawareListener.onPageScrollStateChanged(state);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            this.directionUnawareListener.onPageScrolled(LayoutDirectionAwareViewPager.this.getDirectionAwarePosition(position),
                    getDirectionAwarePositionOffset(positionOffset),
                    getDirectionAwarePositionOffsetPixels(positionOffsetPixels));
        }

        @Override
        public void onPageSelected(int position) {
            this.directionUnawareListener.onPageSelected(LayoutDirectionAwareViewPager.this.getDirectionAwarePosition(position));
        }
    }

    private static class DirectionAwarePagerAdapter
            extends PagerAdapter {
        private final DataSetObserver directionUnawareDataSetObserver;
        private final PagerAdapter directionUnawarePagerAdapter;
        private boolean usesRtl;

        public DirectionAwarePagerAdapter(PagerAdapter adapter) {
            this.directionUnawarePagerAdapter = adapter;
            this.directionUnawareDataSetObserver = new DataSetObserver() {
                @Override
                public void onChanged() {
                    LayoutDirectionAwareViewPager.DirectionAwarePagerAdapter.this.notifyDataSetChanged();
                }
            };
            this.directionUnawarePagerAdapter.registerDataSetObserver(this.directionUnawareDataSetObserver);
        }

        private void destroy() {
            this.directionUnawarePagerAdapter.unregisterDataSetObserver(this.directionUnawareDataSetObserver);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            this.directionUnawarePagerAdapter.destroyItem(container, getDirectionAwarePosition(position), object);
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            this.directionUnawarePagerAdapter.finishUpdate(container);
        }

        public int getCount() {
            return this.directionUnawarePagerAdapter.getCount();
        }

        int getDirectionAwarePosition(int position) {
            int directPosition = position;
            if (this.usesRtl) {
                directPosition = getCount() - position - 1;
            }
            return directPosition;
        }

        @Override
        public int getItemPosition(Object object) {
            int position = this.directionUnawarePagerAdapter.getItemPosition(object);
            if (position < 0) {
                return position;
            }
            return getDirectionAwarePosition(position);
        }

        @Override
        public float getPageWidth(int position) {
            return this.directionUnawarePagerAdapter.getPageWidth(getDirectionAwarePosition(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return this.directionUnawarePagerAdapter.instantiateItem(container, getDirectionAwarePosition(position));
        }

        public boolean isViewFromObject(View view, Object object) {
            return this.directionUnawarePagerAdapter.isViewFromObject(view, object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
            this.directionUnawarePagerAdapter.restoreState(state, loader);
        }

        @Override
        public Parcelable saveState() {
            return this.directionUnawarePagerAdapter.saveState();
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            this.directionUnawarePagerAdapter.setPrimaryItem(container, getDirectionAwarePosition(position), object);
        }

        void setUsesRtl(boolean usesRtl) {
            this.usesRtl = usesRtl;
            notifyDataSetChanged();
        }

        @Override
        public void startUpdate(ViewGroup container) {
            this.directionUnawarePagerAdapter.startUpdate(container);
        }
    }
}