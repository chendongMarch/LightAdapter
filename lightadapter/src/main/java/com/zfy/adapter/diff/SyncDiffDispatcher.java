package com.zfy.adapter.diff;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;

import com.zfy.adapter.data.Diffable;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateAt : 2019-09-08
 * Describe :
 *
 * @author chendong
 */
public class SyncDiffDispatcher<E extends Diffable<E>> implements IDiffDispatcher<E> {

    private final DiffUtilCallback<E> diffCallback;
    private       boolean             detectMoves;
    private       List<E>             list;
    private       ListUpdateCallback  callback;

    public SyncDiffDispatcher(ListUpdateCallback callback) {
        this.diffCallback = new DiffUtilCallback<>();
        this.list = new ArrayList<>();
        this.callback = callback;
    }


    @Override
    @MainThread
    public void update(@NonNull List<E> newItems) {
        DiffUtil.DiffResult diffResult = doCalculateDiff(list, newItems);
        list = newItems;
        diffResult.dispatchUpdatesTo(callback);
    }

    @Override
    public List<E> list() {
        return list;
    }

    public void setDetectMoves(boolean detectMoves) {
        this.detectMoves = detectMoves;
    }

    // 计算 DiffResult
    private DiffUtil.DiffResult doCalculateDiff(final List<E> oldItems, final List<E> newItems) {
        diffCallback.setItems(oldItems, newItems);
        return DiffUtil.calculateDiff(diffCallback, detectMoves);
    }


    static class DiffUtilCallback<E extends Diffable<E>> extends DiffUtil.Callback {

        private List<E> oldItems;
        private List<E> newItems;

        void setItems(List<E> oldItems, List<E> newItems) {
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
            E oldItem = oldItems.get(oldItemPosition);
            E newItem = newItems.get(newItemPosition);
            return oldItem.areItemsTheSame(newItem);
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            E oldItem = oldItems.get(oldItemPosition);
            E newItem = newItems.get(newItemPosition);
            return oldItem.areContentsTheSame(newItem);
        }

        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            E oldItem = oldItems.get(oldItemPosition);
            E newItem = newItems.get(newItemPosition);
            return oldItem.getChangePayload(newItem);
        }
    }

}
