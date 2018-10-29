package com.zfy.adapter.module;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.LightHolder;
import com.zfy.adapter.VALUE;

/**
 * CreateAt : 16/8/20
 * Describe : Header Footer模块
 *
 * @author chendong
 */
public class HFModule extends BaseModule {

    private View headerView;
    private View footerView;
    private int mHeaderRes = VALUE.NONE;
    private int mFooterRes = VALUE.NONE;
    private boolean isHeaderEnable = true;
    private boolean isFooterEnable = true;

    private LightHolder mHeaderHolder;
    private LightHolder mFooterHolder;

    private Context mContext;

    public HFModule(Context context, int headerRes, int footerRes) {
        mContext = context;
        mHeaderRes = headerRes;
        mFooterRes = footerRes;
    }

    public HFModule(Context context) {
        mContext = context;
    }


    @Override
    public LightHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LightHolder holder = null;
        boolean isFooter = isFooterEnable() && viewType == LightAdapter.TYPE_FOOTER;
        boolean isHeader = isHeaderEnable() && viewType == LightAdapter.TYPE_HEADER;
        if (isFooter) {
            holder = new LightHolder(mAdapter, viewType, footerView);
            mFooterHolder = holder;
        } else if (isHeader) {
            holder = new LightHolder(mAdapter, viewType, headerView);
            mHeaderHolder = holder;
        }
        // 如果是 StaggeredGridLayoutManager,放在创建 ViewHolder 里面来处理
        RecyclerView.LayoutManager layoutManager = mAttachRecyclerView.getLayoutManager();
        boolean isStaggeredGridLayoutManager = false;
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            isStaggeredGridLayoutManager = true;
        }
        if (isStaggeredGridLayoutManager && (isFooter || isHeader)) {
            ViewGroup.LayoutParams originLp = holder.getItemView().getLayoutParams();
            StaggeredGridLayoutManager.LayoutParams layoutParams =
                    new StaggeredGridLayoutManager.LayoutParams(originLp.width, originLp.height);
            layoutParams.setFullSpan(true);
            holder.getItemView().setLayoutParams(layoutParams);
        }
        return holder;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        isHeaderEnable = mHeaderRes != VALUE.NONE;
        if (isHeaderEnable) {
            headerView = LayoutInflater.from(mContext).inflate(mHeaderRes, recyclerView, false);
        }
        isFooterEnable = mFooterRes != VALUE.NONE;
        if (isFooterEnable) {
            footerView = LayoutInflater.from(mContext).inflate(mFooterRes, recyclerView, false);
        }
    }

    @Override
    public boolean onBindViewHolder(LightHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (isFooterEnable() && position == mAdapter.getItemCount() - 1) {
//            mAdapter.onBindFooterView(holder);
            return true;
        } else if (isHeaderEnable() && position == 0) {
//            mAdapter.onBindHeaderView(holder);
            return true;
        }
        return false;
    }


    public boolean isHeaderEnable() {
        return isHeaderEnable;
    }

    public boolean isFooterEnable() {
        return isFooterEnable;
    }

    public void setFooterEnable(boolean footerEnable) {
        if (mFooterHolder != null && mFooterHolder.itemView != null) {
            mFooterHolder.itemView.setVisibility(footerEnable ? View.VISIBLE : View.GONE);
        }
    }

    public void setHeaderEnable(boolean headerEnable) {
        if (mHeaderHolder != null && mHeaderHolder.itemView != null) {
            mHeaderHolder.itemView.setVisibility(headerEnable ? View.VISIBLE : View.GONE);
        }
    }

    public void notifyHeaderUpdate() {
        if (mHeaderHolder != null && mAdapter != null) {
//            mAdapter.onBindHeaderView(mHeaderHolder);
        }
    }

    public void notifyFooterUpdate() {
        if (mFooterHolder != null && mAdapter != null) {
//            mAdapter.onBindFooterView(mFooterHolder);
        }
    }

    public int getItemCount() {
        int count = 0;
        if (isHeaderEnable()) count++;
        if (isFooterEnable()) count++;
        return count;
    }


    public int getItemViewType(int position) {
        // 有header且位置0
        if (isHeaderEnable() && position == 0)
            return VALUE.TYPE_HEADER;
        // pos超出
        if (isFooterEnable() && position == mAdapter.getItemCount() - 1)
            return VALUE.TYPE_FOOTER;
        return VALUE.NONE;
    }
}
