package com.zfy.adapter.delegate.refs;

import com.zfy.adapter.listener.BindCallback;
import com.zfy.adapter.model.EmptyState;
import com.zfy.adapter.model.LightView;

/**
 * CreateAt : 2018/11/10
 * Describe :
 *
 * @author chendong
 */
public interface EmptyViewRef {

    /**
     * {@inheritDoc}
     * 设置 EmptyView
     *
     * @param lightView LightView
     * @param callback  绑定回调
     */
    void setEmptyView(LightView lightView, BindCallback<EmptyState> callback);

    /**
     * {@inheritDoc}
     *
     * @return emptyView 功能是否可用
     */
    boolean isEmptyEnable();

    /**
     * {@inheritDoc}
     * 设置 Empty 状态
     *
     * @param state 状态
     * @see EmptyState#NONE
     * @see EmptyState#SUCCESS
     * @see EmptyState#ERROR
     * @see EmptyState#NO_DATA
     */
    void setEmptyState(int state);
}
