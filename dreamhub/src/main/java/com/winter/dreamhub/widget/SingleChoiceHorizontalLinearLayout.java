package com.winter.dreamhub.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.google.common.base.Preconditions;

import java.util.ArrayList;

/**
 * Created by hoaxoan on 10/24/2016.
 */

public class SingleChoiceHorizontalLinearLayout extends LinearLayout {
    public final ArrayList<View> children = new ArrayList();
    private final ChildrenClickListener childrenClickListener = new ChildrenClickListener();
    OnSelectListener onSelectListener;
    OnUnselectListener onUnselectListener;
    int selectedPosition = -1;

    public SingleChoiceHorizontalLinearLayout(Context context) {
        super(context);
    }

    public SingleChoiceHorizontalLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SingleChoiceHorizontalLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setHorizontalScrollBarEnabled(false);
        setOrientation(LinearLayout.HORIZONTAL);
        setFocusable(false);
        setClickable(false);
    }

    public final void addView(View view) {
        addView(view, view.getLayoutParams().width, view.getLayoutParams().height);
    }

    public final void addView(View view, int width, int height) {
        Preconditions.checkNotNull(view);
        this.children.add(view);
        super.addView(view, width, height);
        view.setOnClickListener(this.childrenClickListener);
        if (this.children.size() < 2) {
            setVisibility(View.GONE);
        } else {
            setVisibility(View.VISIBLE);
        }
    }

    public final View getSelectedChild() {
        if (this.selectedPosition != -1) {
            return (View) this.children.get(this.selectedPosition);
        }
        return null;
    }

    public final void selectChild(int position) {
        if (this.selectedPosition == position) {
            return;
        }
        if (this.selectedPosition != -1) {
            View currentView = (View) this.children.get(this.selectedPosition);
            currentView.setSelected(false);
            if (this.onUnselectListener != null) {
                this.onUnselectListener.unSelect(currentView);
            }
        }

        View selectView = (View) this.children.get(position);
        selectView.setSelected(true);
        this.selectedPosition = position;
        this.onSelectListener.onSelect(selectView, this.selectedPosition);
    }

    private final class ChildrenClickListener
            implements View.OnClickListener {
        ChildrenClickListener() {
        }

        public final void onClick(View view) {
            int position = SingleChoiceHorizontalLinearLayout.this.children.indexOf(view);
            if (position != -1) {
                SingleChoiceHorizontalLinearLayout.this.selectChild(position);
            }
        }
    }

    public interface OnSelectListener {
        void onSelect(View view, int position);
    }

    public interface OnUnselectListener {
        void unSelect(View view);
    }
}
