package com.zfy.adapter.delegate.refs;

import android.support.v7.widget.RecyclerView;

import com.zfy.adapter.animation.IAnimator;
import com.zfy.adapter.animation.BindAnimator;

/**
 * CreateAt : 2018/11/12
 * Describe :
 *
 * @author chendong
 */
public interface AnimatorRef {

    /**
     * 设置绑定的时候的 Animator，他会在 onBindViewHolder 的时候绑定动画
     *
     * @param animator animator
     * @see IAnimator
     * @see com.zfy.adapter.animation.AlphaAnimator
     * @see com.zfy.adapter.animation.ScaleAnimator
     * @see com.zfy.adapter.animation.SlideAnimator
     */
    void setBindAnimator(BindAnimator animator);

    /**
     * bind 动画只执行一次
     *
     * @param animOnlyOnce 是否只绑定动画一次
     *                     true 表示只有第一次绑定的时候会生效，false 表示每次从页面划出都会重新绑定动画
     */
    void setBindAnimatorOnlyOnce(boolean animOnlyOnce);

    /**
     * 设置 ItemAnimator，他会在局部更新时调用这个动画
     *
     * @param animator animator
     */
    void setItemAnimator(RecyclerView.ItemAnimator animator);

    /**
     * 设置动画是否可用
     * @param enable 动画是否可用
     */
    void setAnimatorEnable(boolean enable);
}
