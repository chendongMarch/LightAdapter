package com.zfy.adapter.module;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.LightHolder;


/**
 * CreateAt : 2016/20/8
 * Describe : module 基类
 *
 * @author chendong
 */
public abstract class BaseModule {

    public static final String TAG = BaseModule.class.getSimpleName();

    protected LightAdapter mAdapter;

    protected RecyclerView mAttachRecyclerView;

    protected boolean mIsAttach;

    public LightHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    public boolean onBindViewHolder(LightHolder holder, int position) {
        return false;
    }

    /**
     * RecyclerView.setAdapter()触发
     *
     * @param recyclerView rv
     */
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mAttachRecyclerView = recyclerView;
        mIsAttach = mAttachRecyclerView != null && mAdapter != null;
    }

    /**
     * Adapter.addModule()触发
     *
     * @param adapter adapter
     */
    public void onAttachAdapter(LightAdapter adapter) {
        mAdapter = adapter;
        mIsAttach = mAttachRecyclerView != null && mAdapter != null;
    }

}
