package com.zfy.adapter.extend.decoration;


import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * CreateAt : 2018/9/5
 * Describe : 为 GridLayoutManager 的布局，在每个 item 周围均匀绘制间距
 *
 * @author chendong
 */
public class LinearSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int     spacing;
    private boolean includeEdge;

    /**
     * @param spacing     列中间的space
     * @param includeEdge
     */
    public LinearSpaceItemDecoration(int spacing, boolean includeEdge) {
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        if (includeEdge) {
            outRect.left = position == 0 ? spacing : 0;
            outRect.right = spacing;
        } else {
            outRect.left = position == 0 ? 0 : spacing;
            outRect.right = 0;
        }
    }
}