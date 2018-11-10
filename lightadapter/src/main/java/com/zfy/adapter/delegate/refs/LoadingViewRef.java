package com.zfy.adapter.delegate.refs;

import com.zfy.adapter.listener.BindCallback;
import com.zfy.adapter.model.LightView;
import com.zfy.adapter.model.LoadingState;

/**
 * CreateAt : 2018/11/10
 * Describe :
 *
 * @author chendong
 */
public interface LoadingViewRef {
    /**
     * {@inheritDoc}
     * 设置 LoadingView
     *
     * @param lightView LightView
     * @param callback  绑定回调
     * @see LoadingState#INIT
     * @see LoadingState#FINISH
     * @see LoadingState#LOADING
     * @see LoadingState#NO_DATA
     */
    void setLoadingView(LightView lightView, BindCallback<LoadingState> callback);

    /**
     * {@inheritDoc}
     * 设置 loading view 显示状态
     *
     * @param loadingEnable 是否可以显示
     */
    void setLoadingEnable(boolean loadingEnable);

    /**
     * {@inheritDoc}
     *
     * @return loadingView 是否可见
     */
    boolean isLoadingEnable();

    /**
     * {@inheritDoc}
     * 设置 Loading 状态
     *
     * @param state 状态
     * @see LoadingState#FINISH
     * @see LoadingState#INIT
     * @see LoadingState#LOADING
     * @see LoadingState#NO_DATA
     */
    void setLoadingState(int state);
}
