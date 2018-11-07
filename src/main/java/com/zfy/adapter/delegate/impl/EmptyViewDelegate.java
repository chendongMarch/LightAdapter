package com.zfy.adapter.delegate.impl;

import android.view.ViewGroup;

import com.zfy.adapter.LightHolder;
import com.zfy.adapter.common.LightUtils;
import com.zfy.adapter.common.LightValues;
import com.zfy.adapter.listener.BindCallback;
import com.zfy.adapter.model.EmptyState;
import com.zfy.adapter.model.LightView;

/**
 * CreateAt : 2018/11/5
 * Describe :
 *
 * @author chendong
 */
public class EmptyViewDelegate extends BaseViewDelegate {

    private EmptyState mEmptyState; // 空白状态状态
    private ViewGroup mEmptyView; // 容器
    private LightHolder mEmptyHolder; // 当前 holder
    private BindCallback<EmptyState> mBindCallback; // 绑定回调
    private boolean mEmptyEnable; // 是否支持 emptyView

    @Override
    public int getKey() {
        return EMPTY;
    }

    @Override
    public LightHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LightValues.TYPE_EMPTY) {
            mEmptyHolder = new LightHolder(mAdapter, viewType, mEmptyView);
            return mEmptyHolder;
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public boolean onBindViewHolder(LightHolder holder, int position) {
        if (mAdapter.getItemViewType(position) == LightValues.TYPE_EMPTY) {
            return true;
        }
        return super.onBindViewHolder(holder, position);
    }


    @Override
    public int getItemViewType(int position) {
        int aboveItemCount = mAdapter.getDelegateRegistry().getAboveItemCount(LightValues.FLOW_LEVEL_EMPTY);
        if (isEmptyEnable() && position == aboveItemCount) {
            return LightValues.TYPE_EMPTY;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return isEmptyEnable() ? 1 : 0;
    }

    @Override
    public int getAboveItemCount(int level) {
        if (isEmptyEnable() && level > LightValues.FLOW_LEVEL_EMPTY) {
            return 1;
        }
        return super.getAboveItemCount(level);
    }

    private void displayEmptyView(boolean emptyEnable) {
        if (mEmptyEnable == emptyEnable) {
            return;
        }
        mEmptyEnable = emptyEnable;
        if (mEmptyEnable) {
            mAdapter.notifyItem().insert(mAdapter.getDelegateRegistry().getAboveItemCount(LightValues.FLOW_LEVEL_FOOTER));
        } else {
            mAdapter.notifyItem().remove(mAdapter.getDelegateRegistry().getAboveItemCount(LightValues.FLOW_LEVEL_FOOTER));
        }
    }

    /**
     * 设置 EmptyView
     *
     * @param lightView LightView
     * @param callback  绑定回调
     */
    public void setEmptyView(LightView lightView, BindCallback<EmptyState> callback) {
        mBindCallback = callback;
        mEmptyState = EmptyState.from(EmptyState.NONE);
        postOnRecyclerViewAttach(() -> {
            lightView.inflate(mAdapter.getContext());
            if (mEmptyView == null) {
                mEmptyView = LightUtils.createMatchParentFrameContainer(mAdapter.getContext());
            }
            mEmptyView.addView(lightView.view);
            mEmptyHolder = new LightHolder(mAdapter, LightValues.TYPE_EMPTY, mEmptyView);
            setEmptyState(EmptyState.NONE);
        });
    }

    /**
     * @return emptyView 功能是否可用
     */
    public boolean isEmptyEnable() {
        return mEmptyEnable && mEmptyView != null && mEmptyState.state != EmptyState.NONE;
    }

    /**
     * 设置 Empty 状态
     *
     * @param state 状态
     * @see EmptyState#NONE
     * @see EmptyState#SUCCESS
     * @see EmptyState#ERROR
     * @see EmptyState#NO_DATA
     */
    public void setEmptyState(int state) {
        mEmptyState.state = state;
        if (mBindCallback != null && mEmptyHolder != null) {
            mBindCallback.bind(mEmptyHolder, LightValues.NONE, mEmptyState);
        }
        displayEmptyView(mEmptyState.state != EmptyState.NONE);
    }
}
