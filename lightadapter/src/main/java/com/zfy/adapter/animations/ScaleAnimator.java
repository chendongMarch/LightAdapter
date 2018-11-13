package com.zfy.adapter.animations;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * CreateAt : 2018/11/12
 * Describe :
 *
 * @author chendong
 */
public class ScaleAnimator extends LightAnimator {

    private float fromX = .5f, fromY = .5f;

    public ScaleAnimator(float fromX, float fromY) {
        this.fromX = fromX;
        this.fromY = fromY;
    }

    public ScaleAnimator(float from) {
        this.fromX = from;
        this.fromY = from;
    }

    public ScaleAnimator() {
    }

    @Override
    public Animator[] getAnimators(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", fromX, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", fromY, 1f);
        return new ObjectAnimator[]{scaleX, scaleY};
    }
}
