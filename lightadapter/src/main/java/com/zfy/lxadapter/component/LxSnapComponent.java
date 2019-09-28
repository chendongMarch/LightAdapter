package com.zfy.lxadapter.component;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;

import com.zfy.lxadapter.Lx;
import com.zfy.lxadapter.LxAdapter;

/**
 * CreateAt : 2019-09-02
 * Describe :
 *
 * @author chendong
 */
public class LxSnapComponent extends LxComponent {

    public static final int TIME_OUT = 300;

    private long                 lastIdleTime;
    private int                  lastSelectPosition;
    private int                  snapMode;
    private OnPageChangeListener onPageChangeListener;

    public LxSnapComponent(@Lx.SnapMode int snapMode) {
        this.snapMode = snapMode;
    }

    public LxSnapComponent(@Lx.SnapMode int snapMode, OnPageChangeListener onPageChangeListener) {
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
        if (snapMode == Lx.SNAP_MODE_LINEAR) {
            snapHelper = new LinearSnapHelper();
        } else {
            snapHelper = new PagerSnapHelper();
        }
        snapHelper.attachToRecyclerView(recyclerView);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrollStateChanged(newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        long timeMillis = System.currentTimeMillis();
                        if (timeMillis - lastIdleTime > TIME_OUT) {
                            View snapView = snapHelper.findSnapView(recyclerView.getLayoutManager());
                            int childAdapterPosition = recyclerView.getChildAdapterPosition(snapView);
                            if (lastSelectPosition != childAdapterPosition) {
                                onPageChangeListener.onPageSelected(lastSelectPosition, childAdapterPosition);
                                lastIdleTime = timeMillis;
                                lastSelectPosition = childAdapterPosition;
                            }
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }
}
