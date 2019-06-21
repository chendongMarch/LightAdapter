package com.zfy.light.sample;


import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.common.LightUtil;
import com.zfy.adapter.model.ModelType;

/**
 * CreateAt : 2018/9/5
 * Describe : 为 GridLayoutManager 的布局，在每个 item 周围均匀绘制间距
 *
 * @author chendong
 */
public class TypeGridSpaceItemDecoration extends RecyclerView.ItemDecoration {


    private int getSpanCount(RecyclerView recyclerView) {
        return LightUtil.getRecyclerViewSpanCount(recyclerView);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (position == RecyclerView.NO_POSITION) {
            return;
        }
        LightAdapter adapter = (LightAdapter) parent.getAdapter();
        ModelType modelType = adapter.getModelType(adapter.getItemViewType(position));
        if (modelType.spaceRect == null) {
            return;
        }
        int spanCount = getSpanCount(parent);
        int typePosition;
        if (position == 0) {
            typePosition = 0;
        } else {
            int firstSameTypePosition = getFirstSameTypePosition(adapter, position - 1, modelType.type);
            if (firstSameTypePosition < 0) {
                typePosition = 0;
            } else {
                typePosition = position - firstSameTypePosition - 1;
            }
        }
        int spanSize = modelType.spanSize = LightUtil.getSpanSize(modelType.spanSize, spanCount);
        int columnCount = spanCount / spanSize;

        if (typePosition % columnCount == 0) {
            // 开始
            outRect.left = modelType.spaceRect.left;
        }
        outRect.right = modelType.spaceRect.right;
        if (typePosition / columnCount == 0) {
            outRect.top = modelType.spaceRect.top;
        }
        outRect.bottom = modelType.spaceRect.bottom;
    }


    private int getFirstSameTypePosition(LightAdapter adapter, int position, int type) {
        for (int i = position; i >= 0; i--) {
            if (adapter.getItemViewType(i) != type) {
                return i;
            }
        }
        return -1;
    }
}