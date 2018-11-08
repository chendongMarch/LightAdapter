package com.zfy.adapter.collections;

import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.zfy.adapter.able.Diffable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CreateAt : 2018/11/1
 * Describe : 使用 DiffUtil 更新数据
 *
 * @author chendong
 */
public class LightDiffList<T extends Diffable<T>> extends AbstractLightList<T> {

    private final Object LIST_LOCK = new Object();
    private final DiffUtilCallback<T> mDiffUtilCallback;
    private final boolean mDetectMoves;
    private List<T> mList = Collections.emptyList();


    public LightDiffList() {
        this(false);
    }

    public LightDiffList(boolean detectMoves) {
        mDetectMoves = detectMoves;
        mCallback = new LightAdapterUpdateCallback();
        mDiffUtilCallback = new DiffUtilCallback<>();
    }

    @Override
    public List<T> getList() {
        return mList;
    }

    /**
     * 根据给出的新集合计算 DiffResult
     *
     * @param newItems 新的集合
     * @return 返回 DiffResult
     */
    public DiffUtil.DiffResult calculateDiff(final List<T> newItems) {
        final ArrayList<T> frozenList;
        synchronized (LIST_LOCK) {
            frozenList = new ArrayList<>(mList);
        }
        return doCalculateDiff(frozenList, newItems);
    }

    @Override
    @MainThread
    public void update(List<T> newItems) {
        DiffUtil.DiffResult diffResult = doCalculateDiff(mList, newItems);
        mList = newItems;
        diffResult.dispatchUpdatesTo(mCallback);
    }

    // 计算 DiffResult
    private DiffUtil.DiffResult doCalculateDiff(final List<T> oldItems, final List<T> newItems) {
        mDiffUtilCallback.setItems(oldItems, newItems);
        return DiffUtil.calculateDiff(mDiffUtilCallback, mDetectMoves);
    }


    static class DiffUtilCallback<T extends Diffable<T>> extends DiffUtil.Callback {

        private List<T> oldItems;
        private List<T> newItems;

        public void setItems(List<T> oldItems, List<T> newItems) {
            this.oldItems = oldItems;
            this.newItems = newItems;
        }

        @Override
        public int getOldListSize() {
            return oldItems.size();
        }

        @Override
        public int getNewListSize() {
            return newItems != null ? newItems.size() : 0;
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            T oldItem = oldItems.get(oldItemPosition);
            T newItem = newItems.get(newItemPosition);
            return oldItem.areItemsTheSame(newItem);
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            T oldItem = oldItems.get(oldItemPosition);
            T newItem = newItems.get(newItemPosition);
            return oldItem.areContentsTheSame(newItem);
        }

        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            T oldItem = oldItems.get(oldItemPosition);
            T newItem = newItems.get(newItemPosition);
            return oldItem.getChangePayload(newItem);
        }
    }


}
