package com.zfy.adapter.list;

import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v7.recyclerview.extensions.AsyncDifferConfig;
import android.support.v7.recyclerview.extensions.AsyncListDiffer;
import android.support.v7.util.DiffUtil;

import com.zfy.adapter.data.Diffable;

import java.util.List;

/**
 * CreateAt : 2018/11/2
 * Describe : 异步更新数据
 *
 * @author chendong
 */
public class LxAsyncDiffList<T extends Diffable<T>> extends LxList<T> {

    private final AsyncListDiffer<T> differ;

    public LxAsyncDiffList() {
        super();
        DiffUtil.ItemCallback<T> itemCallback = new DiffUtil.ItemCallback<T>() {
            @Override
            public boolean areItemsTheSame(T oldItem, T newItem) {
                return oldItem.areItemsTheSame(newItem);
            }

            @Override
            public boolean areContentsTheSame(T oldItem, T newItem) {
                return oldItem.areContentsTheSame(newItem);
            }

            @Override
            public Object getChangePayload(T oldItem, T newItem) {
                return oldItem.getChangePayload(newItem);
            }
        };
        AsyncDifferConfig<T> config = new AsyncDifferConfig.Builder<>(itemCallback).build();
        differ = new AsyncListDiffer<>(updateCallback, config);
    }

    @MainThread
    public void update(@Nullable List<T> newItems) {
        differ.submitList(newItems);
    }

    @Override
    public List<T> list() {
        return differ.getCurrentList();
    }
}
