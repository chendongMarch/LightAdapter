package com.zfy.adapter.delegate;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.LightHolder;
import com.zfy.adapter.common.ItemType;
import com.zfy.adapter.common.LightValues;
import com.zfy.adapter.delegate.impl.AnimatorDelegate;
import com.zfy.adapter.delegate.impl.BaseDelegate;
import com.zfy.adapter.delegate.impl.DragSwipeDelegate;
import com.zfy.adapter.delegate.impl.EmptyViewDelegate;
import com.zfy.adapter.delegate.impl.FakeDelegate;
import com.zfy.adapter.delegate.impl.HFViewDelegate;
import com.zfy.adapter.delegate.impl.LoadMoreDelegate;
import com.zfy.adapter.delegate.impl.LoadingViewDelegate;
import com.zfy.adapter.delegate.impl.NotifyDelegate;
import com.zfy.adapter.delegate.impl.SectionDelegate;
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

    public interface DelegateFactory {
        IDelegate create();
    }

    private SparseArray<IDelegate> mDelegates;

    private SparseArray<DelegateFactory> mDelegateFactorys;


    public DelegateRegistry() {
        mDelegates = new SparseArray<>();
        mDelegateFactorys = new SparseArray<>();

        register(new SpanDelegate());
        register(new NotifyDelegate());
        register(IDelegate.HF, HFViewDelegate::new);
        register(IDelegate.TOP_MORE, TopMoreDelegate::new);
        register(IDelegate.LOAD_MORE, LoadMoreDelegate::new);
        register(IDelegate.SELECTOR, SelectorDelegate::new);
        register(IDelegate.LOADING, LoadingViewDelegate::new);
        register(IDelegate.EMPTY, EmptyViewDelegate::new);
        register(IDelegate.DRAG_SWIPE, DragSwipeDelegate::new);
        register(IDelegate.SECTION, SectionDelegate::new);
        register(IDelegate.ANIMATOR, AnimatorDelegate::new);
        register(IDelegate.FAKE, FakeDelegate::new);
    }

    public void register(int key, DelegateFactory delegateFactory) {
        mDelegateFactorys.append(key, delegateFactory);
    }

    public void register(IDelegate delegate) {
        if (mAdapter != null) {
            delegate.onAttachAdapter(mAdapter);
        }
        mDelegates.append(delegate.getKey(), delegate);
    }

    public boolean isLoaded(int type) {
        return mDelegates.get(type) != null;
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
    public boolean onBindViewHolder(LightHolder holder, int layoutIndex) {
        boolean result = false;
        for (int i = 0; i < mDelegates.size(); i++) {
            if (mDelegates.valueAt(i).onBindViewHolder(holder, layoutIndex)) {
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
    public int getAboveItemCount(int level) {
        int count = 0;
        for (int i = 0; i < mDelegates.size(); i++) {
            count += mDelegates.valueAt(i).getAboveItemCount(level);
        }
        if (level > LightValues.FLOW_LEVEL_CONTENT) {
            count += mAdapter.getDatas().size();
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        int type = ItemType.TYPE_NONE;
        for (int i = 0; i < mDelegates.size(); i++) {
            type = mDelegates.valueAt(i).getItemViewType(position);
            if (type != ItemType.TYPE_NONE) {
                break;
            }
        }
        return type;
    }

    @SuppressWarnings("unchecked")
    public <D extends IDelegate> D get(int key) {
        IDelegate iDelegate = mDelegates.get(key);
        if (iDelegate == null) {
            DelegateFactory delegateFactory = mDelegateFactorys.get(key);
            if (delegateFactory != null) {
                iDelegate = delegateFactory.create();
                register(iDelegate);
                if (mAdapter != null) {
                    iDelegate.onAttachAdapter(mAdapter);
                }
                if (mView != null) {
                    iDelegate.onAttachedToRecyclerView(mView);
                }
            }
        }
        return (D) iDelegate;
    }
}
