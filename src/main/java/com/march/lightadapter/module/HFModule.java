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

    public HFModule(Context context, int headerRes, int footerRes) {
        mContext = context;
        mHeaderRes = headerRes;
        mFooterRes = footerRes;
    }

    public HFModule(Context context) {
        mContext = context;
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

        //        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//        if (layoutManager instanceof GridLayoutManager) {
//            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
//            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                @Override
//                public int getSpanSize(int position) {
//                    int type = mAttachAdapter.getItemViewType4HF(position);
//                    if (isFullSpan(type)) {
//                        return gridLayoutManager.getSpanCount();
//                    } else {
//                        return 1;
//                    }
//                }
//            });
//        }

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
        if (!mIsAttach)
            return;
        if (footerEnable) {
            footerView = LayoutInflater.from(mContext).inflate(mFooterRes, mAttachRecyclerView, false);
            if (footerView != null) {
                isFooterEnable = true;
                mAttachAdapter.update().notifyItemInserted(mAttachAdapter.getItemCount());
            }
        } else {
            if (footerView != null) {
                footerView = null;
                isFooterEnable = false;
                mAttachAdapter.update().notifyItemRemoved(mAttachAdapter.getItemCount());
            }
        }
    }

    public void setHeaderEnable(boolean headerEnable) {
        if (mHeaderRes == NO_RES)
            return;
        if (isHeaderEnable == headerEnable)
            return;
        if (!mIsAttach)
            return;
        if (headerEnable) {
            headerView = LayoutInflater.from(mContext).inflate(mHeaderRes, mAttachRecyclerView, false);
            if (headerView != null) {
                isHeaderEnable = true;
                mAttachAdapter.update().notifyItemInserted(0);
            }
        } else {
            if (headerView != null) {
                headerView = null;
                isHeaderEnable = false;
                mAttachAdapter.update().notifyItemRemoved(0);
            }
        }
    }


    private LightHolder mHeaderHolder;
    private LightHolder mFooterHolder;

    public void notifyHeaderUpdate() {
        if (mHeaderHolder != null && mAttachAdapter != null) {
            mAttachAdapter.onBindHeaderView(mHeaderHolder);
        }
    }

    public void notifyFooterUpdate() {
        if (mFooterHolder != null && mAttachAdapter != null) {
            mAttachAdapter.onBindFooterView(mFooterHolder);
        }
    }


    public int getItemCount4HF() {
        int result = 0;
        if (isHeaderEnable()) result++;
        if (isFooterEnable()) result++;
        return result;
    }


    public int getItemViewType4HF(int position) {

        // 有header且位置0
        if (isHeaderEnable() && position == 0)
            return LightAdapter.TYPE_HEADER;

        // pos超出
        if (isFooterEnable() && position == mAttachAdapter.getItemCount() - 1)
            return LightAdapter.TYPE_FOOTER;

        return 0;
    }


    @Override
    public boolean onBindViewHolder(LightHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (isFooterEnable() && position == mAttachAdapter.getItemCount() - 1) {
            mAttachAdapter.onBindFooterView(holder);
            return true;
        } else if (isHeaderEnable() && position == 0) {
            mAttachAdapter.onBindHeaderView(holder);
            return true;
        }
        return false;
    }

    @Override
    public LightHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LightHolder holder = null;
        boolean isFooter = isFooterEnable() && viewType == LightAdapter.TYPE_FOOTER;
        boolean isHeader = isHeaderEnable() && viewType == LightAdapter.TYPE_HEADER;
        if (isFooter) {
            mFooterHolder = holder = new LightHolder(mAttachAdapter.getContext(), "footer-holder",footerView);
        } else if (isHeader) {
            mHeaderHolder = holder = new LightHolder(mAttachAdapter.getContext(), "header-holder",headerView);
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
                    new StaggeredGridLayoutManager.LayoutParams (originLp.width, originLp.height);
            layoutParams.setFullSpan(true);
            holder.getItemView().setLayoutParams(layoutParams);
        }
        return holder;
    }
}
