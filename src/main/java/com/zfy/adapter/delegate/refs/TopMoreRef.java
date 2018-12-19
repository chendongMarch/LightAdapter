package com.zfy.adapter.delegate.refs;

import com.zfy.adapter.listener.AdapterCallback;

/**
 * CreateAt : 2018/11/10
 * Describe :
 *
 * @author chendong
 */
public interface TopMoreRef {

    /**
     * {@inheritDoc}
     * 加载更多是否可用
     *
     * @param enable 是否支持加载更多
     */
    void setTopMoreEnable(boolean enable);

    /**
     * {@inheritDoc}
     * 结束加载才能开启下次加载
     */
    void finishTopMore();


    /**
     * {@inheritDoc}
     * 设置加载更多回调
     *
     * @param count    预加载个数
     * @param callback 回调
     */
    void setTopMoreListener(int count, AdapterCallback callback);

    /**
     * {@inheritDoc}
     * 设置加载更多回调
     *
     * @param callback 回调
     */
    void setTopMoreListener(AdapterCallback callback);

}
