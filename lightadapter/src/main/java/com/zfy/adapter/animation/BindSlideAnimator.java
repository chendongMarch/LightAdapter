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
public class BindSlideAnimator extends BindAnimator {

    public static final int BOTTOM = 0;
    public static final int RIGHT  = 1;
    public static final int LEFT   = 2;

    private int direction = LEFT;

    public BindSlideAnimator(int direction) {
        this.direction = direction;
    }

    public BindSlideAnimator() {
    }

    @Override
    public Animator[] getAnimators(View view) {
        switch (direction) {
            case BOTTOM:
                return new Animator[]{ObjectAnimator.ofFloat(view, "translationY", view.getMeasuredHeight(), 0)};
            case RIGHT:
                return new Animator[]{ObjectAnimator.ofFloat(view, "translationX", view.getRootView().getWidth(), 0)};
            case LEFT:
                return new Animator[]{ObjectAnimator.ofFloat(view, "translationX", -view.getRootView().getWidth(), 0)};
        }
        return null;
    }
}
