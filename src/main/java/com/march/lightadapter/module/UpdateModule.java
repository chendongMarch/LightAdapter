package com.march.lightadapter.module;

import android.support.v7.util.DiffUtil;

import com.march.lightadapter.LightAdapter;
import com.march.lightadapter.module.AbstractModule;

import java.util.List;

/**
 * CreateAt : 2018/2/24
 * Describe :
 * 数据更新的代理
 * 1. 将数据更新发送到 UI 线程
 * 2. 扩展更多更新数据的方法
 *
 * @author chendong
 */
public class UpdateModule<D> extends AbstractModule {

    private int itemCount;

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
        mAttachAdapter.getRecyclerView().post(action);
    }

    private boolean checkPosition(int pos) {
        return pos >= 0 && pos < mAttachAdapter.getDatas().size();
    }

    private List<D> getDatas() {
        return mAttachAdapter.getDatas();
    }

    // 更新一项
    public void set(int pos, D data) {
        if (checkPosition(pos)) {
            getDatas().set(pos, data);
            notifyItemChanged(pos);
        }
    }

    // 清除数据
    public void clear() {
        mAttachAdapter.getDatas().clear();
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
        itemCount = getDatas().size();
        if (isAllData)
            mAttachAdapter.setDatas(datas);
        else
            mAttachAdapter.getDatas().addAll(datas);
        notifyItemRangeInserted(itemCount + (mAttachAdapter.isHeaderEnable() ? 1 : 0), getDatas().size() - itemCount);
    }

    // 在头部追加数据
    public void appendHeadList(List<D> datas, boolean isAllData) {
        itemCount = getDatas().size();
        if (isAllData)
            mAttachAdapter.setDatas(datas);
        else
            getDatas().addAll(0, datas);
        notifyItemRangeInserted(0, getDatas().size() - itemCount);
    }


}
