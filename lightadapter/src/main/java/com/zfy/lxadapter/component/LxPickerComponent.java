package com.zfy.lxadapter.component;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zfy.lxadapter.Lx;
import com.zfy.lxadapter.LxAdapter;
import com.zfy.lxadapter.helper.LxScroller;
import com.zfy.lxadapter.helper.LxUtil;

/**
 * CreateAt : 2019-09-02
 * Describe : 选择器
 * <p>
 * 1. 滑动时 UI 响应
 *
 * @author chendong
 */
public class LxPickerComponent extends LxComponent {

    private static final float DEFAULT_SCALE_MAX = 1.3f;

    private LxSnapComponent            snapComponent;
    private OnPickerListener           onPickerListener;
    private Opts                       opts;
    private OnScrollListenerPickerImpl onScrollListenerPicker;

    public static class Opts {

        public  float   maxScaleValue      = DEFAULT_SCALE_MAX; // 缩放的比例
        public  int     listViewWidth; // 布局宽度
        private int     listViewHeight; // 布局高度
        public  int     itemViewHeight; // 每一项的高度
        public  int     exposeViewCount; // 暴露的个数
        public  boolean infinite           = false; // 是否无限滚动
        public  float   flingVelocityRatio = 0.1f; // 增大滑动阻尼，越小阻尼越大
        public  float   baseAlpha          = 0.6f; // 基础的 alpha 值，在这个基础上变大
        public  float   baseRotation       = 45; // 基础的角度

        // listViewHeight = itemViewHeight * exposeViewCount;

        private void init() {
            listViewHeight = itemViewHeight * exposeViewCount;
        }

        private void assertOpts() {
            if (itemViewHeight <= 0 || exposeViewCount <= 0) {
                throw new IllegalArgumentException("please set itemViewHeight and exposeViewCount!!!");
            }
        }
    }


    // 根据滑动比例做一些UI上的响应
    public interface OnPickerListener {

        void onPick(int position);

        default void onPickerScroll(View view, int position, float ratio, int dy, int middlePosition) {

        }

    }

    public LxPickerComponent(Opts opts) {
        this(opts, null);
    }

    public LxPickerComponent(Opts opts, OnPickerListener onPickerScrollListener) {
        this.onPickerListener = onPickerScrollListener;
        this.opts = opts;
        this.opts.init();
    }

    @Override
    public void onAttachedToAdapter(LxAdapter adapter) {
        super.onAttachedToAdapter(adapter);
        initSnapComponent(adapter);
    }

    private void initSnapComponent(LxAdapter adapter) {
        if (snapComponent == null) {
            LxSnapComponent lxSnapComponent = new LxSnapComponent(Lx.SNAP_MODE_LINEAR, new LxSnapComponent.OnPageChangeListener() {
                @Override
                public void onPageSelected(int lastPosition, int position) {
                    LxPickerComponent component = LxPickerComponent.this;
                    if (component.onPickerListener != null) {
                        component.onPickerListener.onPick(position);
                    }
                }
            });
            lxSnapComponent.onAttachedToAdapter(adapter);
            snapComponent = lxSnapComponent;
        }
    }

    @Override
    public void onAttachedToRecyclerView(LxAdapter adapter, @NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(adapter, recyclerView);

        // 设置宽高
        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
        if (opts.listViewWidth != 0) {
            layoutParams.width = opts.listViewWidth;
        }
        if (opts.listViewHeight != 0) {
            layoutParams.height = opts.listViewHeight;
        }
        recyclerView.setLayoutParams(layoutParams);

        // 阻尼系数
        LxScroller.setMaxFlingVelocity(recyclerView, opts.flingVelocityRatio);
        // 初始化滑动效果
        initSnapComponent(adapter);
        snapComponent.onAttachedToRecyclerView(adapter, recyclerView);
        // 滚动监听
        onScrollListenerPicker = new OnScrollListenerPickerImpl();
        recyclerView.addOnScrollListener(onScrollListenerPicker);

    }


