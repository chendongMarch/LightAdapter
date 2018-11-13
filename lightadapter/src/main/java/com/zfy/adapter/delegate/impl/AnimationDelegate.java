package com.zfy.adapter.delegate.impl;

import android.animation.Animator;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zfy.adapter.LightHolder;
import com.zfy.adapter.animations.LightAnimator;
import com.zfy.adapter.delegate.refs.AnimatorRef;
import com.zfy.adapter.model.ModelType;

/**
 * CreateAt : 2018/11/12
 * Describe : 动画代理实现，支持 BindAnimator / ItemAnimator
 *
 * @author chendong
 */
public class AnimationDelegate extends BaseDelegate implements AnimatorRef {

    private int          mLastPosition = -1;
    private boolean mAnimOnce = true;
    private LightAnimator mLightAnimator;

    @Override
    public int getKey() {
        return ANIMATOR;
    }

    @Override
    public boolean onBindViewHolder(LightHolder holder, int layoutIndex) {
        if (mLightAnimator == null) {
            return super.onBindViewHolder(holder, layoutIndex);
        }
        ModelType modelType = mAdapter.getModelType(mAdapter.getItemViewType(layoutIndex));
        LightAnimator animator = (modelType == null || modelType.animator == null) ? mLightAnimator : modelType.animator;
        int adapterPosition = holder.getAdapterPosition();
        if (!mAnimOnce || adapterPosition > mLastPosition) {
            for (Animator anim : animator.getAnimators(holder.itemView)) {
                anim.setDuration(animator.getDuration()).start();
                anim.setInterpolator(animator.getInterceptor());
            }
            mLastPosition = adapterPosition;
        } else {
            clear(holder.itemView);
        }
        return super.onBindViewHolder(holder, layoutIndex);
    }

    @Override
    public void setBindAnimator(LightAnimator animator) {
        mLightAnimator = animator;
    }

    @Override
    public void setBindAnimatorOnlyOnce(boolean animOnlyOnce) {
        mAnimOnce = animOnlyOnce;
    }

    @Override
    public void setItemAnimator(RecyclerView.ItemAnimator animator) {
        postOnRecyclerViewAttach(() -> mView.setItemAnimator(animator));
    }


    private void clear(View v) {
        v.setAlpha(1);
        v.setScaleY(1);
        v.setScaleX(1);
        v.setTranslationY(0);
        v.setTranslationX(0);
        v.setRotation(0);
        v.setRotationY(0);
        v.setRotationX(0);
        v.setPivotY(v.getMeasuredHeight() / 2);
        v.setPivotX(v.getMeasuredWidth() / 2);
        v.animate().setInterpolator(null).setStartDelay(0);
    }


}
