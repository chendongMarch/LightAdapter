package com.zfy.adapter.delegate.impl;

import android.os.Handler;
import android.os.Looper;

/**
 * CreateAt : 2018/10/30
 * Describe :
 *
 * @author chendong
 */
public class NotifyDelegate extends BaseDelegate {

    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public int getKey() {
        return NOTIFY;
    }


    private void notifyInUIThread(Runnable runnable) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            runnable.run();
        } else {
            mHandler.post(runnable);
        }
    }

    public final void change() {
        notifyInUIThread(() -> mAdapter.notifyDataSetChanged());
    }

    public final void change(final int position) {
        notifyInUIThread(() -> mAdapter.notifyItemChanged(position));
    }

    public final void change(final int positionStart, final int itemCount) {
        notifyInUIThread(() -> mAdapter.notifyItemRangeChanged(positionStart, itemCount));
    }

    public final void change(final int positionStart, final int itemCount, Object payloads) {
        notifyInUIThread(() -> mAdapter.notifyItemRangeChanged(positionStart, itemCount, payloads));
    }

    public final void insert(final int position) {
        notifyInUIThread(() -> mAdapter.notifyItemRangeInserted(position, 1));
    }

    public final void insert(final int positionStart, final int itemCount) {
        notifyInUIThread(() -> {
            mAdapter.notifyItemRangeInserted(positionStart, itemCount);
        });
    }

    public final void remove(final int position) {
        notifyInUIThread(() -> mAdapter.notifyItemRangeRemoved(position, 1));
    }

    public final void remove(final int positionStart, final int itemCount) {
        notifyInUIThread(() -> mAdapter.notifyItemRangeRemoved(positionStart, itemCount));
    }

    public final void move(final int fromPosition, final int toPosition) {
        notifyInUIThread(() -> mAdapter.notifyItemMoved(fromPosition, toPosition));
    }
}
