package com.zfy.adapter.delegate.impl;

import com.zfy.adapter.LightHolder;
import com.zfy.adapter.assistant.SlidingSelectLayout;
import com.zfy.adapter.common.ItemType;
import com.zfy.adapter.common.LightUtils;
import com.zfy.adapter.common.LightValues;
import com.zfy.adapter.delegate.refs.SelectorRef;
import com.zfy.adapter.listener.BindCallback;
import com.zfy.adapter.model.Extra;
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

    private BindCallback<D> mBindCallback;
    private int mSelectType = LightValues.SINGLE;
    private OnSelectListener<D> mOnSelectListener;
    private List<D> mResults = new ArrayList<>();

    @Override
    public int getKey() {
        return SELECTOR;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean onBindViewHolder(LightHolder holder, int layoutIndex) {
        if (mBindCallback == null) {
            return super.onBindViewHolder(holder, layoutIndex);
        }
        Extra extra = mAdapter.obtainExtraByLayoutIndex(layoutIndex);
        Object data = mAdapter.getItem(extra.modelIndex);
        if (data != null) {
            ModelType modelType = mAdapter.getModelType(data);
            if (modelType != null) {
                if (modelType.type == ItemType.TYPE_CONTENT || !LightUtils.isBuildInType(modelType.type)) {
                    D d = (D) data;
                    extra.selected = isSelect(d);
                    mBindCallback.bind(holder, d, extra);
                }
            }
        }
        return super.onBindViewHolder(holder, layoutIndex);
    }

    @SuppressWarnings("unchecked")
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
    public void setSingleSelector(BindCallback<D> bindCallback) {
        mSelectType = LightValues.SINGLE;
        mBindCallback = bindCallback;
    }

    @Override
    public void setMultiSelector(BindCallback<D> bindCallback) {
        mSelectType = LightValues.MULTI;
        mBindCallback = bindCallback;
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
        if (mOnSelectListener != null && !mOnSelectListener.onSelect(data, true)) {
            return;
        }
        mResults.add(data);
        if (!isAttached()) {
            return;
        }
        int modelIndex = mAdapter.getDatas().indexOf(data);
        Extra extra = mAdapter.obtainExtraByModelIndex(modelIndex);
        LightHolder holder = (LightHolder) mAdapter.getView().findViewHolderForLayoutPosition(extra.layoutIndex);
        extra.selected = true;
        if (holder != null) {
            mBindCallback.bind(holder, data, extra);
        } else {
            mAdapter.notifyItem().change(extra.layoutIndex);
        }
    }

    @Override
    public void releaseItem(D data) {
        if (!isSelect(data)) {
            return;
        }
        if (!mOnSelectListener.onSelect(data, false)) {
            return;
        }
        if (mResults.remove(data)) {
            if (!isAttached()) {
                return;
            }
            int modelIndex = mAdapter.getDatas().indexOf(data);
            Extra extra = mAdapter.obtainExtraByModelIndex(modelIndex);
            LightHolder holder = (LightHolder) mAdapter.getView().findViewHolderForLayoutPosition(extra.layoutIndex);
            extra.selected = false;
            if (holder != null) {
                mBindCallback.bind(holder, data, extra);
            } else {
                mAdapter.notifyItem().change(extra.layoutIndex);
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