    /**
     * @param view           当前需要响应的 view
     * @param position       当前的位置
     * @param ratio          比例 0-1
     * @param dy             滑动距离，主要用来判断防线
     * @param middlePosition 中间位置
     */
    private void onPickerScroll(View view, int position, float ratio, int dy, int middlePosition) {
        if (onPickerListener != null) {
            onPickerListener.onPickerScroll(view, position, ratio, dy, middlePosition);
        }
        // 中间位置放大
        float scaleValue = opts.maxScaleValue - 1;
        view.setScaleX(1 + scaleValue * ratio);
        view.setScaleY(1 + scaleValue * ratio);
        // 两边变淡
        view.setAlpha(opts.baseAlpha + 0.4f * ratio);
        // 两边偏转
        if (position < middlePosition) {
            view.setRotationX(opts.baseRotation * (1 - ratio));
        } else if (position == middlePosition) {
            view.setRotationX(0);
        } else if (position > middlePosition) {
            view.setRotationX(-(opts.baseRotation * (1 - ratio)));
        }
    }


    class OnScrollListenerPickerImpl extends RecyclerView.OnScrollListener {

        private int middleViewBottom;
        private int middleViewTop;

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            invalidatePicker(recyclerView, dy);
        }

        private void invalidatePicker(@NonNull RecyclerView recyclerView, int dy) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager == null) {
                return;
            }
            int firstVisiblePosition = LxUtil.findFirstVisibleItemPosition(recyclerView);
            int lastVisiblePosition = LxUtil.findLastCompletelyVisibleItemPosition(recyclerView);
            int middlePosition = (lastVisiblePosition + firstVisiblePosition) / 2;
            if (opts.listViewHeight == 0) {
                opts.listViewHeight = recyclerView.getHeight();
            }
            if (opts.itemViewHeight == 0) {
                View view = layoutManager.findViewByPosition(firstVisiblePosition);
                if (view == null) {
                    return;
                }
                opts.itemViewHeight = view.getHeight();
            }

            if (opts.itemViewHeight == 0) {
                return;
            }
            if (middleViewTop == 0) {
                int centerHeight = opts.listViewHeight / 2;
                middleViewTop = opts.listViewHeight - (centerHeight + opts.itemViewHeight / 2);
                middleViewBottom = centerHeight - opts.itemViewHeight / 2;
            }
            for (int i = firstVisiblePosition; i <= lastVisiblePosition; i++) {
                View viewByPosition = layoutManager.findViewByPosition(i);
                if (viewByPosition == null) {
                    continue;
                }
                float ratio;
                int top = viewByPosition.getTop();
                if (top < middleViewTop) {
                    ratio = top * 1f / middleViewTop;
                } else {
                    int bottom = recyclerView.getHeight() - viewByPosition.getBottom();
                    ratio = bottom * 1f / middleViewBottom;
                }
                if (ratio < 0) {
                    ratio = 0;
                }
                if (ratio > 1) {
                    ratio = 1;
                }
                onPickerScroll(viewByPosition, i, ratio, dy, middlePosition);
            }
        }
    }

    public void selectItem(int position, boolean smooth) {
        if (opts.infinite) {
            int halfExposeCount = (opts.exposeViewCount - 1) / 2;
            // 想选中的数据其实在 realPosition
            int realPosition = position;
            // 想滚动的位置其实要往前两个
            snapComponent.selectItem(realPosition, smooth);
        } else {
            int halfExposeCount = (opts.exposeViewCount - 1) / 2;
            // 想选中的数据其实在 realPosition
            int realPosition = position + halfExposeCount;
            // 想滚动的位置其实要往前两个
            snapComponent.selectItem(realPosition, smooth);
        }

        view.scrollBy(10, 10);
        view.scrollBy(-10, -10);

        onScrollListenerPicker.invalidatePicker(view, 10);

    }


    public void setOnPickerListener(OnPickerListener onPickerListener) {
        this.onPickerListener = onPickerListener;
    }

    public Opts getOpts() {
        return opts;
    }
}
