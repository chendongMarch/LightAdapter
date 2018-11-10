package com.zfy.adapter.delegate.impl;

import android.os.Handler;
import android.os.Looper;

import com.zfy.adapter.delegate.refs.NotifyRef;

/**
 * CreateAt : 2018/10/30
 * Describe :
 *
 * @author chendong
 */
public class NotifyDelegate extends BaseDelegate implements NotifyRef {

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private void notifyInUIThread(Runnable runnable) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            runnable.run();
        } else {
            mHandler.post(runnable);
        }
    }
    @Override
    public int getKey() {
        return NOTIFY;
    }

    @Override
    public void post(Runnable runnable, int delay) {
        mHandler.postDelayed(runnable,delay);
    }

    @Override
    public final void change() {
        notifyInUIThread(() -> mAdapter.notifyDataSetChanged());
    }

    @Override
    public final void change(final int position) {
        notifyInUIThread(() -> mAdapter.notifyItemChanged(position));
    }

    @Override
    public final void change(final int positionStart, final int itemCount) {
        notifyInUIThread(() -> mAdapter.notifyItemRangeChanged(positionStart, itemCount));
    }

    @Override
    public final void change(final int positionStart, final int itemCount, Object payloads) {
        notifyInUIThread(() -> mAdapter.notifyItemRangeChanged(positionStart, itemCount, payloads));
    }

    @Override
    public final void insert(final int position) {
        notifyInUIThread(() -> mAdapter.notifyItemRangeInserted(position, 1));
    }

    @Override
    public final void insert(final int positionStart, final int itemCount) {
        notifyInUIThread(() -> {
            mAdapter.notifyItemRangeInserted(positionStart, itemCount);
        });
    }

    @Override
    public final void remove(final int position) {
        notifyInUIThread(() -> mAdapter.notifyItemRangeRemoved(position, 1));
    }

    @Override
    public final void remove(final int positionStart, final int itemCount) {
        notifyInUIThread(() -> mAdapter.notifyItemRangeRemoved(positionStart, itemCount));
    }

    @Override
    public final void move(final int fromPosition, final int toPosition) {
        notifyInUIThread(() -> mAdapter.notifyItemMoved(fromPosition, toPosition));
    }
}
