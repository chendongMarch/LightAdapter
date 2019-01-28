package com.zfy.adapter.common;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * CreateAt : 2018/10/29
 * Describe :
 *
 * @author chendong
 */
public class LightUtils {

    /**
     * @return how to find view holder
     */
    public static RecyclerView.ViewHolder findViewHolder() {
//        RecyclerView recyclerView = mAdapter.getView();
//        View childViewUnder = recyclerView.findChildViewUnder(0, 0);
//        RecyclerView.ViewHolder holder = recyclerView.findViewHolderForLayoutPosition(1);
//        RecyclerView.ViewHolder holder = recyclerView.findViewHolderForItemId();
//        RecyclerView.ViewHolder holder = recyclerView.findContainingViewHolder();
//        RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder()
        return null;
    }

    /**
     * @param type type
     * @return 是不是内建类型
     */
    public static boolean isBuildInType(int type) {
        return type == ItemType.TYPE_HEADER
                || type == ItemType.TYPE_FOOTER
                || type == ItemType.TYPE_CONTENT
                || type == ItemType.TYPE_LOADING
                || type == ItemType.TYPE_EMPTY;
    }

    /**
     * 加载 View
     *
     * @param context  ctx
     * @param layoutId 资源 id
     * @param group view
     * @return View
     */
    public static View inflateView(Context context, ViewGroup group, int layoutId) {
        View inflate = LayoutInflater.from(context).inflate(layoutId, group, false);
        return inflate;
    }



    /**
     * 加载 View
     *
     * @param context  ctx
     * @param layoutId 资源 id
     * @return View
     */
    public static View inflateView(Context context, int layoutId) {
        View inflate = LayoutInflater.from(context).inflate(layoutId, null);
        return inflate;
    }

    public static int getSpanSize(int spanSize, int spanCount) {
        if (spanSize == SpanSize.NONE) {
            return 1;
        }
        if (spanSize > 0) {
            return spanSize;
        }
        if (spanSize == SpanSize.SPAN_SIZE_ALL) {
            spanSize = spanCount;
        } else if (spanSize == SpanSize.SPAN_SIZE_HALF && spanCount % 2 == 0) {
            spanSize = spanCount / 2;
        } else if (spanSize == SpanSize.SPAN_SIZE_THIRD && spanCount % 3 == 0) {
            spanSize = spanCount / 3;
        } else if (spanSize == SpanSize.SPAN_SIZE_QUATER && spanCount % 4 == 0) {
            spanSize = spanCount / 4;
        }
        if (spanSize == SpanSize.SPAN_SIZE_HALF && spanCount % 2 != 0
                || spanSize == SpanSize.SPAN_SIZE_THIRD && spanCount % 3 != 0
                || spanSize == SpanSize.SPAN_SIZE_QUATER && spanCount % 4 != 0) {
            throw new AdapterException("SpanCount Set Error");
        }
        return spanSize;
    }


    /**
     * 获取 RecyclerView SpanCount
     *
     * @param view Rv
     * @return span count
     */
    public static int getRecyclerViewSpanCount(RecyclerView view) {
        RecyclerView.LayoutManager layoutManager = view.getLayoutManager();
        if (layoutManager == null) {
            return LightValues.NONE;
        }
        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        } else {
            return 1;
        }
    }
    /**
     * 获取 RecyclerView 防线，默认垂直
     *
     * @param view RecyclerView
     * @return 方向
     */
    public static int getRecyclerViewOrientation(RecyclerView view) {
        int orientation = LinearLayout.VERTICAL;
        if (view == null) {
            return orientation;
        }
        RecyclerView.LayoutManager layoutManager = view.getLayoutManager();
        if (layoutManager == null) {
            throw new AdapterException(AdapterException.LAYOUT_MANAGER_NOT_SET);
        }
        if (layoutManager instanceof LinearLayoutManager) {
            orientation = ((LinearLayoutManager) layoutManager).getOrientation();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
        }
        return orientation;
    }


    /**
     * 创建容器 View
     *
     * @param context      ctx
     * @param recyclerView RecyclerView 用来决定容器的方向
     * @return 容器 View
     */
    public static LinearLayout createWrapContentLinearContainerView(Context context, RecyclerView recyclerView) {
        LinearLayout container = new LinearLayout(context);
        int orientation = getRecyclerViewOrientation(recyclerView);
        if (orientation == LinearLayout.VERTICAL) {
            container.setOrientation(LinearLayout.VERTICAL);
            container.setLayoutParams(new RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        } else {
            container.setOrientation(LinearLayout.HORIZONTAL);
            container.setLayoutParams(new RecyclerView.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
        }
        return container;
    }


    /**
     * 创建一个占满全屏的 FrameLayout
     *
     * @param context ctx
     * @return 占满全屏的 FrameLayout
     */
    public static FrameLayout createMatchParentFrameContainer(Context context) {
        FrameLayout frameLayout = new FrameLayout(context);
        final RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT);
        frameLayout.setLayoutParams(layoutParams);
        return frameLayout;
    }

    /**
     * 获取最后一条展示的位置
     *
     * @param view RecyclerView
     * @return 最后一个位置 pos
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

    // 在一堆位置中获取最大的获得最大的位置
    private static int getMaxPosition(int[] positions) {
        int maxPosition = Integer.MIN_VALUE;
        for (int position : positions) {
            maxPosition = Math.max(maxPosition, position);
        }
        return maxPosition;
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

}
