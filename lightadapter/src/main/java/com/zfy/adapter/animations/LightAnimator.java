package com.zfy.adapter.animations;

import android.animation.Animator;
import android.view.View;

/**
 * CreateAt : 2018/11/12
 * Describe :
 *
 * @author chendong
 */
public interface LightAnimator {
    Animator[] getAnimators(View view);
}
