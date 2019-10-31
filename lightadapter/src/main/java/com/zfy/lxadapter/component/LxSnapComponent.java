package com.zfy.lxadapter.component;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;

import com.zfy.lxadapter.Lx;
import com.zfy.lxadapter.LxAdapter;
import com.zfy.lxadapter.helper.LxScroller;
import com.zfy.lxadapter.helper.LxUtil;

/**
 * CreateAt : 2019-09-02
 * Describe :
 *
 * @author chendong
 */
public class LxSnapComponent extends LxComponent {

    private static final int UN_SELECT = -1;

    private int                  lastSelectPosition = UN_SELECT;
    private int                  snapMode;
    private boolean              enableListenerDispatch;
    private OnPageChangeListener onPageChangeListener;

    public LxSnapComponent(@Lx.SnapModeDef int snapMode) {
        this.snapMode = snapMode;
    }

    public LxSnapComponent(@Lx.SnapModeDef int snapMode, OnPageChangeListener onPageChangeListener) {
        this.snapMode = snapMode;
        this.onPageChangeListener = onPageChangeListener;
    }

    public interface OnPageChangeListener {

//        default void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        }

        default void onPageSelected(int lastPosition, int position) {
        }

        default void onPageScrollStateChanged(int state) {
        }
    }


    @Override
    public void onAttachedToRecyclerView(LxAdapter adapter, @NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(adapter, recyclerView);

        SnapHelper snapHelper;
        if (snapMode == Lx.SnapMode.LINEAR) {
            snapHelper = new LinearSnapHelper();
        } else {
            snapHelper = new PagerSnapHelper();
        }
        snapHelper.attachToRecyclerView(recyclerView);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!enableListenerDispatch) {
                    return;
                }
                if (onPageChangeListener == null) {
                    return;
                }
                onPageChangeListener.onPageScrollStateChanged(newState);
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    return;
                }
                int firstPosition = LxUtil.findFirstCompletelyVisibleItemPosition(recyclerView);
                int lastPosition = LxUtil.findLastCompletelyVisibleItemPosition(recyclerView);
                int position = (firstPosition + lastPosition) / 2;

                if (position >= 0 && lastSelectPosition != position) {
                    onPageChangeListener.onPageSelected(lastSelectPosition, position);
                    lastSelectPosition = position;
                }
            }
        });
    }


    /**
     * 选中某一项
     *
     * @param position 需要选中的位置
     * @param smooth   是否平滑滚动
     */
    public void selectItem(int position, boolean smooth) {
        if (lastSelectPosition == position) {
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageSelected(lastSelectPosition, position);
            }
        } else {
            enableListenerDispatch = false;
            if (smooth) {
                LxScroller.scrollToPositionOnCenter(view, position);
            } else {
                LxScroller.scrollToPositionOnCenter(view, position);
            }
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageSelected(lastSelectPosition, position);
            }
            lastSelectPosition = position;
            enableListenerDispatch = true;
        }
    }

    private void scrollToPosition(RecyclerView recyclerView, int pos) {
        LinearSmoothScroller smoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }

            @Override
            protected int getHorizontalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };
        smoothScroller.setTargetPosition(pos);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager != null) {
            layoutManager.startSmoothScroll(smoothScroller);
        }
    }

}
