package com.march.lightadapter.module;

import android.support.v7.widget.RecyclerView;

import com.march.lightadapter.LightAdapter;
import com.march.lightadapter.helper.LightLogger;

import java.util.ArrayList;
import java.util.List;


/**
 * CreateAt : 2016/20/8
 * Describe : module 基类
 *
 * @author chendong
 */
abstract class AbstractModule<D> {

    public static final String TAG = AbstractModule.class.getSimpleName();

    private List<Integer> mIgnoreTypeList;
    LightAdapter<D> mAttachAdapter;
    RecyclerView    mAttachRecyclerView;


    public void ignoreType(int... types) {
        if (mIgnoreTypeList == null) {
            mIgnoreTypeList = new ArrayList<>();
        }
        mIgnoreTypeList.clear();
        for (int type : types) {
            mIgnoreTypeList.add(type);
        }
    }

    boolean isIgnoreThisType(int type) {
        return mIgnoreTypeList.contains(type);
    }

    /**
     * RecyclerView.setAdapter()触发
     *
     * @param recyclerView rv
     */
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.mAttachRecyclerView = recyclerView;
    }

    /**
     * Adapter.addModule()触发
     *
     * @param adapter adapter
     */
    public void onAttachAdapter(LightAdapter<D> adapter) {
        this.mAttachAdapter = adapter;
    }

    void post(Runnable runnable) {
        if (mAttachRecyclerView != null)
            mAttachRecyclerView.post(runnable);
    }

    boolean isSupportPos(Integer pos) {
        boolean isSupport = pos != null && mAttachAdapter != null && mAttachAdapter.getDatas() != null
                && pos >= 0 && pos < mAttachAdapter.getDatas().size();
        if (!isSupport) {
            LightLogger.e(TAG, "not Support Pos,check pos = " + pos + " , and data size = " + getDatas().size());
        }
        return isSupport;
    }

    boolean isAttachSuccess() {
        return mAttachRecyclerView != null && mAttachAdapter != null;
    }

    List<D> getDatas() {
        if (mAttachAdapter == null || mAttachAdapter.getDatas() == null) {
            throw new IllegalArgumentException("adapter data not set");
        }
        return mAttachAdapter.getDatas();
    }

}
