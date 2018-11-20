package com.zfy.adapter.delegate.impl;

import android.view.ViewGroup;

import com.zfy.adapter.LightHolder;
import com.zfy.adapter.common.ItemType;
import com.zfy.adapter.common.LightUtils;
import com.zfy.adapter.common.LightValues;
import com.zfy.adapter.delegate.IDelegate;
import com.zfy.adapter.delegate.refs.EmptyViewRef;
import com.zfy.adapter.listener.BindCallback;
import com.zfy.adapter.model.EmptyState;
import com.zfy.adapter.model.LightView;

/**
 * CreateAt : 2018/11/5
 * Describe :
 *
 * 当 Empty 显示时，LoadMore 会自动停止
 *
 * @author chendong
 */
public class EmptyViewDelegate extends BaseViewDelegate implements EmptyViewRef {

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
        if (viewType == ItemType.TYPE_EMPTY) {
            mEmptyHolder = new LightHolder(mAdapter, viewType, mEmptyView);
            return mEmptyHolder;
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public boolean onBindViewHolder(LightHolder holder, int layoutIndex) {
        if (mAdapter.getItemViewType(layoutIndex) == ItemType.TYPE_EMPTY) {
            return true;
        }
        return super.onBindViewHolder(holder, layoutIndex);
    }


    @Override
    public int getItemViewType(int position) {
        int aboveItemCount = mAdapter.getDelegateRegistry().getAboveItemCount(LightValues.FLOW_LEVEL_EMPTY);
        if (isEmptyEnable() && position == aboveItemCount) {
            return ItemType.TYPE_EMPTY;
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
            mAdapter.notifyItem().insert(mAdapter.getDelegateRegistry().getAboveItemCount(LightValues.FLOW_LEVEL_EMPTY));
        } else {
            mAdapter.notifyItem().remove(mAdapter.getDelegateRegistry().getAboveItemCount(LightValues.FLOW_LEVEL_EMPTY));
        }
    }

    @Override
    public void setEmptyView(LightView lightView, BindCallback<EmptyState> callback) {
        mBindCallback = callback;
        mEmptyState = EmptyState.from(EmptyState.NONE);
        postOnRecyclerViewAttach(() -> {
            lightView.inflate(mAdapter.getContext());
            if (mEmptyView == null) {
                mEmptyView = LightUtils.createMatchParentFrameContainer(mAdapter.getContext());
            }
            mEmptyView.addView(lightView.view);
            mEmptyHolder = new LightHolder(mAdapter, ItemType.TYPE_EMPTY, mEmptyView);
            setEmptyState(EmptyState.NONE);
        });
    }

    @Override
    public boolean isEmptyEnable() {
        return mEmptyEnable && mEmptyView != null && mEmptyState.state != EmptyState.NONE;
    }

    @Override
    public void setEmptyState(int state) {
        mEmptyState.state = state;
        if (mBindCallback != null && mEmptyHolder != null) {
            mBindCallback.bind(mEmptyHolder, null, mEmptyState);
        }
        displayEmptyView(mEmptyState.state != EmptyState.NONE);

        if (mEmptyState.state == EmptyState.NONE) {
            if (mAdapter.getDelegateRegistry().isLoaded(IDelegate.LOADING)) {
                ((LoadingViewDelegate) mAdapter.loadingView()).setLoadingEnable(true);
            }
            if (mAdapter.getDelegateRegistry().isLoaded(IDelegate.LOAD_MORE)) {
                ((LoadMoreDelegate) mAdapter.loadMore()).setLoadMoreEnableFlagInternal(true);
            }
        } else {
            if (mAdapter.getDelegateRegistry().isLoaded(IDelegate.LOADING)) {
                ((LoadingViewDelegate) mAdapter.loadingView()).setLoadingEnable(false);
            }
            if (mAdapter.getDelegateRegistry().isLoaded(IDelegate.LOAD_MORE)) {
                ((LoadMoreDelegate) mAdapter.loadMore()).setLoadMoreEnableFlagInternal(false);
            }
        }
    }
}
