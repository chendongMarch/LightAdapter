package com.zfy.adapter.delegate.impl;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zfy.adapter.LightHolder;
import com.zfy.adapter.able.Selectable;
import com.zfy.adapter.assistant.SlidingSelectLayout;
import com.zfy.adapter.common.ItemType;
import com.zfy.adapter.common.LightValues;
import com.zfy.adapter.delegate.refs.SelectorRef;
import com.zfy.adapter.listener.BindCallback;
import com.zfy.adapter.model.ModelType;
import com.zfy.adapter.model.Position;

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
        Position position = mAdapter.obtainPositionByLayoutIndex(layoutIndex);
        Object data = mAdapter.getItem(position.modelIndex);
        if (data != null) {
            ModelType type = mAdapter.getModelType(data);
            if (type != null) {
                if (type.getType() == ItemType.TYPE_CONTENT || !type.isBuildInType()) {
                    mBindCallback.bind(holder, position, (D) data);
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
        if (mOnSelectListener != null && !mOnSelectListener.onSelect(data)) {
            return;
        }
        mResults.add(data);
        if (data instanceof Selectable) {
            ((Selectable) data).setSelected(true);
        }
        if (!isAttached()) {
            return;
        }
        int modelIndex = mAdapter.getDatas().indexOf(data);
        Position position = mAdapter.obtainPositionByModelIndex(modelIndex);
        LightHolder holder = (LightHolder) mAdapter.getRecyclerView().findViewHolderForLayoutPosition(position.layoutIndex);
        if (holder != null) {
            mBindCallback.bind(holder, position, data);
        } else {
            mAdapter.notifyItem().change(position.layoutIndex);
        }
    }

    @Override
    public void releaseItem(D data) {
        if (!isSelect(data)) {
            return;
        }
        if (mResults.remove(data)) {
            if (data instanceof Selectable) {
                ((Selectable) data).setSelected(false);
            }
            if (!isAttached()) {
                return;
            }

            int modelIndex = mAdapter.getDatas().indexOf(data);
            Position position = mAdapter.obtainPositionByModelIndex(modelIndex);
            LightHolder holder = (LightHolder) mAdapter.getRecyclerView().findViewHolderForLayoutPosition(position.layoutIndex);
            if (holder != null) {
                mBindCallback.bind(holder, position, data);
            } else {
                mAdapter.notifyItem().change(position.layoutIndex);
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
