package com.zfy.adapter.delegate.refs;

import android.support.annotation.LayoutRes;

import com.zfy.adapter.callback.BindCallback;
import com.zfy.adapter.model.ModelType;

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
     * @param type ModelType
     * @param bindCallback   绑定隔断显示
     */
    void setOptions(ModelType type, BindCallback<D> bindCallback);

    /**
     * {@inheritDoc}
     *
     * @param layoutId   隔断布局
     * @param supportPin 是否支持悬停
     * @param callback   绑定隔断显示
     */
    void setOptions(@LayoutRes int layoutId, boolean supportPin, BindCallback<D> callback);
}
