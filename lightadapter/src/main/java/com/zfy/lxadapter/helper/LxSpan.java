package com.zfy.lxadapter.helper;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import com.zfy.lxadapter.Lx;
import com.zfy.lxadapter.LxAdapter;
import com.zfy.lxadapter.LxViewHolder;
import com.zfy.lxadapter.data.TypeOpts;


/**
 * CreateAt : 2019-09-01
 * Describe : 实现跨越多行的 span
 *
 * @author chendong
 */
public class LxSpan {


    public static void onViewAttachedToWindow(LxAdapter adapter, LxViewHolder holder) {
        RecyclerView view = adapter.getView();
        if (view == null) {
            throw new IllegalStateException("set RecyclerView first");
        }
        // 处理 StaggeredGridLayoutManager 的 span
        RecyclerView.LayoutManager layoutManager = view.getLayoutManager();
        if (layoutManager == null) {
            throw new IllegalStateException("set layout manager first");
        }
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            int type = holder.getItemViewType();
            TypeOpts typeOpts = adapter.getTypeOpts(type);
            if (typeOpts.spanSize == Lx.SpanSize.ALL) {
                ViewGroup.LayoutParams originLp = holder.itemView.getLayoutParams();
                StaggeredGridLayoutManager.LayoutParams layoutParams =
                        new StaggeredGridLayoutManager.LayoutParams(originLp.width, originLp.height);
                layoutParams.setFullSpan(true);
                holder.itemView.setLayoutParams(layoutParams);
            }
        }
    }

    public static void onAttachedToRecyclerView(LxAdapter adapter) {
        // 处理 GridLayoutManager 的 span
        RecyclerView view = adapter.getView();
        if (view == null) {
            throw new IllegalStateException("set RecyclerView first");
        }
        RecyclerView.LayoutManager layoutManager = view.getLayoutManager();
        if (layoutManager == null) {
            throw new IllegalStateException("set layout manager first");
        }
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = adapter.getItemViewType(position);
                    TypeOpts typeOpts = adapter.getTypeOpts(type);
                    int spanCount = gridLayoutManager.getSpanCount();
                    typeOpts.spanSize = LxSpan.getSpanSize(typeOpts.spanSize, spanCount);
                    return typeOpts.spanSize;
                }
            });
        }
    }

    public static SpanSizeAdapter spanSizeAdapter;

    public static int getSpanSize(int spanSize, int spanCount) {
        if (spanSize == Lx.SpanSize.NONE) {
            return 1;
        }
        if (spanSize > 0) {
            return spanSize;
        }
        if (spanSize == Lx.SpanSize.ALL) {
            spanSize = spanCount;
        } else if (spanSize == Lx.SpanSize.HALF && spanCount % 2 == 0) {
            spanSize = spanCount / 2;
        } else if (spanSize == Lx.SpanSize.THIRD && spanCount % 3 == 0) {
            spanSize = spanCount / 3;
        } else if (spanSize == Lx.SpanSize.QUARTER && spanCount % 4 == 0) {
            spanSize = spanCount / 4;
        } else if (spanSizeAdapter != null) {
            spanSize = spanSizeAdapter.adapt(spanCount, spanSize);
        }
        if (spanSize == Lx.SpanSize.HALF && spanCount % 2 != 0
                || spanSize == Lx.SpanSize.THIRD && spanCount % 3 != 0
                || spanSize == Lx.SpanSize.QUARTER && spanCount % 4 != 0) {
            throw new IllegalArgumentException("spanSize error, spanSize = " + spanCount + ", spanCount = " + spanCount);
        }
        return spanSize;
    }


    public interface SpanSizeAdapter {
        int adapt(int spanCount, int spanSize);
    }

}
