package com.zfy.adapter.list;

import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v7.recyclerview.extensions.AsyncDifferConfig;
import android.support.v7.recyclerview.extensions.AsyncListDiffer;
import android.support.v7.util.DiffUtil;

import com.zfy.adapter.LxModelList;
import com.zfy.adapter.data.Diffable;
import com.zfy.adapter.data.LxModel;

import java.util.List;

/**
 * CreateAt : 2018/11/2
 * Describe : 异步更新数据
 *
 * @author chendong
 */
public class LxModelAsyncDiffList extends LxModelList {

    private final AsyncListDiffer<LxModel> differ;

    public LxModelAsyncDiffList() {
        super();
        DiffUtil.ItemCallback<LxModel> itemCallback = new DiffUtil.ItemCallback<LxModel>() {
            @Override
            public boolean areItemsTheSame(LxModel oldItem, LxModel newItem) {
                return oldItem.areItemsTheSame(newItem);
            }

            @Override
            public boolean areContentsTheSame(LxModel oldItem, LxModel newItem) {
                return oldItem.areContentsTheSame(newItem);
            }

            @Override
            public Object getChangePayload(LxModel oldItem, LxModel newItem) {
                return oldItem.getChangePayload(newItem);
            }
        };
        AsyncDifferConfig<LxModel> config = new AsyncDifferConfig.Builder<>(itemCallback).build();
        differ = new AsyncListDiffer<>(updateCallback, config);
    }

    @MainThread
    public void update(@Nullable List<LxModel> newItems) {
        differ.submitList(newItems);
    }

    @Override
    public List<LxModel> list() {
        return differ.getCurrentList();
    }
}
