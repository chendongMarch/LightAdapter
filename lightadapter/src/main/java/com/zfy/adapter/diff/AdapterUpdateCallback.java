package com.zfy.adapter.diff;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.RecyclerView;

/**
 * CreateAt : 2019/6/20
 * Describe :
 *
 * @author chendong
 */
public class AdapterUpdateCallback implements ListUpdateCallback {

    private static Handler handler = new Handler(Looper.getMainLooper());

    private RecyclerView.Adapter adapter;

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onInserted(int position, int count) {
        if (adapter == null) {
            return;
        }
        if (Looper.getMainLooper() == Looper.myLooper()) {
            adapter.notifyItemRangeInserted(position, count);
        } else {
            handler.post(() -> adapter.notifyItemRangeInserted(position, count));
        }
    }

    @Override
    public void onRemoved(int position, int count) {
        if (adapter == null) {
            return;
        }
        if (Looper.getMainLooper() == Looper.myLooper()) {
            adapter.notifyItemRangeRemoved(position, count);
        } else {
            handler.post(() -> adapter.notifyItemRangeRemoved(position, count));
        }
    }

    @Override
    public void onMoved(int fromPosition, int toPosition) {
        if (adapter == null) {
            return;
        }
        if (Looper.getMainLooper() == Looper.myLooper()) {
            adapter.notifyItemMoved(fromPosition, toPosition);
        } else {
            handler.post(() -> adapter.notifyItemMoved(fromPosition, toPosition));
        }
    }

    @Override
    public void onChanged(int position, int count, Object payload) {
        if (adapter == null) {
            return;
        }
        if (Looper.getMainLooper() == Looper.myLooper()) {
            adapter.notifyItemRangeChanged(position, count, payload);
        } else {
            handler.post(() -> adapter.notifyItemRangeChanged(position, count, payload));
        }
    }
}
