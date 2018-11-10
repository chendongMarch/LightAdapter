package com.zfy.adapter.delegate.impl;

import com.zfy.adapter.LightHolder;
import com.zfy.adapter.assistant.SlidingSelectLayout;
import com.zfy.adapter.common.LightValues;
import com.zfy.adapter.delegate.refs.SelectorRef;
import com.zfy.adapter.model.ModelType;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateAt : 2018/11/1
 * Describe :
 *
 * @author chendong
 */
public class SelectorDelegate<D> extends BaseDelegate implements SelectorRef<D> {

    private SelectorBinder<D>   mSelectorBinder;
    private int mSelectType = LightValues.SINGLE;
    private OnSelectListener<D> mOnSelectListener;
    private List<D> mResults = new ArrayList<>();

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
            ModelType type = mAdapter.getModelType(data);
            if (type != null) {
                if (type.getType() == LightValues.TYPE_CONTENT || !type.isBuildInType()) {
                    mSelectorBinder.onBindSelectableViewHolder(holder, position, (D) data, isSelect((D) data));
                }
            }
        }
        return super.onBindViewHolder(holder, position);
    }

    public void setSlidingSelectLayout(SlidingSelectLayout slidingSelectLayout) {
        slidingSelectLayout.setOnSlidingSelectListener(data -> {
            if (mSelectType == LightValues.SINGLE) {
                selectItem((D) data);
            } else {
                toggleItem((D) data);
            }
        });
    }

    @Override
    public void setSelectType(int selectType) {
        mSelectType = selectType;
    }

    @Override
    public void setSelectorBinder(SelectorBinder<D> selectorBinder) {
        mSelectorBinder = selectorBinder;
    }

    @Override
    public void setOnSelectListener(OnSelectListener<D> onSelectListener) {
        mOnSelectListener = onSelectListener;
    }

    @Override
    public List<D> getResults() {
        return mResults;
    }

    @Override
    public D getResult(D defaultValue) {
        if (mResults.size() == 0) {
            return defaultValue;
        }
        return mResults.get(0);
    }


    @Override
    public boolean isSelect(D data) {
        return mResults.contains(data);
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

    @Override
    public void selectItem(D data) {
        if (mSelectType == LightValues.SINGLE) {
            releaseOthers(data);
        }
        if (isSelect(data)) {
            return;
        }
        if (mOnSelectListener != null && !mOnSelectListener.onSelect(data)) {
            return;
        }
        mResults.add(data);
        if (!isAttached()) {
            return;
        }
        int pos = mAdapter.getDatas().indexOf(data);
        int layoutIndex = mAdapter.toLayoutIndex(pos);
        LightHolder holder = (LightHolder) mAdapter.getRecyclerView().findViewHolderForLayoutPosition(layoutIndex);
        if (holder != null) {
            mSelectorBinder.onBindSelectableViewHolder(holder, pos, (D) data, true);
        } else {
            mAdapter.notifyItem().change(layoutIndex);
        }
    }

    @Override
    public void releaseItem(D data) {
        if (!isSelect(data)) {
            return;
        }
        if (mResults.remove(data)) {
            if (!isAttached()) {
                return;
            }
            int pos = mAdapter.getDatas().indexOf(data);
            int layoutIndex = mAdapter.toLayoutIndex(pos);
            LightHolder holder = (LightHolder) mAdapter.getRecyclerView().findViewHolderForLayoutPosition(layoutIndex);
            if (holder != null) {
                mSelectorBinder.onBindSelectableViewHolder(holder, pos, data, false);
            } else {
                mAdapter.notifyItem().change(layoutIndex);
            }
        }
    }

    @Override
    public void toggleItem(D data) {
        if (isSelect(data)) {
            releaseItem(data);
        } else {
            selectItem(data);
        }
    }
}
