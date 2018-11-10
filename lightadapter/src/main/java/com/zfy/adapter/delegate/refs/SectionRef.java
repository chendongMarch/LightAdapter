package com.zfy.adapter.delegate.refs;

import android.support.annotation.LayoutRes;

import com.zfy.adapter.listener.BindCallback;
import com.zfy.adapter.model.SingleTypeConfigCallback;

/**
 * CreateAt : 2018/11/10
 * Describe :
 *
 * @author chendong
 */
public interface SectionRef<D> {

    /**
     * {@inheritDoc}
     * 开启支持悬停
     *
     * @param pinEnable 是否支持悬停
     */
    void setPinEnable(boolean pinEnable);

    /**
     * {@inheritDoc}
     * 设置隔断配置选项
     *
     * @param configCallback 设置 section 类型的 configCallback
     * @param bindCallback   绑定隔断显示
     */
    void setOptions(SingleTypeConfigCallback configCallback, BindCallback<D> bindCallback);

    /**
     * {@inheritDoc}
     *
     * @param layoutId   隔断布局
     * @param supportPin 是否支持悬停
     * @param callback   绑定隔断显示
     */
    void setOptions(@LayoutRes int layoutId, boolean supportPin, BindCallback<D> callback);
}
