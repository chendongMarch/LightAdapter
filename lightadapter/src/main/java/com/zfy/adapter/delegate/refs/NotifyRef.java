package com.zfy.adapter.delegate.refs;

import android.support.v7.widget.RecyclerView;

/**
 * CreateAt : 2018/11/10
 * Describe :
 *
 * @author chendong
 */
public interface NotifyRef {

    /**
     * {@inheritDoc}
     * 向主线程发送事件
     *
     * @param runnable 事件
     * @param delay    延时
     */
    void post(Runnable runnable, int delay);


    /**
     * {@inheritDoc}
     * 更新事件，全局更新
     *
     * @see RecyclerView.Adapter#notifyDataSetChanged()
     */
    void change();

    /**
     * {@inheritDoc}
     * 更新事件，更新一个
     *
     * @param position 更新的位置
     * @see RecyclerView.Adapter#notifyItemChanged(int)
     */
    void change(final int position);

    /**
     * {@inheritDoc}
     * 更新事件，局部更新
     *
     * @param positionStart 开始位置
     * @param itemCount     元素个数
     * @see RecyclerView.Adapter#notifyItemRangeChanged(int, int)
     */
    void change(final int positionStart, final int itemCount);

    /**
     * {@inheritDoc}
     * 更新事件，局部更新，支持 payload
     *
     * @param positionStart 开始位置
     * @param itemCount     元素个数
     * @param payloads      payload
     * @see RecyclerView.Adapter#notifyItemRangeChanged(int, int, Object)
     */
    void change(final int positionStart, final int itemCount, Object payloads);

    /**
     * {@inheritDoc}
     * 插入事件，在指定位置插入一个
     *
     * @param position 插入位置
     * @see RecyclerView.Adapter#notifyItemRangeInserted(int, int)
     */
    void insert(final int position);

    /**
     * {@inheritDoc}
     * 插入事件，范围插入
     *
     * @param positionStart 开始位置
     * @param itemCount     插入个数
     * @see RecyclerView.Adapter#notifyItemRangeInserted(int, int)
     */
    void insert(final int positionStart, final int itemCount);

    /**
     * {@inheritDoc}
     * 删除事件，删除一个
     *
     * @param position 开始删除的位置
     * @see RecyclerView.Adapter#notifyItemRangeRemoved(int, int)
     */
    void remove(final int position);

    /**
     * {@inheritDoc}
     * 删除事件，删除范围内元素
     *
     * @param positionStart 开始位置
     * @param itemCount     删除的元素个数
     * @see RecyclerView.Adapter#notifyItemRangeRemoved(int, int)
     */
    void remove(final int positionStart, final int itemCount);

    /**
     * {@inheritDoc}
     * 移动事件
     *
     * @param fromPosition 移动开始的位置
     * @param toPosition   移动目标位置
     * @see RecyclerView.Adapter#notifyItemMoved(int, int)
     */
    void move(final int fromPosition, final int toPosition);
}
