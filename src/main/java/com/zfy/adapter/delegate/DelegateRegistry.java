package com.zfy.adapter.delegate;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.LightHolder;
import com.zfy.adapter.VALUE;

/**
 * CreateAt : 2018/10/28
 * Describe : 委托功能注册管理类
 *
 * @author chendong
 */
public class DelegateRegistry implements IDelegate {

    private SparseArray<IDelegate> mDelegates;

    public DelegateRegistry() {
        mDelegates = new SparseArray<>();
        register(new HFDelegate());
        register(new SpanDelegate());
        register(new TopMoreDelegate());
        register(new LoadMoreDelegate());
    }

    public void register(IDelegate delegate) {
        mDelegates.append(delegate.getKey(), delegate);
    }

    @Override
    public int getKey() {
        return VALUE.NONE;
    }

    @Override
    public LightHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LightHolder holder = null;
        for (int i = 0; i < mDelegates.size(); i++) {
            holder = mDelegates.get(i).onCreateViewHolder(parent, viewType);
            if (holder != null) {
                break;
            }
        }
        return holder;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull LightHolder holder) {
        for (int i = 0; i < mDelegates.size(); i++) {
            mDelegates.get(i).onViewAttachedToWindow(holder);
        }
    }

    @Override
    public boolean onBindViewHolder(LightHolder holder, int position) {
        boolean result = false;
        for (int i = 0; i < mDelegates.size(); i++) {
            if (mDelegates.get(i).onBindViewHolder(holder, position)) {
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        for (int i = 0; i < mDelegates.size(); i++) {
            mDelegates.get(i).onAttachedToRecyclerView(recyclerView);
        }
    }

    @Override
    public void onAttachAdapter(LightAdapter adapter) {
        for (int i = 0; i < mDelegates.size(); i++) {
            mDelegates.get(i).onAttachAdapter(adapter);
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (int i = 0; i < mDelegates.size(); i++) {
            count += mDelegates.get(i).getItemCount();
        }
        return count;
    }


    @Override
    public int getItemViewType(int position) {
        int type = VALUE.NONE;
        for (int i = 0; i < mDelegates.size(); i++) {
            type = mDelegates.get(i).getItemViewType(position);
            if (type != VALUE.NONE) {
                break;
            }
        }
        return type;
    }

    @SuppressWarnings("unchecked")
    public <D extends IDelegate> D get(int key, Class<D> clazz) {
        IDelegate iDelegate = mDelegates.get(key);
        if (iDelegate.getClass() == clazz) {
            return (D) iDelegate;
        }
        return null;
    }
}
