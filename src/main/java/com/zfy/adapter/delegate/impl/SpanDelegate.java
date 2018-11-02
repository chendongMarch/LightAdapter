package com.zfy.adapter.delegate.impl;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import com.zfy.adapter.LightHolder;
import com.zfy.adapter.model.ModelType;
import com.zfy.adapter.common.LightValues;

/**
 * CreateAt : 2018/2/24
 * Describe : 完成跨越整行功能
 *
 * @author chendong
 */
public class SpanDelegate extends BaseDelegate {

    @Override
    public void onViewAttachedToWindow(@NonNull LightHolder holder) {
        super.onViewAttachedToWindow(holder);
        // 处理 StaggeredGridLayoutManager 的 span
        RecyclerView.LayoutManager layoutManager = mView.getLayoutManager();
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            int type = holder.getItemViewType();
            ModelType modelType = mAdapter.getType(type);
            if (modelType.getSpanSize() == LightValues.SPAN_SIZE_ALL) {
                ViewGroup.LayoutParams originLp = holder.getItemView().getLayoutParams();
                StaggeredGridLayoutManager.LayoutParams layoutParams =
                        new StaggeredGridLayoutManager.LayoutParams(originLp.width, originLp.height);
                layoutParams.setFullSpan(true);
                holder.getItemView().setLayoutParams(layoutParams);
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        // 处理 GridLayoutManager 的 span
        RecyclerView.LayoutManager layoutManager = mView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = mAdapter.getItemViewType(position);
                    ModelType modelType = mAdapter.getType(type);
                    int spanCount = gridLayoutManager.getSpanCount();
                    int spanSize = modelType.getSpanSize();
                    if (spanSize == LightValues.SPAN_SIZE_ALL) {
                        spanSize = spanCount;
                    } else if (spanSize == LightValues.SPAN_SIZE_HALF && spanCount % 2 == 0) {
                        spanSize = spanCount / 2;
                    } else if (spanSize == LightValues.SPAN_SIZE_HALF && spanCount % 3 == 0) {
                        spanSize = spanCount / 3;
                    } else if (spanSize <= 0) {
                        spanSize = 1;
                    }
                    return spanSize;
                }
            });
        }
    }

    @Override
    public int getKey() {
        return SPAN;
    }
}



