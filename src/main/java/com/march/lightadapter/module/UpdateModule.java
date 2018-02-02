package com.march.lightadapter.module;

import android.support.v7.widget.RecyclerView;

import com.march.lightadapter.LightAdapter;
import com.march.lightadapter.LightHolder;
import com.march.lightadapter.listener.OnHolderUpdateListener;

import java.util.List;

/**
 * CreateAt : 2017/6/14
 * Describe : 数据更新的module
 * 使用 RecyclerView post 在主线程更新数据
 * 提供简化更新数据的方法。
 *
 * @author chendong
 */
public class UpdateModule<D> extends AbstractModule<D> {

    private int lastDataCount;

    @Override
    public void onAttachAdapter(LightAdapter<D> adapter) {
        super.onAttachAdapter(adapter);
        refreshLastDataCount();
    }

    public final void notifyDataSetChanged() {
        notifyInUIThread(new Runnable() {
            @Override
            public void run() {
                mAttachAdapter.notifyDataSetChanged();
            }
        });
    }

    public final void notifyItemChanged(final int position) {
        notifyInUIThread(new Runnable() {
            @Override
            public void run() {
                mAttachAdapter.notifyItemRangeChanged(position, 1);
            }
        });
    }

    public final void notifyItemRangeChanged(final int positionStart, final int itemCount) {
        notifyInUIThread(new Runnable() {
            @Override
            public void run() {
                mAttachAdapter.notifyItemRangeChanged(positionStart, itemCount);
            }
        });
    }

    public final void notifyItemInserted(final int position) {
        notifyInUIThread(new Runnable() {
            @Override
            public void run() {
                mAttachAdapter.notifyItemRangeInserted(position, 1);
            }
        });
    }

    public final void notifyItemMoved(final int fromPosition, final int toPosition) {
        notifyInUIThread(new Runnable() {
            @Override
            public void run() {
                mAttachAdapter.notifyItemMoved(fromPosition, toPosition);
            }
        });
    }

    public final void notifyItemRangeInserted(final int positionStart, final int itemCount) {
        notifyInUIThread(new Runnable() {
            @Override
            public void run() {
                mAttachAdapter.notifyItemRangeInserted(positionStart, itemCount);
            }
        });
    }


    public final void notifyItemRemoved(final int position) {
        notifyInUIThread(new Runnable() {
            @Override
            public void run() {
                mAttachAdapter.notifyItemRangeRemoved(position, 1);
            }
        });
    }

    public final void notifyItemRangeRemoved(final int positionStart, final int itemCount) {
        notifyInUIThread(new Runnable() {
            @Override
            public void run() {
                mAttachAdapter.notifyItemRangeRemoved(positionStart, itemCount);
            }
        });
    }

    private void notifyInUIThread(final Runnable action) {
        if (!isAttachSuccess())
            return;
        post(new Runnable() {
            @Override
            public void run() {
                try {
                    action.run();
                    refreshLastDataCount();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void notifyByHolder(int pos, OnHolderUpdateListener<D> onHolderUpdateListener) {
        if (!isAttachSuccess()) return;
        RecyclerView.ViewHolder findHolder = mAttachRecyclerView.findViewHolderForAdapterPosition(pos);
        if (findHolder != null && findHolder instanceof LightHolder) {
            LightHolder<D> holder = (LightHolder<D>) findHolder;
            onHolderUpdateListener.onChanged(holder, holder.getData(), pos, false);
        }
    }

    // 更新一项
    public void set(final int pos, D data) {
        if (!isSupportPos(pos))
            return;
        getDatas().set(pos, data);
        notifyItemChanged(pos);
    }

    // 清除数据
    public void clear() {
        getDatas().clear();
        notifyDataSetChanged();
    }

    // 更新全部数据
    public void update(List<D> data) {
        mAttachAdapter.setDatas(data);
        notifyDataSetChanged();
    }

    // 更新全部数据
    public void update() {
        notifyDataSetChanged();
    }

    // 在尾部追加数据
    public void appendTailList(List<D> datas, boolean isAllData) {
        if (isAllData)
            mAttachAdapter.setDatas(datas);
        else
            getDatas().addAll(datas);
        notifyItemRangeInserted(lastDataCount + getAppendCount(), getDatas().size() - lastDataCount);
    }

    // 在头部追加数据
    public void appendHeadList(List<D> datas, boolean isAllData) {
        if (isAllData)
            mAttachAdapter.setDatas(datas);
        else
            getDatas().addAll(0, datas);
        notifyItemRangeInserted(0, getDatas().size() - lastDataCount);
    }

    private void refreshLastDataCount() {
        lastDataCount = getDatas().size();
    }

    private int getAppendCount() {
        if (mAttachAdapter != null && mAttachAdapter.getHFModule() != null && mAttachAdapter.getHFModule().isFooterEnable()) {
            return 1;
        }
        return 0;
    }
}
