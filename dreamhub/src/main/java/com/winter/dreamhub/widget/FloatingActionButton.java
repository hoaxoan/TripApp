package com.winter.dreamhub.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;

import com.winter.dreamhub.R;

/**
 * Created by hoaxoan on 5/1/2016.
 */
public class FloatingActionButton extends ImageButton {
    private int mColor;
    private int mIconsResId;
    private boolean mIsHidden;
    private Resources mResources;
    private int mSize;

    public FloatingActionButton(Context context) {
        super(context);
        init(context, null);
    }

    public FloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void createDrawable() {
        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
        ((ShapeDrawable) shapeDrawable).setIntrinsicHeight(this.mSize);
        ((ShapeDrawable) shapeDrawable).setIntrinsicWidth(this.mSize);
        ((ShapeDrawable) shapeDrawable).setBounds(new Rect(0, 0, this.mSize, this.mSize));
        ((ShapeDrawable) shapeDrawable).getPaint().setColor(this.mColor);

        Drawable[] drawables = new Drawable[2];
        StateListDrawable stateListDrawable = new StateListDrawable();
        ((StateListDrawable) stateListDrawable).addState(new int[0], (Drawable) shapeDrawable);
        drawables[0] = stateListDrawable;
        if (this.mIconsResId != 0) {
            drawables[1] = getBitmapDrawable(this.mIconsResId);
        }
        setImageDrawable(new LayerDrawable((Drawable[]) drawables));
        invalidate();
    }

    private BitmapDrawable getBitmapDrawable(int id) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), id);
        BitmapDrawable drawable = new BitmapDrawable(this.mResources, (Bitmap) bitmap);
        ((BitmapDrawable) drawable).setGravity(Gravity.CENTER);
        return (BitmapDrawable) drawable;
    }

    private void init(Context context, AttributeSet attrs) {
        this.mResources = context.getResources();
        this.mIconsResId = 0;
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, new int[]{16843033, 16843173});
            if (a != null) {
                this.mIconsResId = a.getResourceId(0, 0);
               // this.mColor = a.getResourceId(1, -1);
                a.recycle();
            }
        }
        setSize(0);
        this.mIsHidden = false;
        createDrawable();
    }

    public void hide(boolean show) {
        if (!this.mIsHidden) {
            if (!show) {
                setScaleX(0.0F);
                setScaleY(0.0F);
            }
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, "scaleX", new float[]{1.0F, 0.0F}), ObjectAnimator.ofFloat(this, "scaleY", new float[]{1.0F, 0.0F})});
            animatorSet.setDuration(100L);
            animatorSet.start();
        }
        this.mIsHidden = true;
        this.setVisibility(View.GONE);
    }

    public void show(boolean show) {
        if (this.mIsHidden) {
            if (!show) {
                setScaleX(1.0F);
                setScaleY(1.0F);
            }
            AnimatorSet localAnimatorSet = new AnimatorSet();
            localAnimatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, "rotation", new float[]{-180.0F, 0.0F}), ObjectAnimator.ofFloat(this, "scaleX", new float[]{0.0F, 1.0F}), ObjectAnimator.ofFloat(this, "scaleY", new float[]{0.0F, 1.0F})});
            localAnimatorSet.setDuration(100L);
            localAnimatorSet.start();
        }
        this.mIsHidden = false;
        this.setVisibility(View.VISIBLE);
    }

    public void setColor(int color) {
        this.mColor = color;
        createDrawable();
    }

    public void setIcon(int iconsResId) {
        this.mIconsResId = iconsResId;
        createDrawable();
    }

    public void setSize(int type) {
        if (type == 0) {
            this.mSize = this.mResources.getDimensionPixelSize(R.dimen.fab_small_circle_diameter);
        } else {
            this.mSize = this.mResources.getDimensionPixelSize(R.dimen.fab_big_circle_diameter);
        }
        createDrawable();
    }

}
