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
public class AlphaAnimator extends BindAnimator {

    private float from = 0f;

    public AlphaAnimator(float from) {
        this.from = from;
    }

    public AlphaAnimator() {
    }

    @Override
    public Animator[] getAnimators(View view) {
        return new Animator[]{ObjectAnimator.ofFloat(view, "alpha", from, 1f)};
    }
}
