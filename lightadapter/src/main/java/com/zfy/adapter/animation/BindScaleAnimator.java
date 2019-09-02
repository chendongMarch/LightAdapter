package com.zfy.adapter.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * CreateAt : 2018/11/12
 * Describe :
 *
 * @author chendong
 */
public class BindScaleAnimator extends BindAnimator {

    private float fromX = .5f, fromY = .5f;

    public BindScaleAnimator(float fromX, float fromY) {
        this.fromX = fromX;
        this.fromY = fromY;
    }

    public BindScaleAnimator(float from) {
        this.fromX = from;
        this.fromY = from;
    }

    public BindScaleAnimator() {
    }

    @Override
    public Animator[] getAnimators(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", fromX, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", fromY, 1f);
        return new ObjectAnimator[]{scaleX, scaleY};
    }
}
