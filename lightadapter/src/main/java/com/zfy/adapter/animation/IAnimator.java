package com.zfy.adapter.animation;

import android.animation.Animator;
import android.view.View;
import android.view.animation.Interpolator;

/**
 * CreateAt : 2018/11/12
 * Describe :
 *
 * @author chendong
 */
public interface IAnimator {

    Animator[] getAnimators(View view);

    IAnimator duration(int duration);

    IAnimator interceptor(Interpolator interceptor);
}
