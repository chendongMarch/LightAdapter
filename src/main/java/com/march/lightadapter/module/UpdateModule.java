package com.march.lightadapter.module;

import com.march.lightadapter.LightAdapter;

import java.util.List;

/**
 * CreateAt : 2017/6/14
 * Describe : 数据更新的module
 * 使用 RecyclerView post 在主线程更新数据
 * 提供简化更新数据的方法。
 *
 * @author chendong
 */
public class UpdateModule<D> extends AbstractModule {

    private int itemCount;

    @Override
    public void onAttachAdapter(LightAdapter adapter) {
        super.onAttachAdapter(adapter);
        refreshItemCount();
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
        if (mIsAttach) {
            mAttachRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        action.run();
                        refreshItemCount();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    private boolean checkPosition(int pos) {
        return mIsAttach
                && pos >= 0
                && pos < mAttachAdapter.getDatas().size();
    }



    @SuppressWarnings("unchecked")
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
    @SuppressWarnings("unchecked")
    public void update(List<D> data) {
        mAttachAdapter.setDatas(data);
        notifyDataSetChanged();
    }

    // 更新全部数据
    public void update() {
        notifyDataSetChanged();
    }


    // 在尾部追加数据
    @SuppressWarnings("unchecked")
    public void appendTailList(List<D> datas, boolean isAllData) {
        if (isAllData)
            mAttachAdapter.setDatas(datas);
        else
            mAttachAdapter.getDatas().addAll(datas);
        notifyItemRangeInserted(itemCount + (mAttachAdapter.isHasHeader() ? 1 : 0), getDatas().size() - itemCount);
    }

    // 在头部追加数据
    @SuppressWarnings("unchecked")
    public void appendHeadList(List<D> datas, boolean isAllData) {
        if (isAllData)
            mAttachAdapter.setDatas(datas);
        else
            getDatas().addAll(0, datas);
        notifyItemRangeInserted(0, getDatas().size() - itemCount);
    }

    private void refreshItemCount() {
        itemCount = getDatas().size();
    }

}
