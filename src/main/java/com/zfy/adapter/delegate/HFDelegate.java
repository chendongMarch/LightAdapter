package com.zfy.adapter.delegate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zfy.adapter.LightHolder;
import com.zfy.adapter.VALUE;
import com.zfy.adapter.ViewHolderBinder;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * CreateAt : 16/8/20
 * Describe : Header Footer模块
 *
 * @author chendong
 */
public class HFDelegate extends BaseDelegate {

    private ViewGroup mHeaderView;
    private ViewGroup mFooterView;

    private List<ViewHolderBinder> mHeaderViewHolderBinders = new ArrayList<>();
    private List<ViewHolderBinder> mFooterViewHolderBinders = new ArrayList<>();

    @Override
    public LightHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LightHolder holder = null;
        boolean isFooter = isFooterEnable() && viewType == VALUE.TYPE_FOOTER;
        boolean isHeader = isHeaderEnable() && viewType == VALUE.TYPE_HEADER;
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
        if (itemViewType == VALUE.TYPE_HEADER || itemViewType == VALUE.TYPE_FOOTER) {
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
        if (isHeaderEnable() && position == 0)
            return VALUE.TYPE_HEADER;
        // pos 超出
        if (isFooterEnable() && position == mAdapter.getItemCount() - 1)
            return VALUE.TYPE_FOOTER;
        return VALUE.NONE;
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
    public void addHeaderView(View view, int index, ViewHolderBinder binder) {
        boolean isNewHeader = false;
        if (mHeaderView == null) {
            mHeaderView = createContainerView(mAdapter.getContext(), Utils.getRecyclerViewOrientation(mView));
            isNewHeader = true;
        }
        final int childCount = mHeaderView.getChildCount();
        if (index < 0 || index > childCount) {
            index = childCount;
        }
        mHeaderView.addView(view, index);
        LightHolder holder = new LightHolder(mAdapter, VALUE.TYPE_HEADER, mHeaderView);
        binder.onBindViewHolder(holder, VALUE.NONE);
        if (isNewHeader && mHeaderView.getChildCount() == 1) {
            mAdapter.notifyItemInserted(0);
        }
        mHeaderViewHolderBinders.add(binder);
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
    public void addFooterView(View view, int index, ViewHolderBinder binder) {
        boolean isNewFooter = false;
        if (mFooterView == null) {
            mFooterView = createContainerView(mAdapter.getContext(), Utils.getRecyclerViewOrientation(mView));
            isNewFooter = true;
        }
        final int childCount = mFooterView.getChildCount();
        if (index < 0 || index > childCount) {
            index = childCount;
        }
        mFooterView.addView(view, index);
        LightHolder holder = new LightHolder(mAdapter, VALUE.TYPE_FOOTER, mFooterView);
        binder.onBindViewHolder(holder, VALUE.NONE);
        if (isNewFooter && mFooterView.getChildCount() == 1) {
            mAdapter.notifyItemInserted(mAdapter.getItemCount() - 1);
        }
        mFooterViewHolderBinders.add(binder);
    }

    // header 是否可用
    public boolean isHeaderEnable() {
        return mHeaderView != null;
    }

    // footer 是否可用
    public boolean isFooterEnable() {
        return mFooterView != null;
    }

    // 隐藏/隐藏 Footer
    public void setFooterEnable(boolean footerEnable) {
        if (isFooterEnable()) {
            mFooterView.setVisibility(footerEnable ? View.VISIBLE : View.GONE);
            mAdapter.notifyItemChanged(getItemCount() - 1);
        }
    }

    // 显示/隐藏 Header
    public void setHeaderEnable(boolean headerEnable) {
        if (isHeaderEnable()) {
            mHeaderView.setVisibility(headerEnable ? View.VISIBLE : View.GONE);
            mAdapter.notifyItemChanged(0);
        }
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

    // 更新 Header
    public void notifyHeaderUpdate() {
        if (mHeaderView == null) {
            return;
        }
        LightHolder holder = new LightHolder(mAdapter, VALUE.TYPE_HEADER, mHeaderView);
        for (ViewHolderBinder binder : mHeaderViewHolderBinders) {
            binder.onBindViewHolder(holder, VALUE.NONE);
        }
    }

    // 更新 Footer
    public void notifyFooterUpdate() {
        LightHolder holder = new LightHolder(mAdapter, VALUE.TYPE_FOOTER, mFooterView);
        for (ViewHolderBinder binder : mFooterViewHolderBinders) {
            binder.onBindViewHolder(holder, VALUE.NONE);
        }
    }


    @Override
    public int getKey() {
        return IDelegate.HF;
    }
}
