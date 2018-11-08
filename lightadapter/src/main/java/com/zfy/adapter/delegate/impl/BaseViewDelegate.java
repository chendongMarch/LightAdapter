package com.zfy.adapter.delegate.impl;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateAt : 2018/11/6
 * Describe :
 *
 * @author chendong
 */
public abstract class BaseViewDelegate extends BaseDelegate {

    protected List<Runnable> mPendingRunnableList = new ArrayList<>();

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (!mPendingRunnableList.isEmpty()) {
            for (Runnable pendingRunnable : mPendingRunnableList) {
                pendingRunnable.run();
            }
            mPendingRunnableList.clear();
        }
    }

    protected void postOnRecyclerViewAttach(Runnable runnable) {
        if (isAttached() && mView.getLayoutManager() != null) {
            runnable.run();
        } else {
            mPendingRunnableList.add(runnable);
        }
    }
}
