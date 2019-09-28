package com.zfy.lxadapter.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * CreateAt : 2018/11/12
 * Describe :
 *
 * @author chendong
 */
public class BindAlphaAnimator extends BindAnimator {

    private float from = 0f;

    public BindAlphaAnimator(float from) {
        this.from = from;
    }

    public BindAlphaAnimator() {
    }

    @Override
    public Animator[] getAnimators(View view) {
        return new Animator[]{ObjectAnimator.ofFloat(view, "alpha", from, 1f)};
    }
}
