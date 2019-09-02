package com.zfy.adapter.x.list;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.zfy.adapter.data.Diffable;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateAt : 2018/11/1
 * Describe : 使用 DiffUtil 更新数据
 *
 * @author chendong
 */
public class LxDiffList<T extends Diffable<T>> extends LxList<T> {

    private final Object LIST_LOCK = new Object();

    private final DiffUtilCallback<T> diffCallback;
    private       boolean             detectMoves;
    private       List<T>             list;

    public LxDiffList() {
        this(new ArrayList<>());
    }

    public LxDiffList(List<T> list) {
        super();
        this.list = list;
        diffCallback = new DiffUtilCallback<>();
    }

    public void setDetectMoves(boolean detectMoves) {
        this.detectMoves = detectMoves;
    }

    @Override
    public List<T> list() {
        return list;
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
            frozenList = new ArrayList<>(list);
        }
        return doCalculateDiff(frozenList, newItems);
    }

    @Override
    @MainThread
    public void update(@NonNull List<T> newItems) {
        DiffUtil.DiffResult diffResult = doCalculateDiff(list, newItems);
        list = newItems;
        diffResult.dispatchUpdatesTo(updateCallback);
    }

    // 计算 DiffResult
    private DiffUtil.DiffResult doCalculateDiff(final List<T> oldItems, final List<T> newItems) {
        diffCallback.setItems(oldItems, newItems);
        return DiffUtil.calculateDiff(diffCallback, detectMoves);
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
