package com.zfy.adapter.delegate.impl;

import com.zfy.adapter.LightHolder;
import com.zfy.adapter.common.LightValues;
import com.zfy.adapter.model.ModelType;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateAt : 2018/11/1
 * Describe :
 *
 * @author chendong
 */
public class SelectorDelegate<D> extends BaseDelegate {

    public interface SelectorBinder<D> {
        void onBindSelectableViewHolder(LightHolder holder, int position, D data, boolean isSelect);
    }

    public interface OnSelectListener<D> {
        void onSelect(D data);
    }

    private SelectorBinder<D> mSelectorBinder;
    private int mSelectType = LightValues.SINGLE;
    private OnSelectListener<D> mOnSelectListener;
    private List<D> mResults = new ArrayList<>();


    public void setSelectType(int selectType) {
        mSelectType = selectType;
    }

    public void setSelectorBinder(SelectorBinder<D> selectorBinder) {
        mSelectorBinder = selectorBinder;
    }

    public void setOnSelectListener(OnSelectListener<D> onSelectListener) {
        mOnSelectListener = onSelectListener;
    }

    public List<D> getResults() {
        return mResults;
    }

    public D getResult(D defaultValue) {
        if(mResults.size() == 0){
            return defaultValue;
        }
        return mResults.get(0);
    }

    @Override
    public int getKey() {
        return SELECTOR;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean onBindViewHolder(LightHolder holder, int position) {
        if (mSelectorBinder == null) {
            return super.onBindViewHolder(holder, position);
        }
        int pos = mAdapter.toModelIndex(position);
        Object data = mAdapter.getItem(pos);
        if (data != null) {
            ModelType type = mAdapter.getType(data);
            if (type != null) {
                if (type.getType() == LightValues.TYPE_CONTENT || !type.isBuildInType()) {
                    mSelectorBinder.onBindSelectableViewHolder(holder, position, (D) data, isSelect((D) data));
                }
            }
        }
        return super.onBindViewHolder(holder, position);
    }


    public boolean isSelect(D object) {
        return mResults.contains(object);
    }


    // 单选时，选中一个，取消选中其他的
    private void releaseOthers(D selectable) {
        List datas = mAdapter.getDatas();
        for (Object data : datas) {
            if (data.equals(selectable)) {
                continue;
            }
            D d = (D) data;
            if (isSelect(d)) {
                releaseItem(d);
            }
        }
    }

    /**
     * 选中某一项
     *
     * @param selectable 继承 Selectable 接口
     */
    public void selectItem(D selectable) {
        if (mSelectType == LightValues.SINGLE) {
            releaseOthers(selectable);
        }
        if (isSelect(selectable)) {
            return;
        }
        mResults.add(selectable);
        if (mOnSelectListener != null) {
            mOnSelectListener.onSelect(selectable);
        }
        if (!isAttached()) {
            return;
        }
        int pos = mAdapter.getDatas().indexOf(selectable);
        int layoutIndex = mAdapter.toLayoutIndex(pos);
        LightHolder holder = (LightHolder) mAdapter.getRecyclerView().findViewHolderForLayoutPosition(layoutIndex);
        if (holder != null) {
            mSelectorBinder.onBindSelectableViewHolder(holder, pos, (D) selectable, true);
        } else {
            mAdapter.notifyItem().change(layoutIndex);
        }
    }

    /**
     * 释放某一项
     *
     * @param selectable 继承 Selectable 接口
     */
    public void releaseItem(D selectable) {
        if (!isSelect(selectable)) {
            return;
        }
        if (mResults.remove(selectable)) {
            if (!isAttached()) {
                return;
            }
            int pos = mAdapter.getDatas().indexOf(selectable);
            int layoutIndex = mAdapter.toLayoutIndex(pos);
            LightHolder holder = (LightHolder) mAdapter.getRecyclerView().findViewHolderForLayoutPosition(layoutIndex);
            if (holder != null) {
                mSelectorBinder.onBindSelectableViewHolder(holder, pos, selectable, false);
            } else {
                mAdapter.notifyItem().change(layoutIndex);
            }
        }
    }

    /**
     * 选中改为不选中，不选中改为选中
     *
     * @param selectable 继承 Selectable 接口
     */
    public void toggleItem(D selectable) {
        if (isSelect(selectable)) {
            releaseItem(selectable);
        } else {
            selectItem(selectable);
        }
    }
}
