package com.zfy.adapter.delegate.impl;

import android.view.View;
import android.view.ViewGroup;

import com.zfy.adapter.LightHolder;
import com.zfy.adapter.callback.ViewHolderCallback;
import com.zfy.adapter.common.ItemType;
import com.zfy.adapter.common.LightUtils;
import com.zfy.adapter.common.LightValues;
import com.zfy.adapter.delegate.IDelegate;
import com.zfy.adapter.delegate.refs.FooterRef;
import com.zfy.adapter.delegate.refs.HeaderRef;
import com.zfy.adapter.model.LightView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * CreateAt : 16/8/20
 * Describe : 完成添加 header footer 功能
 *
 * @author chendong
 */
public class HFViewDelegate extends BaseViewDelegate implements HeaderRef, FooterRef {

    private ViewGroup mHeaderView;
    private ViewGroup mFooterView;

    private List<Binder> mHeaderBinders = new ArrayList<>();
    private List<Binder> mFooterBinders = new ArrayList<>();

    private boolean mFooterEnable;
    private boolean mHeaderEnable;

    static class Binder {
        ViewHolderCallback callback;
        LightHolder holder;

        public Binder(LightHolder holder, ViewHolderCallback callback) {
            this.callback = callback;
            this.holder = holder;
        }
    }


    @Override
    public int getKey() {
        return IDelegate.HF;
    }


