package com.zfy.adapter.extend;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.common.LightUtil;
import com.zfy.adapter.delegate.impl.SelectorDelegate;
import com.zfy.adapter.delegate.refs.SelectorRef;
import com.zfy.adapter.function._Consumer;

/**
 * CreateAt : 2016/9/12
 * Describe : 滑动选中
 *
 * @author chendong
 */
public class SlidingSelectLayout extends FrameLayout {

    public static final String TAG = SlidingSelectLayout.class.getSimpleName();

    private static final float TOUCH_SLOP_RATE = 0.15f;// 初始化值
    private static final int   INVALID_PARAM   = -1;

    public SlidingSelectLayout(Context context) {
        this(context, null);
    }

    public SlidingSelectLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mSpanCount = INVALID_PARAM;
        mPrePublishPos = INVALID_PARAM;
    }

    private LightAdapter mAdapter;

    private float        mXTouchSlop; // 横轴滑动阈值，超过阈值表示触发横轴滑动
    private float        mYTouchSlop; // 纵轴滑动阈值，超过阈值表示触发纵轴滑动
    private int          mSpanCount; // 横向的item数量
    private float        mInitialDownX; // down 事件初始值
    private float        mInitialDownY; // down 事件初始值
    private boolean      mIsSliding; // 是否正在滑动
    private int          mPrePublishPos; // 上次发布的位置，避免频繁发布

    private _Consumer<Object> onSlidingSelectListener;// 滑动选中监听

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isEnabled()) {
            return super.onInterceptTouchEvent(ev);
        }
        // 不支持多点触摸
        int pointerCount = ev.getPointerCount();
        if (pointerCount > 1) {
            return super.onInterceptTouchEvent(ev);
        }
        ensureAdapter();
        ensureSpanCount();
        if (!isReadyToIntercept())
            return super.onInterceptTouchEvent(ev);

        int action = ev.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // build
                mInitialDownX = ev.getX();
                mInitialDownY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                // stop
                mIsSliding = false;
                break;
            case MotionEvent.ACTION_MOVE:
                // handle
                float xDiff = Math.abs(ev.getX() - mInitialDownX);
                float yDiff = Math.abs(ev.getY() - mInitialDownY);
                if (yDiff < mYTouchSlop && xDiff > mXTouchSlop) {
                    mIsSliding = true;
                }
                break;
        }
        return mIsSliding;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                // re build 手指抬起时重置
                mIsSliding = false;
                mPrePublishPos = INVALID_PARAM;
                break;
            case MotionEvent.ACTION_MOVE:
                // 滑动过程中，触发监听事件
                publishSlidingCheck(ev);
                break;
        }
        return mIsSliding;
    }

    // 发布滑动的结果
    private void publishSlidingCheck(MotionEvent event) {
        if (mAdapter == null) {
            return;
        }
        if (onSlidingSelectListener == null) {
            return;
        }
        RecyclerView recyclerView = mAdapter.getView();
        View childViewUnder = recyclerView.findChildViewUnder(event.getX(), event.getY());
        if (childViewUnder == null) {
            return;
        }
        int position = recyclerView.getChildAdapterPosition(childViewUnder);
        Object data = mAdapter.getItem(mAdapter.toModelIndex(position));
        // 当前触摸的点与上一次触摸的点相同 || 没有pos || 没有数据
        if (mPrePublishPos == position || position == INVALID_PARAM || data == null) {
            return;
        }
        onSlidingSelectListener.accept(data);
        mPrePublishPos = position;
    }

    // 是否开始检测
    private boolean isReadyToIntercept() {
        return mAdapter != null && mSpanCount != INVALID_PARAM;
    }

    // 计算 spanCount
    private void ensureSpanCount() {
        if (mAdapter == null) {
            return;
        }
        if (mSpanCount != INVALID_PARAM) {
            return;
        }
        mSpanCount = LightUtil.getRecyclerViewSpanCount(mAdapter.getView());
        if (mSpanCount < 0) {
            return;
        }
        int size = (int) (getResources().getDisplayMetrics().widthPixels / (mSpanCount * 1.0f));
        mXTouchSlop = mYTouchSlop = size * TOUCH_SLOP_RATE;
    }

    // 获取 adapter
    private void ensureAdapter() {
        if (mAdapter != null) {
            return;
        }
        RecyclerView recyclerView = searchRecyclerView(this);
        if (recyclerView != null) {
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if (adapter != null && adapter instanceof LightAdapter) {
                mAdapter = (LightAdapter) adapter;
                SelectorRef selector = mAdapter.selector();
                if (selector instanceof SelectorDelegate) {
                    ((SelectorDelegate) selector).setSlidingSelectLayout(this);
                }
            }
        }
    }

    // 查找 RecyclerView
    private RecyclerView searchRecyclerView(ViewGroup viewGroup) {
        RecyclerView rv = null;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt instanceof RecyclerView) {
                rv = (RecyclerView) childAt;
                break;
            } else if (childAt instanceof ViewGroup) {
                rv = searchRecyclerView((ViewGroup) childAt);
            }
        }
        return rv;
    }

    public void setOnSlidingSelectListener(_Consumer<Object> onSlidingSelectListener) {
        this.onSlidingSelectListener = onSlidingSelectListener;
    }

    public void setXTouchSlop(float XTouchSlop) {
        mXTouchSlop = XTouchSlop;
    }
}

