package com.zfy.adapter.delegate.refs;

import android.view.ViewGroup;

import com.zfy.adapter.callback.ViewHolderCallback;
import com.zfy.adapter.model.LightView;

/**
 * CreateAt : 2018/11/10
 * Describe :
 *
 * @author chendong
 */
public interface HeaderRef {


    /**
     * {@inheritDoc}
     *
     * @return 获取 HeaderView 容器
     */
    ViewGroup getHeaderView();

    /**
     * {@inheritDoc}
     * 删除一个 View
     *
     * @param lightView lightView
     */
    void removeHeaderView(LightView lightView);


    /**
     * {@inheritDoc}
     * 添加 Header
     *
     * @param lightView LightView
     * @param binder    数据绑定回调
     */
    void addHeaderView(LightView lightView, ViewHolderCallback binder);


    /**
     * {@inheritDoc}
     *
     * @return 当前 Header 是否可用
     */
    boolean isHeaderEnable();


    /**
     * {@inheritDoc}
     * 删除全部的 header
     */
    void removeAllHeaderViews();


    /**
     * {@inheritDoc}
     * 设置 Header 是否展示
     *
     * @param headerEnable enable
     */
    void setHeaderEnable(boolean headerEnable);

    /**
     * {@inheritDoc}
     * <p>
     * 回调所有的绑定更新 Header
     */
    void notifyHeaderUpdate();

}
