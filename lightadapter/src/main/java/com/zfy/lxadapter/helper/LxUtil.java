package com.zfy.lxadapter.helper;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.zfy.lxadapter.BuildConfig;
import com.zfy.lxadapter.Lx;
import com.zfy.lxadapter.data.Copyable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * CreateAt : 2019-09-01
 * Describe :
 *
 * @author chendong
 */
public class LxUtil {

    /**
     * 获取 RecyclerView SpanCount
     *
     * @param view Rv
     * @return span count
     */
    public static int getRecyclerViewSpanCount(RecyclerView view) {
        RecyclerView.LayoutManager layoutManager = view.getLayoutManager();
        if (layoutManager == null) {
            return Lx.SPAN_NONE;
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
    public static int findLastVisibleItemPosition(RecyclerView view) {
        int position;
        RecyclerView.LayoutManager manager = view.getLayoutManager();
        if (manager == null) {
            return -1;
        }
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
     * 获取最后一条展示的位置
     *
     * @param view RecyclerView
     * @return 最后一个位置 position
     */
    public static int findLastCompletelyVisibleItemPosition(RecyclerView view) {
        int position;
        RecyclerView.LayoutManager manager = view.getLayoutManager();
        if (manager == null) {
            return -1;
        }
        if (manager instanceof GridLayoutManager) {
            position = ((GridLayoutManager) manager).findLastCompletelyVisibleItemPosition();
        } else if (manager instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) manager).findLastCompletelyVisibleItemPosition();
        } else if (manager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) manager;
            int[] lastPositions = layoutManager.findLastCompletelyVisibleItemPositions(new int[layoutManager.getSpanCount()]);
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
    public static int findFirstVisibleItemPosition(RecyclerView view) {
        int position;
        RecyclerView.LayoutManager manager = view.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            position = ((GridLayoutManager) manager).findFirstCompletelyVisibleItemPosition();
        } else if (manager instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) manager).findFirstVisibleItemPosition();
        } else if (manager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) manager;
            int[] lastPositions = layoutManager.findFirstVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMaxPosition(lastPositions);
        } else {
            position = -1;
        }
        return position;
    }


    /**
     * 获取第一条展示的位置
     *
     * @param view RecyclerView
     * @return 第一条展示的位置
     */
    public static int findFirstCompletelyVisibleItemPosition(RecyclerView view) {
        int position;
        RecyclerView.LayoutManager manager = view.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            position = ((GridLayoutManager) manager).findFirstCompletelyVisibleItemPosition();
        } else if (manager instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) manager).findFirstCompletelyVisibleItemPosition();
        } else if (manager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) manager;
            int[] lastPositions = layoutManager.findFirstCompletelyVisibleItemPositions(new int[layoutManager.getSpanCount()]);
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


    public static void log(String msg) {
        if (BuildConfig.DEBUG) {
            Log.e("LxAdapter", msg);
        }
    }


    // 复制一份新数据
    public static Object copy(Object input) {
        Object newOne = null;
        if (input instanceof Copyable) {
            try {
                newOne = ((Copyable) input).copyNewOne();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (input instanceof Parcelable) {
            Parcelable parcelable = (Parcelable) input;
            Parcel parcel = null;
            try {
                parcel = Parcel.obtain();
                parcel.writeParcelable(parcelable, 0);
                parcel.setDataPosition(0);
                newOne = parcel.readParcelable(input.getClass().getClassLoader());
            } finally {
                if (parcel != null) {
                    parcel.recycle();
                }
            }
        }
        if (newOne == null) {
            newOne = input;
        }
        return newOne;
    }

    // 复制一份新数据
    public static <T> T copyAddress(T input) {
        return input;
    }

    public static List<String> parsePayloads(List<Object> payloads) {
        List<String> list = new ArrayList<>();
        for (Object payload : payloads) {
            if (!(payload instanceof Set) || ((Set) payload).isEmpty()) {
                continue;
            }
            Set msgSet = (Set) payload;
            for (Object o : msgSet) {
                if (o instanceof String) {
                    list.add((String) o);
                }
            }
        }
        return list;
    }

}
