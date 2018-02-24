package com.march.lightadapter.module;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.march.lightadapter.LightAdapter;
import com.march.lightadapter.LightHolder;


/**
 * CreateAt : 2016/20/8
 * Describe : module 基类
 *
 * @author chendong
 */
public abstract class AbstractModule {

    public static final String TAG = AbstractModule.class.getSimpleName();

    protected LightAdapter mAttachAdapter;
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
        mIsAttach = mAttachRecyclerView != null && mAttachAdapter != null;
    }

    /**
     * Adapter.addModule()触发
     *
     * @param adapter adapter
     */
    public void onAttachAdapter(LightAdapter adapter) {
        mAttachAdapter = adapter;
        mIsAttach = mAttachRecyclerView != null && mAttachAdapter != null;
    }

}
