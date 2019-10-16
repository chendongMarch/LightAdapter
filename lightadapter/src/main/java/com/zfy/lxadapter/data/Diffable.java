package com.zfy.lxadapter.data;

import android.support.v7.widget.RecyclerView;


import java.util.Set;

/**
 * CreateAt : 2018/11/1
 * Describe :
 * 该接口表明在 DiffUtil 中如何对数据进行对比，使用更新数据时需要实现该接口
 * @author chendong
 */
public interface Diffable<T> {


    /**
     * 用来比较是否可以认为是同一个 item, 可以使用地址比较，更推荐使用 id 唯一标示
     *
     * 当返回 true 的时候表示是相同的元素，不需要做任何更新
     * 当返回 false 的时候表示是一个完全的新元素，此时会调用 insert 和 remove 方法来达到数据更新的目的
     *
     * @see RecyclerView.Adapter#notifyItemInserted(int)
     * @see RecyclerView.Adapter#notifyItemRangeInserted(int, int)
     * @see RecyclerView.Adapter#notifyItemRemoved(int)
     * @see RecyclerView.Adapter#notifyItemRangeRemoved(int, int)
     *
     * @param newItem 新的元素
     * @return 是否为同一个 Item
     */
    default boolean areItemsTheSame(T newItem) {
        return this.equals(newItem);
    }

    /**
     * 用来比较两项内容是否相同，只有在 areItemsTheSame 返回 true 时才会调用
     *
     * 只有被认为是同一个元素时此方法会被调用
     *
     * 返回 true 表示内容完全相同不需要更新
     * 返回 false 表示虽然是同个元素但是内容改变了，此时会调用 changed 方法来更新数据
     *
     * @see RecyclerView.Adapter#notifyItemChanged(int)
     * @see RecyclerView.Adapter#notifyItemRangeChanged(int, int)
     * @see RecyclerView.Adapter#notifyItemRangeChanged(int, int, Object)
     * @param newItem 新的元素
     * @return 内容是否相同
     */
    default boolean areContentsTheSame(T newItem) {
        return false;
    }

    /**
     * 使用 payload(有效载荷) 增量更新
     * 这个是解决更新数据时调用 onBindViewHolder 方法导致所有的数据都被重新绑定的问题，人为的去比对数据差异，拿到差异的部分再去更新
     * 只有在 areItemsTheSame 返回 true 时才会调用，areContentsTheSame 返回 false 时调用
     *
     * 返回事件列表不为空，则会调用到 Adapter OnBindViewHolderUsePayload
     * 返回事件为空，会调用 Adapter OnBindViewHolder
     *
     * @param newItem 新的元素
     * @return 需要更新的事件信息，我们把比对的结果包装成一个 String 类型的信息，在 LightAdapter 绑定时再根据这个自定义信息选择性的更新绑定
     */
    default Set<String> getChangePayload(T newItem) {
        return null;
    }
}