package com.zfy.adapter.delegate.refs;

import android.view.ViewGroup;

import com.zfy.adapter.listener.ViewHolderCallback;
import com.zfy.adapter.model.LightView;

/**
 * CreateAt : 2018/11/10
 * Describe :
 *
 * @author chendong
 */
public interface FooterRef {


    /**
     * {@inheritDoc}
     *
     * @return 获取 FooterView 容器
     */
    ViewGroup getFooterView();


    /**
     * {@inheritDoc}
     * 删除一个 View
     *
     * @param lightView view
     */
    void removeFooterView(LightView lightView);


    /**
     * {@inheritDoc}
     * 添加 Footer
     *
     * @param lightView LightView
     * @param binder    binder
     */
    void addFooterView(LightView lightView, ViewHolderCallback binder);


    /**
     * {@inheritDoc}
     *
     * @return 当前 Footer 是否可用
     */
    boolean isFooterEnable();


    /**
     * {@inheritDoc}
     * 删除全部的 footer
     */
    void removeAllFooterViews();

    /**
     * {@inheritDoc}
     * 设置 Footer 是否展示
     *
     * @param footerEnable enable
     */
    void setFooterEnable(boolean footerEnable);


    /**
     * {@inheritDoc}
     * 回调所有的绑定更新 Footer
     */
    void notifyFooterUpdate();
}
