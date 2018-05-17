package com.winter.dreamhub.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import com.winter.dreamhub.R;
import com.winter.dreamhub.util.AnimatorHelper;

import static com.winter.dreamhub.util.LogUtils.makeLogTag;

/**
 * Created by hoaxoan on 5/1/2016.
 */
public class BackButtonView extends ImageButton {
    private static final String TAG = makeLogTag(BackButtonView.class);
    private int mColorTheme;
    private int mIcon;

    public BackButtonView(Context context) {
        this(context, null, 0);
        init(context);
    }

    public BackButtonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context);
    }

    public BackButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private static int getContentDescResId(int resId) {
        switch (resId) {
            case 0:
                return R.string.accessibility_navigation_icon;
            default:
                return R.string.close_label;
        }
    }

    private static int getDrawableResId(int type, int resId) {
        if (type == 1) {
            switch (resId) {
                case 0:
                    return R.drawable.quantum_ic_close_white_24;
                case 1:
                    return R.drawable.quantum_ic_close_black_24;
                default:
                    return R.drawable.quantum_ic_close_grey600_24;
            }
        } else {
            switch (resId) {
                case 0:
                    return R.drawable.quantum_ic_arrow_back_white_24;
                case 1:
                    return R.drawable.quantum_ic_arrow_back_black_24;
                default:
                    return R.drawable.quantum_ic_arrow_back_grey600_24;
            }
        }
    }


    private void init(final Context context) {
        this.mColorTheme = 1;
        this.mIcon = 0;
        setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if ((context instanceof Activity)) {
                    ((Activity) context).onBackPressed();
                }
            }
        });
    }

    public void setIcon(int icon) {
        this.mIcon = icon;
        setImageDrawable(getResources().getDrawable(getDrawableResId(this.mIcon, this.mColorTheme)));
        setContentDescription(getResources().getString(getContentDescResId(this.mIcon)));
    }

    public void setTheme(int colorTheme, boolean show) {
        if (this.mColorTheme == colorTheme) {
            return;
        }
        this.mColorTheme = colorTheme;
        if (!show) {
            setImageDrawable(getResources().getDrawable(getDrawableResId(this.mIcon, this.mColorTheme)));
            return;
        }
        AnimatorHelper.createFadeOutFadeInAnimator(this, new AnimatorHelper.FadeOutFadeInAnimatorListener() {
            public void fadeOutAnimationEnd() {
                BackButtonView.this.setImageDrawable(BackButtonView.this.getResources().getDrawable(BackButtonView.getDrawableResId(BackButtonView.this.mIcon, BackButtonView.this.mColorTheme)));
            }
        }).start();
    }
}
