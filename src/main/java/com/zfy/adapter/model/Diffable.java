package com.zfy.adapter.model;

import android.os.Parcelable;

import java.util.Set;

/**
 * CreateAt : 2018/11/1
 * Describe : 使用 LightDiffList 更新数据时需要实现该接口
 *
 * @author chendong
 */
public interface Diffable<T> extends Parcelable {

    /**
     * 用来比较是否可以认为是同一个 item, 可以使用地址比较，或者使用 id 唯一标示
     *
     * @param newItem 新的元素
     * @return 是否为同一个 Item
     */
    default boolean areItemsTheSame(T newItem) {
        return this.equals(newItem);
    }

    /**
     * 用来比较两项内容是否相同
     * 只有在 areItemsTheSame 返回 true 时才会调用
     *
     * @param newItem 新的元素
     * @return 内容是否相同
     */
    boolean areContentsTheSame(T newItem);

    /**
     * 增量更新
     * 只有在 areItemsTheSame 返回 true 时才会调用，areContentsTheSame 返回 false 时调用
     *
     * @param newItem 新的元素
     * @return 需要更新的事件信息
     */
    default Set<String> getChangePayload(T newItem) {
        return null;
    }
}