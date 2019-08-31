package com.zfy.adapter.x.list;

import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.RecyclerView;

/**
 * CreateAt : 2019/6/20
 * Describe :
 *
 * @author chendong
 */
public class AdapterUpdateCallback implements ListUpdateCallback {

    private RecyclerView.Adapter adapter;

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onInserted(int position, int count) {
        if (adapter == null) {
            return;
        }
        adapter.notifyItemRangeInserted(position, count);
    }

    @Override
    public void onRemoved(int position, int count) {
        if (adapter == null) {
            return;
        }
        adapter.notifyItemRangeRemoved(position, count);
    }

    @Override
    public void onMoved(int fromPosition, int toPosition) {
        if (adapter == null) {
            return;
        }
        adapter.notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onChanged(int position, int count, Object payload) {
        if (adapter == null) {
            return;
        }
        adapter.notifyItemRangeChanged(position, count, payload);
    }
}
