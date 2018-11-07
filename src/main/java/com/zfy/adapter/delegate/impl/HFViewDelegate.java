package com.zfy.adapter.delegate.impl;

import android.view.View;
import android.view.ViewGroup;

import com.zfy.adapter.LightHolder;
import com.zfy.adapter.common.LightUtils;
import com.zfy.adapter.common.LightValues;
import com.zfy.adapter.delegate.IDelegate;
import com.zfy.adapter.listener.ViewHolderCallback;
import com.zfy.adapter.model.LightView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * CreateAt : 16/8/20
 * Describe : 完成添加 header&footer 功能
 *
 * @author chendong
 */
public class HFViewDelegate extends BaseViewDelegate {

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
        boolean isFooter = isFooterEnable() && viewType == LightValues.TYPE_FOOTER;
        boolean isHeader = isHeaderEnable() && viewType == LightValues.TYPE_HEADER;
        if (isFooter) {
            holder = new LightHolder(mAdapter, viewType, mFooterView);
        } else if (isHeader) {
            holder = new LightHolder(mAdapter, viewType, mHeaderView);
        }
        return holder;
    }


    @Override
    public boolean onBindViewHolder(LightHolder holder, int position) {
        int itemViewType = mAdapter.getItemViewType(position);
        if (itemViewType == LightValues.TYPE_HEADER || itemViewType == LightValues.TYPE_FOOTER) {
            return true;
        }
        return super.onBindViewHolder(holder, position);
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
            return LightValues.TYPE_HEADER;
        }
        // pos 超出
        int aboveItemCount = mAdapter.getDelegateRegistry().getAboveItemCount(LightValues.FLOW_LEVEL_FOOTER);
        if (isFooterEnable() && position == aboveItemCount) {
            return LightValues.TYPE_FOOTER;
        }
        return LightValues.NONE;
    }

    /**
     * @return 获取 FooterView 容器
     */
    public ViewGroup getFooterView() {
        return mFooterView;
    }

    /**
     * @return 获取 HeaderView 容器
     */
    public ViewGroup getHeaderView() {
        return mHeaderView;
    }

    /**
     * 删除一个 View
     *
     * @param view view
     */
    public void removeHeaderView(View view) {
        if (view == null || !isAttached() || !isHeaderEnable() || mHeaderView == null) {
            return;
        }
        mHeaderView.removeView(view);
        Iterator<Binder> iterator = mHeaderBinders.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().holder.itemView.equals(view)) {
                iterator.remove();
                break;
            }
        }
        if (mHeaderView.getChildCount() == 0) {
            setHeaderEnable(false);
        }
    }


    /**
     * 删除一个 View
     *
     * @param view view
     */
    public void removeFooterView(View view) {
        if (view == null || !isAttached() || !isFooterEnable() || mFooterView == null) {
            return;
        }
        mFooterView.removeView(view);
        Iterator<Binder> iterator = mFooterBinders.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().holder.itemView.equals(view)) {
                iterator.remove();
                break;
            }
        }
        if (mFooterView.getChildCount() == 0) {
            setHeaderEnable(false);
        }
    }

    /**
     * 添加 Header
     *
     * @param lightView LightView
     * @param binder    数据绑定回调
     */
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
            LightHolder holder = new LightHolder(mAdapter, LightValues.TYPE_HEADER, lightView.view);
            binder.bind(holder, LightValues.NONE);
            mHeaderBinders.add(new Binder(holder, binder));
        });
    }


    /**
     * 添加 Footer
     *
     * @param lightView LightView
     * @param binder    binder
     */
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
            if (isNewFooter && mFooterView.getChildCount() == 1) {
                mAdapter.notifyItemInserted(mAdapter.getDelegateRegistry().getAboveItemCount(LightValues.FLOW_LEVEL_FOOTER));
            }
            LightHolder holder = new LightHolder(mAdapter, LightValues.TYPE_FOOTER, itemView);
            binder.bind(holder, LightValues.NONE);
            mFooterBinders.add(new Binder(holder, binder));
        });
    }

    /**
     * @return 当前 Header 是否可用
     */
    public boolean isHeaderEnable() {
        return mHeaderEnable;
    }

    /**
     * @return 当前 Footer 是否可用
     */
    public boolean isFooterEnable() {
        return mFooterEnable;
    }

    /**
     * 删除全部的 header
     */
    public void removeAllHeaderViews() {
        if (mHeaderView != null) {
            mHeaderView.removeAllViews();
        }
        mHeaderBinders.clear();
        setHeaderEnable(false);
    }

    /**
     * 删除全部的 footer
     */
    public void removeAllFooterViews() {
        if (mFooterView != null) {
            mFooterView.removeAllViews();
        }
        mFooterBinders.clear();
        setFooterEnable(false);
    }


    /**
     * 设置 Footer 是否展示
     *
     * @param footerEnable enable
     */
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

    /**
     * 设置 Header 是否展示
     *
     * @param headerEnable enable
     */
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

    /**
     * 回调所有的绑定更新 Header
     */
    public void notifyHeaderUpdate() {
        if (mHeaderView == null) {
            return;
        }
        for (Binder headerBinder : mHeaderBinders) {
            headerBinder.callback.bind(headerBinder.holder, LightValues.NONE);
        }
    }

    /**
     * 回调所有的绑定更新 Footer
     */
    public void notifyFooterUpdate() {
        if (mFooterView == null) {
            return;
        }
        for (Binder headerBinder : mFooterBinders) {
            headerBinder.callback.bind(headerBinder.holder, LightValues.NONE);
        }
    }

}
