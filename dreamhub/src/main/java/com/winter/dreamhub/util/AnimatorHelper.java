package com.winter.dreamhub.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by hoaxoan on 5/1/2016.
 */
public class AnimatorHelper {
    public static AnimatorSet createFadeOutFadeInAnimator(View view, final FadeOutFadeInAnimatorListener listener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", new float[]{1.0F, 0.0F});
        animator.addListener(new Animator.AnimatorListener() {
            public void onAnimationCancel(Animator paramAnonymousAnimator) {
            }

            public void onAnimationEnd(Animator paramAnonymousAnimator) {
                listener.fadeOutAnimationEnd();
            }

            public void onAnimationRepeat(Animator paramAnonymousAnimator) {
            }

            public void onAnimationStart(Animator paramAnonymousAnimator) {
            }
        });
        animator.setDuration(52L);

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "alpha", new float[]{0.0F, 1.0F});
        animator2.setDuration(52L);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.playSequentially(new Animator[]{animator, animator2});
        return animatorSet;
    }

    public interface FadeOutFadeInAnimatorListener {
        void fadeOutAnimationEnd();
    }
}
