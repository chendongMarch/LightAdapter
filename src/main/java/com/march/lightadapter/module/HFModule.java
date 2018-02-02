package com.march.lightadapter.module;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.march.lightadapter.LightAdapter;
import com.march.lightadapter.LightHolder;

/**
 * CreateAt : 16/8/20
 * Describe : Header Footer模块
 *
 * @author chendong
 */
public class HFModule extends AbstractModule {

    public static final int NO_RES = 0;

    private View headerView;
    private View footerView;
    private int mHeaderRes = NO_RES, mFooterRes = NO_RES;
    private boolean isHeaderEnable = true;
    private boolean isFooterEnable = true;

    private Context mContext;

    public HFModule(Context context, int headerRes, int footerRes, RecyclerView recyclerView) {
        mContext = context;
        mHeaderRes = headerRes;
        mFooterRes = footerRes;
    }

    public boolean isFullSpan(int viewType) {
        return viewType == LightAdapter.TYPE_HEADER || viewType == LightAdapter.TYPE_FOOTER;
    }

    public boolean isHeaderEnable() {
        return isHeaderEnable;
    }

    public boolean isFooterEnable() {
        return isFooterEnable;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        isHeaderEnable = mHeaderRes != NO_RES;
        if (isHeaderEnable) {
            headerView = LayoutInflater.from(mContext).inflate(mHeaderRes, recyclerView, false);
        }

        isFooterEnable = mFooterRes != NO_RES;
        if (isFooterEnable) {
            footerView = LayoutInflater.from(mContext).inflate(mFooterRes, recyclerView, false);
        }
    }

    public void setFooterEnable(boolean footerEnable) {
        if (mFooterRes == NO_RES)
            return;
        if (isFooterEnable == footerEnable)
            return;
        if (!isAttachSuccess())
            return;
        if (footerEnable) {
            footerView = LayoutInflater.from(mContext).inflate(mFooterRes, mAttachRecyclerView, false);
            if (footerView != null) {
                isFooterEnable = true;
                mAttachAdapter.getUpdateModule().notifyItemInserted(mAttachAdapter.getItemCount());
            }
        } else {
            if (footerView != null) {
                footerView = null;
                isFooterEnable = false;
                mAttachAdapter.getUpdateModule().notifyItemRemoved(mAttachAdapter.getItemCount());
            }
        }
    }

    public void setHeaderEnable(boolean headerEnable) {
        if (mHeaderRes == NO_RES)
            return;
        if (isHeaderEnable == headerEnable)
            return;
        if (!isAttachSuccess())
            return;
        if (headerEnable) {
            headerView = LayoutInflater.from(mContext).inflate(mHeaderRes, mAttachRecyclerView, false);
            if (headerView != null) {
                isHeaderEnable = true;
                mAttachAdapter.getUpdateModule().notifyItemInserted(0);
            }
        } else {
            if (headerView != null) {
                headerView = null;
                isHeaderEnable = false;
                mAttachAdapter.getUpdateModule().notifyItemRemoved(0);
            }
        }
    }


    private LightHolder mHeaderHolder;
    private LightHolder mFooterHolder;

    public void notifyHeaderUpdate() {
        if (mHeaderHolder != null && mAttachAdapter != null) {
            mAttachAdapter.onBindHeader(mHeaderHolder);
        }
    }

    public void notifyFooterUpdate() {
        if (mFooterHolder != null && mAttachAdapter != null) {
            mAttachAdapter.onBindHeader(mFooterHolder);
        }
    }

    public <D> LightHolder<D> getHFViewHolder(int viewType) {
        LightHolder<D> holder = null;
        boolean isFooter = isFooterEnable() && viewType == LightAdapter.TYPE_FOOTER;
        boolean isHeader = isHeaderEnable() && viewType == LightAdapter.TYPE_HEADER;
        if (isFooter) {
            mFooterHolder = holder = new LightHolder<D>(mAttachAdapter.getContext(), footerView, viewType);
        } else if (isHeader) {
            mHeaderHolder = holder = new LightHolder<D>(mAttachAdapter.getContext(), headerView, viewType);
        }
        // 如果是StaggeredGridLayoutManager,放在创建ViewHolder里面来处理
        RecyclerView.LayoutManager layoutManager = mAttachRecyclerView.getLayoutManager();
        boolean isStaggeredGridLayoutManager = false;
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            isStaggeredGridLayoutManager = true;
        }
        if (isStaggeredGridLayoutManager && (isFooter || isHeader)) {
            ViewGroup.LayoutParams originLp = holder.getItemView().getLayoutParams();
            StaggeredGridLayoutManager.LayoutParams layoutParams =
                    new StaggeredGridLayoutManager.LayoutParams
                            (originLp.width,
                                    originLp.height);
            layoutParams.setFullSpan(true);
            holder.getItemView().setLayoutParams(layoutParams);
        }
        return holder;
    }
}
