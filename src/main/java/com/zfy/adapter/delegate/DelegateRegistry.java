package com.zfy.adapter.delegate;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.LightHolder;
import com.zfy.adapter.common.LightValues;
import com.zfy.adapter.delegate.impl.BaseDelegate;
import com.zfy.adapter.delegate.impl.HFDelegate;
import com.zfy.adapter.delegate.impl.LoadMoreDelegate;
import com.zfy.adapter.delegate.impl.NotifyDelegate;
import com.zfy.adapter.delegate.impl.SelectorDelegate;
import com.zfy.adapter.delegate.impl.SpanDelegate;
import com.zfy.adapter.delegate.impl.TopMoreDelegate;

/**
 * CreateAt : 2018/10/28
 * Describe : 委托功能注册管理类
 *
 * @author chendong
 */
public class DelegateRegistry extends BaseDelegate {

    private SparseArray<IDelegate> mDelegates;

    public DelegateRegistry() {
        mDelegates = new SparseArray<>();
        register(new HFDelegate());
        register(new SpanDelegate());
        register(new TopMoreDelegate());
        register(new LoadMoreDelegate());
        register(new NotifyDelegate());
        register(new SelectorDelegate());
    }

    public void register(IDelegate delegate) {
        if (mAdapter != null) {
            delegate.onAttachAdapter(mAdapter);
        }
        mDelegates.append(delegate.getKey(), delegate);
    }

    @Override
    public int getKey() {
        return LightValues.NONE;
    }

    @Override
    public LightHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LightHolder holder = null;
        for (int i = 0; i < mDelegates.size(); i++) {
            holder = mDelegates.valueAt(i).onCreateViewHolder(parent, viewType);
            if (holder != null) {
                break;
            }
        }
        return holder;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull LightHolder holder) {
        for (int i = 0; i < mDelegates.size(); i++) {
            mDelegates.valueAt(i).onViewAttachedToWindow(holder);
        }
    }

    @Override
    public boolean onBindViewHolder(LightHolder holder, int position) {
        boolean result = false;
        for (int i = 0; i < mDelegates.size(); i++) {
            if (mDelegates.valueAt(i).onBindViewHolder(holder, position)) {
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        for (int i = 0; i < mDelegates.size(); i++) {
            mDelegates.valueAt(i).onAttachedToRecyclerView(recyclerView);
        }
    }

    @Override
    public void onAttachAdapter(LightAdapter adapter) {
        super.onAttachAdapter(adapter);
        for (int i = 0; i < mDelegates.size(); i++) {
            mDelegates.valueAt(i).onAttachAdapter(adapter);
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (int i = 0; i < mDelegates.size(); i++) {
            count += mDelegates.valueAt(i).getItemCount();
        }
        return count;
    }


    @Override
    public int getItemViewType(int position) {
        int type = LightValues.NONE;
        for (int i = 0; i < mDelegates.size(); i++) {
            type = mDelegates.valueAt(i).getItemViewType(position);
            if (type != LightValues.NONE) {
                break;
            }
        }
        return type;
    }

    @SuppressWarnings("unchecked")
    public <D extends IDelegate> D get(int key) {
        IDelegate iDelegate = mDelegates.get(key);
        return (D) iDelegate;
    }


}