    @Override
    public LightHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LightHolder holder = null;
        boolean isFooter = isFooterEnable() && viewType == ItemType.TYPE_FOOTER;
        boolean isHeader = isHeaderEnable() && viewType == ItemType.TYPE_HEADER;
        if (isFooter) {
            holder = new LightHolder(mAdapter, viewType, mFooterView);
        } else if (isHeader) {
            holder = new LightHolder(mAdapter, viewType, mHeaderView);
        }
        return holder;
    }


    @Override
    public boolean onBindViewHolder(LightHolder holder, int layoutIndex) {
        int itemViewType = mAdapter.getItemViewType(layoutIndex);
        if (itemViewType == ItemType.TYPE_HEADER) {
//            for (Binder headerBinder : mHeaderBinders) {
//                headerBinder.callback.bind(headerBinder.holder);
//            }
            return true;
        } else if (itemViewType == ItemType.TYPE_FOOTER) {
//            for (Binder footerBinder : mFooterBinders) {
//                footerBinder.callback.bind(footerBinder.holder);
//            }
            return true;
        }
        return super.onBindViewHolder(holder, layoutIndex);
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (isHeaderEnable()) count++;
        if (isFooterEnable()) count++;
        return count;
    }

    @Override
    public int getAboveItemCount(int level) {
        int count = 0;
        if (isHeaderEnable() && level > LightValues.FLOW_LEVEL_HEADER) count++;
        if (isFooterEnable() && level > LightValues.FLOW_LEVEL_FOOTER) count++;
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        // 有 header 且位置 0
        if (isHeaderEnable() && position == 0) {
            return ItemType.TYPE_HEADER;
        }
        // pos 超出
        int aboveItemCount = mAdapter.getDelegateRegistry().getAboveItemCount(LightValues.FLOW_LEVEL_FOOTER);
        if (isFooterEnable() && position == aboveItemCount) {
            return ItemType.TYPE_FOOTER;
        }
        return ItemType.TYPE_NONE;
    }


    @Override
    public ViewGroup getFooterView() {
        return mFooterView;
    }

    @Override
    public ViewGroup getHeaderView() {
        return mHeaderView;
    }

    @Override
    public void removeHeaderView(LightView lightView) {
        if (lightView == null || lightView.view == null || !isAttached() || !isHeaderEnable() || mHeaderView == null) {
            return;
        }
        mHeaderView.removeView(lightView.view);
        Iterator<Binder> iterator = mHeaderBinders.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().holder.itemView.equals(lightView.view)) {
                iterator.remove();
                break;
            }
        }
        if (mHeaderView.getChildCount() == 0) {
            setHeaderEnable(false);
        }
    }

    @Override
    public void removeFooterView(LightView lightView) {
        if (lightView == null || lightView.view == null || !isAttached() || !isFooterEnable() || mFooterView == null) {
            return;
        }
        mFooterView.removeView(lightView.view);
        Iterator<Binder> iterator = mFooterBinders.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().holder.itemView.equals(lightView.view)) {
                iterator.remove();
                break;
            }
        }
        if (mFooterView.getChildCount() == 0) {
            setFooterEnable(false);
        }
    }

    @Override
    public void addHeaderView(LightView lightView, ViewHolderCallback binder) {
        postOnRecyclerViewAttach(() -> {
            lightView.inflate(mAdapter.getContext());
            boolean isNewHeader = false;
            if (mHeaderView == null) {
                mHeaderView = LightUtils.createWrapContentLinearContainerView(mAdapter.getContext(), mView);
                isNewHeader = true;
            }
            final int childCount = mHeaderView.getChildCount();
            if ((lightView.index < 0) || (lightView.index > childCount)) {
                lightView.index = childCount;
            }
            mHeaderView.addView(lightView.view, lightView.index);
            mHeaderEnable = true;
            if (isNewHeader && mHeaderView.getChildCount() == 1) {
                int pos = mAdapter.getDelegateRegistry().getAboveItemCount(LightValues.FLOW_LEVEL_HEADER);
                mAdapter.notifyItemInserted(pos);
            }
            LightHolder holder = new LightHolder(mAdapter, ItemType.TYPE_HEADER, lightView.view);
            if(binder!=null){
                binder.bind(holder);
            }
            mHeaderBinders.add(new Binder(holder, binder));
        });
    }

    @Override
    public void addFooterView(LightView lightView, ViewHolderCallback binder) {
        postOnRecyclerViewAttach(() -> {
            lightView.inflate(mAdapter.getContext());
            boolean isNewFooter = false;
            if (mFooterView == null) {
                mFooterView = LightUtils.createWrapContentLinearContainerView(mAdapter.getContext(), mView);
                isNewFooter = true;
            }
            final int childCount = mFooterView.getChildCount();
            if (lightView.index < 0 || lightView.index > childCount) {
                lightView.index = childCount;
            }
            View itemView = lightView.view;
            mFooterView.addView(itemView, lightView.index);
            mFooterEnable = true;
            LightHolder holder = new LightHolder(mAdapter, ItemType.TYPE_FOOTER, itemView);
            if (binder != null) {
                binder.bind(holder);
            }
            mFooterBinders.add(new Binder(holder, binder));
            if (isNewFooter && mFooterView.getChildCount() == 1) {
                if (mAdapter.getDatas().size() == 0) {
                    mAdapter.notifyItem().change();
                } else {
                    mAdapter.notifyItem().insert(mAdapter.getDelegateRegistry().getAboveItemCount(LightValues.FLOW_LEVEL_FOOTER));
                }
            }
        });
    }

    @Override
    public boolean isHeaderEnable() {
        return mHeaderEnable;
    }

    @Override
    public boolean isFooterEnable() {
        return mFooterEnable;
    }

    @Override
    public void removeAllHeaderViews() {
        if (mHeaderView != null) {
            mHeaderView.removeAllViews();
        }
        mHeaderBinders.clear();
        setHeaderEnable(false);
    }

    @Override
    public void removeAllFooterViews() {
        if (mFooterView != null) {
            mFooterView.removeAllViews();
        }
        mFooterBinders.clear();
        setFooterEnable(false);
    }

    @Override
    public void setFooterEnable(boolean footerEnable) {
        if (mFooterView == null) {
            return;
        }
        if (mFooterEnable == footerEnable) {
            return;
        }
        mFooterEnable = footerEnable;
        if (mFooterEnable) {
            mAdapter.notifyItem().insert(mAdapter.getDelegateRegistry().getAboveItemCount(LightValues.FLOW_LEVEL_FOOTER));
        } else {
            mAdapter.notifyItem().remove(mAdapter.getDelegateRegistry().getAboveItemCount(LightValues.FLOW_LEVEL_FOOTER));
        }
    }

    @Override
    public void setHeaderEnable(boolean headerEnable) {
        if (mHeaderView == null) {
            return;
        }
        if (mHeaderEnable == headerEnable) {
            return;
        }
        mHeaderEnable = headerEnable;
        if (mHeaderEnable) {
            mAdapter.notifyItem().insert(mAdapter.getDelegateRegistry().getAboveItemCount(LightValues.FLOW_LEVEL_HEADER));
        } else {
            mAdapter.notifyItem().remove(mAdapter.getDelegateRegistry().getAboveItemCount(LightValues.FLOW_LEVEL_HEADER));
        }
    }

    @Override
    public void notifyHeaderUpdate() {
        if (mHeaderView == null) {
            return;
        }
        for (Binder binder : mHeaderBinders) {
            if(binder.callback!=null) {
                binder.callback.bind(binder.holder);
            }
        }
    }

    @Override
    public void notifyFooterUpdate() {
        if (mFooterView == null) {
            return;
        }
        for (Binder binder : mFooterBinders) {
            if(binder.callback!=null) {
                binder.callback.bind(binder.holder);
            }
        }
    }

}
