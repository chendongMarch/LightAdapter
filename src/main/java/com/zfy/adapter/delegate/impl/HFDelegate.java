package com.zfy.adapter.delegate.impl;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zfy.adapter.LightHolder;
import com.zfy.adapter.common.Utils;
import com.zfy.adapter.common.Values;
import com.zfy.adapter.delegate.IDelegate;
import com.zfy.adapter.listener.ViewHolderBinder;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * CreateAt : 16/8/20
 * Describe : 完成添加 header&footer 功能
 *
 * @author chendong
 */
public class HFDelegate extends BaseDelegate {

    private ViewGroup mHeaderView;
    private ViewGroup mFooterView;

    private List<ViewHolderBinder> mHeaderViewHolderBinders = new ArrayList<>();
    private List<ViewHolderBinder> mFooterViewHolderBinders = new ArrayList<>();
    private List<Runnable> mPendingRunnables = new ArrayList<>();

    @Override
    public LightHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LightHolder holder = null;
        boolean isFooter = isFooterEnable() && viewType == Values.TYPE_FOOTER;
        boolean isHeader = isHeaderEnable() && viewType == Values.TYPE_HEADER;
        if (isFooter) {
            holder = new LightHolder(mAdapter, viewType, mFooterView);
        } else if (isHeader) {
            holder = new LightHolder(mAdapter, viewType, mHeaderView);
        }
        return holder;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (!mPendingRunnables.isEmpty()) {
            for (Runnable runnable : mPendingRunnables) {
                runnable.run();
            }
        }
    }

    @Override
    public boolean onBindViewHolder(LightHolder holder, int position) {
        int itemViewType = mAdapter.getItemViewType(position);
        if (itemViewType == Values.TYPE_HEADER || itemViewType == Values.TYPE_FOOTER) {
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
    public int getItemViewType(int position) {
        // 有 header 且位置 0
        if (isHeaderEnable() && position == 0) {
            return Values.TYPE_HEADER;
        }
        // pos 超出
        if (isFooterEnable() && position == mAdapter.getItemCount() - 1) {
            return Values.TYPE_FOOTER;
        }
        return Values.NONE;
    }

    public ViewGroup getFooterView() {
        return mFooterView;
    }

    public ViewGroup getHeaderView() {
        return mHeaderView;
    }

    // 创建容器 View
    private ViewGroup createContainerView(Context context, int orientation) {
        LinearLayout container = new LinearLayout(context);
        if (orientation == LinearLayout.VERTICAL) {
            container.setOrientation(LinearLayout.VERTICAL);
            container.setLayoutParams(new RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        } else {
            container.setOrientation(LinearLayout.HORIZONTAL);
            container.setLayoutParams(new RecyclerView.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
        }
        return container;
    }


    // 添加 Header
    public void addHeaderView(int layoutId, ViewHolderBinder binder) {
        View view = Utils.inflateView(mAdapter.getContext(), layoutId);
        if (view != null) {
            addHeaderView(view, -1, binder);
        }
    }

    // 添加 Header
    public void addHeaderView(View view, ViewHolderBinder binder) {
        addHeaderView(view, -1, binder);
    }

    // 添加 Header
    public void addHeaderView(int layoutId, int index, ViewHolderBinder binder) {
        View view = Utils.inflateView(mAdapter.getContext(), layoutId);
        if (view != null) {
            addHeaderView(view, index, binder);
        }
    }


    // 添加 Header
    public void addHeaderView(View view, final int index, ViewHolderBinder binder) {
        Runnable runnable = () -> {
            int viewIndex = index;
            boolean isNewHeader = false;
            if (mHeaderView == null) {
                mHeaderView = createContainerView(mAdapter.getContext(), Utils.getRecyclerViewOrientation(mView));
                isNewHeader = true;
            }
            final int childCount = mHeaderView.getChildCount();
            if ((viewIndex < 0) || (viewIndex > childCount)) {
                viewIndex = childCount;
            }
            mHeaderView.addView(view, viewIndex);
            LightHolder holder = new LightHolder(mAdapter, Values.TYPE_HEADER, mHeaderView);
            binder.onBindViewHolder(holder, Values.NONE);
            mHeaderEnable = true;
            if (isNewHeader && mHeaderView.getChildCount() == 1) {
                mAdapter.notifyItemInserted(0);
            }
            mHeaderViewHolderBinders.add(binder);
        };
        if (isAttached() && mView.getLayoutManager() != null) {
            runnable.run();
        } else {
            mPendingRunnables.add(runnable);
        }
    }

    // 添加 Footer
    public void addFooterView(int layoutId, ViewHolderBinder binder) {
        View view = Utils.inflateView(mAdapter.getContext(), layoutId);
        if (view != null) {
            addFooterView(view, -1, binder);
        }
    }

    // 添加 Footer
    public void addFooterView(View view, ViewHolderBinder binder) {
        addHeaderView(view, -1, binder);
    }

    // 添加 Footer
    public void addFooterView(int layoutId, int index, ViewHolderBinder binder) {
        View view = Utils.inflateView(mAdapter.getContext(), layoutId);
        if (view != null) {
            addHeaderView(view, index, binder);
        }
    }

    // 添加 Footer
    public void addFooterView(View view, final int index, ViewHolderBinder binder) {
        Runnable runnable = () -> {
            int viewIndex = index;
            boolean isNewFooter = false;
            if (mFooterView == null) {
                mFooterView = createContainerView(mAdapter.getContext(), Utils.getRecyclerViewOrientation(mView));
                isNewFooter = true;
            }
            final int childCount = mFooterView.getChildCount();
            if (viewIndex < 0 || viewIndex > childCount) {
                viewIndex = childCount;
            }
            mFooterView.addView(view, viewIndex);
            LightHolder holder = new LightHolder(mAdapter, Values.TYPE_FOOTER, mFooterView);
            binder.onBindViewHolder(holder, Values.NONE);
            mFooterEnable = true;
            if (isNewFooter && mFooterView.getChildCount() == 1) {
                mAdapter.notifyItemInserted(mAdapter.getItemCount() - 1);
            }
            mFooterViewHolderBinders.add(binder);
        };
        if (isAttached() && mView.getLayoutManager() != null) {
            runnable.run();
        } else {
            mPendingRunnables.add(runnable);
        }
    }

    // header 是否可用
    public boolean isHeaderEnable() {
        return mHeaderEnable;
    }

    // footer 是否可用
    public boolean isFooterEnable() {
        return mFooterEnable;
    }

    // 清空 Header
    public void clearHeaderView() {
        if (mHeaderView != null) {
            mHeaderView.removeAllViews();
        }
        mHeaderViewHolderBinders.clear();
    }

    // 清空 Footer
    public void clearFooterView() {
        if (mFooterView != null) {
            mFooterView.removeAllViews();
        }
        mFooterViewHolderBinders.clear();
    }

    private boolean mFooterEnable;
    private boolean mHeaderEnable;

    public void setFooterEnable(boolean footerEnable) {
        if (mFooterView == null) {
            return;
        }
        if (mFooterEnable == footerEnable) {
            return;
        }
        mFooterEnable = footerEnable;
        if (mFooterEnable) {
            mAdapter.notifyItem().insert(mAdapter.getItemCount());
        } else {
            mAdapter.notifyItem().remove(mAdapter.getItemCount());
        }
    }

    public void setHeaderEnable(boolean headerEnable) {
        if (mHeaderView == null) {
            return;
        }
        if (mHeaderEnable == headerEnable) {
            return;
        }
        mHeaderEnable = headerEnable;
        if (mHeaderEnable) {
            mAdapter.notifyItem().insert(0);
        } else {
            mAdapter.notifyItem().remove(0);
        }
    }

    // 更新 Header
    public void notifyHeaderUpdate() {
        if (mHeaderView == null) {
            return;
        }
        LightHolder holder = new LightHolder(mAdapter, Values.TYPE_HEADER, mHeaderView);
        for (ViewHolderBinder binder : mHeaderViewHolderBinders) {
            binder.onBindViewHolder(holder, Values.NONE);
        }
    }

    // 更新 Footer
    public void notifyFooterUpdate() {
        LightHolder holder = new LightHolder(mAdapter, Values.TYPE_FOOTER, mFooterView);
        for (ViewHolderBinder binder : mFooterViewHolderBinders) {
            binder.onBindViewHolder(holder, Values.NONE);
        }
    }


    @Override
    public int getKey() {
        return IDelegate.HF;
    }
}
