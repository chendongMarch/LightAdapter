package com.zfy.adapter.animation;

import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * CreateAt : 2018/11/13
 * Describe :
 *
 * @author chendong
 */
public abstract class BindAnimator implements IAnimator {

    private int duration = 300;

    private Interpolator interceptor = new LinearInterpolator();

    public int getDuration() {
        return duration;
    }

    public Interpolator getInterceptor() {
        return interceptor;
    }

    @Override
    public BindAnimator duration(int duration) {
        this.duration = duration;
        return this;
    }

    @Override
    public BindAnimator interceptor(Interpolator interceptor) {
        this.interceptor = interceptor;
        return this;
    }
}
