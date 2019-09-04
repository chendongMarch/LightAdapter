package com.zfy.adapter.list;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.zfy.adapter.LxModelList;
import com.zfy.adapter.data.Diffable;
import com.zfy.adapter.data.LxModel;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateAt : 2018/11/1
 * Describe : 使用 DiffUtil 更新数据
 *
 * @author chendong
 */
public class LxModelDiffList extends LxModelList {

    private final Object LIST_LOCK = new Object();

    private final DiffUtilCallback<LxModel> diffCallback;
    private       boolean                   detectMoves;
    private       List<LxModel>             list;

    public LxModelDiffList() {
        this(new ArrayList<>());
    }

    public LxModelDiffList(List<LxModel> list) {
        super();
        this.list = list;
        diffCallback = new DiffUtilCallback<>();
    }

    public void setDetectMoves(boolean detectMoves) {
        this.detectMoves = detectMoves;
    }

    @Override
    public List<LxModel> list() {
        return list;
    }

    /**
     * 根据给出的新集合计算 DiffResult
     *
     * @param newItems 新的集合
     * @return 返回 DiffResult
     */
    public DiffUtil.DiffResult calculateDiff(final List<LxModel> newItems) {
        final ArrayList<LxModel> frozenList;
        synchronized (LIST_LOCK) {
            frozenList = new ArrayList<>(list);
        }
        return doCalculateDiff(frozenList, newItems);
    }

    @Override
    @MainThread
    public void update(@NonNull List<LxModel> newItems) {
        DiffUtil.DiffResult diffResult = doCalculateDiff(list, newItems);
        list = newItems;
        diffResult.dispatchUpdatesTo(updateCallback);
    }

    // 计算 DiffResult
    private DiffUtil.DiffResult doCalculateDiff(final List<LxModel> oldItems, final List<LxModel> newItems) {
        diffCallback.setItems(oldItems, newItems);
        return DiffUtil.calculateDiff(diffCallback, detectMoves);
    }


    static class DiffUtilCallback<LxModel extends Diffable<LxModel>> extends DiffUtil.Callback {

        private List<LxModel> oldItems;
        private List<LxModel> newItems;

        public void setItems(List<LxModel> oldItems, List<LxModel> newItems) {
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
            LxModel oldItem = oldItems.get(oldItemPosition);
            LxModel newItem = newItems.get(newItemPosition);
            return oldItem.areItemsTheSame(newItem);
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            LxModel oldItem = oldItems.get(oldItemPosition);
            LxModel newItem = newItems.get(newItemPosition);
            return oldItem.areContentsTheSame(newItem);
        }

        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            LxModel oldItem = oldItems.get(oldItemPosition);
            LxModel newItem = newItems.get(newItemPosition);
            return oldItem.getChangePayload(newItem);
        }
    }


}
