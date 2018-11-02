package com.zfy.adapter.delegate.impl;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.LightHolder;
import com.zfy.adapter.common.Values;
import com.zfy.adapter.delegate.IDelegate;

/**
 * CreateAt : 2018/10/28
 * Describe : 委托基类
 *
 * @author chendong
 */
public abstract class BaseDelegate implements IDelegate {

    protected LightAdapter mAdapter;
    protected RecyclerView mView;

    protected boolean isAttached() {
        return mAdapter != null && mView != null;
    }

    @Override
    public LightHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull LightHolder holder) {

    }

    @Override
    public boolean onBindViewHolder(LightHolder holder, int position) {
        return false;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mView = recyclerView;
    }

    @Override
    public void onAttachAdapter(LightAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return Values.NONE;
    }
}
