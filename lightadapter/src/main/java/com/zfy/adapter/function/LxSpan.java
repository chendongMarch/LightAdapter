package com.zfy.adapter.function;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import com.zfy.adapter.Lx;
import com.zfy.adapter.LxAdapter;
import com.zfy.adapter.LxVh;
import com.zfy.adapter.data.TypeOpts;


/**
 * CreateAt : 2019-09-01
 * Describe : 实现跨越多行的 span
 *
 * @author chendong
 */
public class LxSpan {

    public static void onViewAttachedToWindow(LxAdapter adapter, LxVh holder) {
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
            if (typeOpts.spanSize == Lx.SPAN_SIZE_ALL) {
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

    private static int getSpanSize(int spanSize, int spanCount) {
        if (spanSize == Lx.SPAN_NONE) {
            return 1;
        }
        if (spanSize > 0) {
            return spanSize;
        }
        if (spanSize == Lx.SPAN_SIZE_ALL) {
            spanSize = spanCount;
        } else if (spanSize == Lx.SPAN_SIZE_HALF && spanCount % 2 == 0) {
            spanSize = spanCount / 2;
        } else if (spanSize == Lx.SPAN_SIZE_THIRD && spanCount % 3 == 0) {
            spanSize = spanCount / 3;
        } else if (spanSize == Lx.SPAN_SIZE_QUARTER && spanCount % 4 == 0) {
            spanSize = spanCount / 4;
        }
        if (spanSize == Lx.SPAN_SIZE_HALF && spanCount % 2 != 0
                || spanSize == Lx.SPAN_SIZE_THIRD && spanCount % 3 != 0
                || spanSize == Lx.SPAN_SIZE_QUARTER && spanCount % 4 != 0) {
            throw new IllegalArgumentException("spanSize error, spanSize = " + spanCount + ", spanCount = " + spanCount);
        }
        return spanSize;
    }


}
