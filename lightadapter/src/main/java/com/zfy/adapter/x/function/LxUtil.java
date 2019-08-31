package com.zfy.adapter.x.function;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * CreateAt : 2019-09-01
 * Describe :
 *
 * @author chendong
 */
public class LxUtil {

    /**
     * 获取 RecyclerView 防线，默认垂直
     *
     * @param view RecyclerView
     * @return 方向
     */
    public static int getRecyclerViewOrientation(RecyclerView view) {
        int orientation = RecyclerView.VERTICAL;
        if (view == null) {
            return orientation;
        }
        RecyclerView.LayoutManager layoutManager = view.getLayoutManager();
        if (layoutManager == null) {
            throw new IllegalStateException("set LayoutManager first");
        }
        if (layoutManager instanceof LinearLayoutManager) {
            orientation = ((LinearLayoutManager) layoutManager).getOrientation();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
        }
        return orientation;
    }


    /**
     * 获取最后一条展示的位置
     *
     * @param view RecyclerView
     * @return 最后一个位置 position
     */
    public static int getLastVisiblePosition(RecyclerView view) {
        int position;
        RecyclerView.LayoutManager manager = view.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            position = ((GridLayoutManager) manager).findLastVisibleItemPosition();
        } else if (manager instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
        } else if (manager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) manager;
            int[] lastPositions = layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMaxPosition(lastPositions);
        } else {
            position = manager.getItemCount() - 1;
        }
        return position;
    }

    /**
     * 获取第一条展示的位置
     *
     * @param view RecyclerView
     * @return 第一条展示的位置
     */
    public static int getFirstVisiblePosition(RecyclerView view) {
        int position;
        RecyclerView.LayoutManager manager = view.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            position = ((GridLayoutManager) manager).findFirstVisibleItemPosition();
        } else if (manager instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) manager).findFirstVisibleItemPosition();
        } else if (manager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) manager;
            int[] lastPositions = layoutManager.findFirstVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMaxPosition(lastPositions);
        } else {
            position = 0;
        }
        return position;
    }


    // 在一堆位置中获取最大的获得最大的位置
    private static int getMaxPosition(int[] positions) {
        int maxPosition = Integer.MIN_VALUE;
        for (int position : positions) {
            maxPosition = Math.max(maxPosition, position);
        }
        return maxPosition;
    }


}
