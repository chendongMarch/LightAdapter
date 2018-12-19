package com.zfy.adapter.delegate.refs;

import com.zfy.adapter.listener.AdapterCallback;

/**
 * CreateAt : 2018/11/10
 * Describe :
 *
 * @author chendong
 */
public interface LoadMoreRef {

    /**
     * {@inheritDoc}
     * 加载更多是否可用
     *
     * @param enable 是否支持加载更多
     */
    void setLoadMoreEnable(boolean enable);

    /**
     * {@inheritDoc}
     * 结束加载，才能开启下次加载
     */
    void finishLoadMore();

    /**
     * {@inheritDoc}
     * 设置加载更多监听
     *
     * @param count    预加载的个数
     * @param callback 回调
     */
    void setLoadMoreListener(int count, AdapterCallback callback);

    /**
     * {@inheritDoc}
     * 设置加载更多监听
     *
     * @param callback 回调
     */
    void setLoadMoreListener(AdapterCallback callback);
}
