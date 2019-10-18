package com.zfy.lxadapter.diff;

import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v7.recyclerview.extensions.AsyncDifferConfig;
import android.support.v7.recyclerview.extensions.AsyncListDiffer;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;

import com.zfy.lxadapter.data.Diffable;

import java.util.List;

/**
 * CreateAt : 2018/11/2
 * Describe : 异步更新数据
 *
 * @author chendong
 */
public class AsyncDiffDispatcher<E extends Diffable<E>> implements IDiffDispatcher<E> {

    private final AsyncListDiffer<E> differ;

    public AsyncDiffDispatcher(ListUpdateCallback callback) {
        DiffUtil.ItemCallback<E> itemCallback = new DiffUtil.ItemCallback<E>() {
            @Override
            public boolean areItemsTheSame(E oldItem, E newItem) {
                return oldItem.areItemsTheSame(newItem);
            }

            @Override
            public boolean areContentsTheSame(E oldItem, E newItem) {
                return oldItem.areContentsTheSame(newItem);
            }

            @Override
            public Object getChangePayload(E oldItem, E newItem) {
                return oldItem.getChangePayload(newItem);
            }
        };
        AsyncDifferConfig<E> config = new AsyncDifferConfig.Builder<>(itemCallback).build();
        differ = new AsyncListDiffer<>(callback, config);
    }

    @MainThread
    @Override
    public void update(@Nullable List<E> newItems) {
        differ.submitList(newItems);
    }


    @Override
    public List<E> list() {
        return differ.getCurrentList();
    }
}
