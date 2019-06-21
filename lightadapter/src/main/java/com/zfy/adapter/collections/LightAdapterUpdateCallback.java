package com.zfy.adapter.collections;

import android.support.v7.util.ListUpdateCallback;

import com.zfy.adapter.LightAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * CreateAt : 2019/6/20
 * Describe :
 *
 * @author chendong
 */
public class LightAdapterUpdateCallback implements ListUpdateCallback {

    private List<WeakReference<LightAdapter>> mAdapterRefs;

    LightAdapterUpdateCallback() {
        mAdapterRefs = new ArrayList<>();
    }

    public void register(LightAdapter adapter) {
        unregister(adapter);
        mAdapterRefs.add(new WeakReference<>(adapter));
    }

    public void unregister(LightAdapter adapter) {
        LightAdapter item;
        Iterator<WeakReference<LightAdapter>> iterator = mAdapterRefs.iterator();
        while (iterator.hasNext()) {
            item = iterator.next().get();
            if (item == null || item.equals(adapter)) {
                iterator.remove();
            }
        }
    }

    private interface OnUpdateCallback {
        void update(LightAdapter adapter);
    }

    private void forEach(OnUpdateCallback callback) {
        LightAdapter adapter;
        Iterator<WeakReference<LightAdapter>> iterator = mAdapterRefs.iterator();
        while (iterator.hasNext()) {
            adapter = iterator.next().get();
            if (adapter != null) {
                callback.update(adapter);
            } else {
                iterator.remove();
            }
        }
    }

    @Override
    public void onInserted(int position, int count) {
        forEach(adapter -> adapter.notifyItemRangeInserted(adapter.toLayoutIndex(position), count));
    }

    @Override
    public void onRemoved(int position, int count) {
        forEach(adapter -> adapter.notifyItemRangeRemoved(adapter.toLayoutIndex(position), count));
    }

    @Override
    public void onMoved(int fromPosition, int toPosition) {
        forEach(adapter -> adapter.notifyItemMoved(adapter.toLayoutIndex(fromPosition), adapter.toLayoutIndex(toPosition)));
    }

    @Override
    public void onChanged(int position, int count, Object payload) {
        forEach(adapter -> adapter.notifyItemRangeChanged(adapter.toLayoutIndex(position), count, payload));
    }
}
