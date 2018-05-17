package com.winter.dreamhub.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by hoaxoan on 10/25/2016.
 */

public class FadingImageView extends ImageView implements Target {
    public Callback callback;
    Drawable drawable;
    Animation fadeInAnimation = new AlphaAnimation(0.2F, 1.0F);
    Animation fadeOutAnimation;
    private Animation.AnimationListener listener;

    public FadingImageView(Context context) {
        super(context);
    }

    public FadingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FadingImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.fadeInAnimation.setDuration(250L);
        //this.fadeInAnimation.setInterpolator(MaterialInterpolators.Interpolators.fastOutLinearIn);
        this.fadeOutAnimation = new AlphaAnimation(1.0F, 0.2F);
        this.fadeOutAnimation.setDuration(250L);
        //this.fadeOutAnimation.setInterpolator(MaterialInterpolators.Interpolators.fastOutLinearIn);
        this.listener = new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                FadingImageView.this.fadeOutAnimation.setAnimationListener(null);
                FadingImageView.this.setImageDrawable(FadingImageView.this.drawable);
                FadingImageView.this.startAnimation(FadingImageView.this.fadeInAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
    }

    private void setNewBitmap(Drawable drawable) {
        this.fadeOutAnimation.setAnimationListener(null);
        clearAnimation();
        if (getDrawable() == null) {
            setImageDrawable(drawable);
            startAnimation(this.fadeInAnimation);
            return;
        }
        this.drawable = drawable;
        this.fadeOutAnimation.setAnimationListener(this.listener);
        startAnimation(this.fadeOutAnimation);
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        setNewBitmap(new BitmapDrawable(getResources(), bitmap));
        if (this.callback != null) {
            this.callback.onError();
        }
    }

    @Override
    public final void onBitmapFailed(Drawable drawable) {
        if (drawable != null) {
            setNewBitmap(drawable);
        }
        if (this.callback != null) {
            this.callback.onError();
        }
    }

    @Override
    public final void onPrepareLoad(Drawable drawable) {
        if (drawable != null) {
            setNewBitmap(drawable);
        }
    }
}
