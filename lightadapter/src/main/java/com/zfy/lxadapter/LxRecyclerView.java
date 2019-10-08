package com.zfy.lxadapter;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;

import com.zfy.lxadapter.helper.LxUtil;

/**
 * CreateAt : 2019-10-05
 * Describe :
 *
 * @author chendong
 */
public class LxRecyclerView extends RecyclerView {

    public LxRecyclerView(@NonNull Context context) {
        super(context);
        init();
    }

    public LxRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LxRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public static final int EDGE_START   = 0;
    public static final int EDGE_END     = 1;
    public static final int EDGE_UNKNOWN = 2;

    public interface OnEdgeDragCallback {

        void onUpdate();

        void onAttach(LxRecyclerView view);

        void onDrag(float distance, int edge);

        void onRelease(int edge);

        void onActive(int edge, int velocityY);

    }

    public void init() {
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        setCallback(new OnEdgeDragCallbackScaleImpl(1.2f));
    }

    private int     touchSlop; // 触摸边界
    private int     activePointerId; // 触摸点 ID
    private float   initialDownValue; // 落下的位置
    private boolean isBeingDragged; // 是否触发拖拽
    private float   distance; // 拖拽距离
    private boolean vertical;

    private OnEdgeDragCallback callback;

    public void setCallback(OnEdgeDragCallback callback) {
        this.callback = callback;
        this.callback.onAttach(this);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        if (callback == null || !isEnabled() || (!isScrollToStart() && !isScrollToEnd())) {
            return super.onInterceptTouchEvent(event);
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                activePointerId = event.getPointerId(0);
                isBeingDragged = false;
                float initialMotionY = getMotionEventValue(event);
                if (initialMotionY == -1) {
                    return super.onInterceptTouchEvent(event);
                }
                initialDownValue = initialMotionY;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (activePointerId == MotionEvent.INVALID_POINTER_ID) {
                    return super.onInterceptTouchEvent(event);
                }
                final float y = getMotionEventValue(event);
                if (y == -1f) {
                    return super.onInterceptTouchEvent(event);
                }
                if (isScrollToStart() && !isScrollToEnd()) {
                    // 在顶部不在底部
                    float yDiff = y - initialDownValue;
                    if (yDiff > touchSlop && !isBeingDragged) {
                        isBeingDragged = true;
                    }
                } else if (!isScrollToStart() && isScrollToEnd()) {
                    // 在底部不在顶部
                    float yDiff = initialDownValue - y;
                    if (yDiff > touchSlop && !isBeingDragged) {
                        isBeingDragged = true;
                    }
                } else if (isScrollToStart() && isScrollToEnd()) {
                    // 在底部也在顶部
                    float yDiff = y - initialDownValue;
                    if (Math.abs(yDiff) > touchSlop && !isBeingDragged) {
                        isBeingDragged = true;
                    }
                } else {
                    // 不在底部也不在顶部
                    return super.onInterceptTouchEvent(event);
                }
                break;
            }
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                activePointerId = MotionEvent.INVALID_POINTER_ID;
                isBeingDragged = false;
                break;
        }
        return isBeingDragged || super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionMasked = event.getActionMasked();
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                activePointerId = event.getPointerId(0);
                isBeingDragged = false;
                break;
            case MotionEvent.ACTION_MOVE: {
                float value = getMotionEventValue(event);
                float distance = value - initialDownValue;
                this.distance = distance;
                if (isScrollToStart() && !isScrollToEnd()) {
                    // 在顶部不在底部
                    if (distance < 0) {
                        return super.onTouchEvent(event);
                    }
                    callback.onDrag(this.distance, EDGE_START);
                    return true;
                } else if (!isScrollToStart() && isScrollToEnd()) {
                    // 在底部不在顶部
                    if (distance > 0) {
                        return super.onTouchEvent(event);
                    }
                    callback.onDrag(-this.distance, EDGE_END);
                    return true;
                } else if (isScrollToStart() && isScrollToEnd()) {
                    // 在底部也在顶部
                    if (distance > 0) {
                        callback.onDrag(this.distance, EDGE_START);
                    } else {
                        callback.onDrag(-this.distance, EDGE_END);
                    }
                    return true;
                } else {
                    // 不在底部也不在顶部
                    return super.onTouchEvent(event);
                }
            }
            case MotionEvent.ACTION_POINTER_DOWN:
                activePointerId = event.getPointerId(event.getActionIndex());
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (isScrollToStart() && !isScrollToEnd()) {
                    callback.onRelease(EDGE_START);
                } else if (!isScrollToStart() && isScrollToEnd()) {
                    callback.onRelease(EDGE_END);
                } else if (isScrollToStart() && isScrollToEnd()) {
                    if (distance > 0) {
                        callback.onRelease(EDGE_START);
                    } else {
                        callback.onRelease(EDGE_END);
                    }
                } else {
                    return super.onTouchEvent(event);
                }
                break;
            }
        }
        return super.onTouchEvent(event);
    }

    private boolean isScrollToStart() {
        return vertical ? !canScrollVertically(-1) : !canScrollHorizontally(-1);
    }

    private boolean isScrollToEnd() {
        return vertical ? !canScrollVertically(1) : !canScrollHorizontally(1);
    }

    private float getMotionEventValue(MotionEvent event) {
        int index = event.findPointerIndex(activePointerId);
        return index < 0 ? -1f : (vertical ? event.getRawY() : event.getRawX());
    }

    private void onSecondaryPointerUp(MotionEvent event) {
        final int pointerIndex = event.getActionIndex();
        final int pointerId = event.getPointerId(pointerIndex);
        if (pointerId == activePointerId) {
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            activePointerId = event.getPointerId(newPointerIndex);
        }
    }


    @Override
    public void setLayoutManager(@Nullable LayoutManager layout) {
        super.setLayoutManager(layout);
        vertical = LxUtil.getRecyclerViewOrientation(this) == LinearLayoutManager.VERTICAL;
        callback.onUpdate();
    }

    private int calcEdge(int edge) {
        if (isScrollToStart() && !isScrollToEnd()) {
            return EDGE_START;
        } else if (!isScrollToStart() && isScrollToEnd()) {
            return EDGE_END;
        } else if (isScrollToStart() && isScrollToEnd()) {
            return edge;
        }
        return EDGE_UNKNOWN;
    }

//    @Override
//    void absorbGlows(int velocityX, int velocityY) {
//        super.absorbGlows(velocityX, velocityY);
//        Log.e("chendong", "过度滚动了 " + velocityY);
//        postDelayed(() -> {
//            callback.onActive(velocityY > 0 ? EDGE_END : EDGE_START, velocityY);
//        },500);
//    }


    public static abstract class OnEdgeDragCallbackImpl implements OnEdgeDragCallback {

        protected int            totalHeight;
        protected LxRecyclerView attachView;
        protected ValueAnimator  restoreAnimator;
        protected ValueAnimator  activeAnimator;
        protected boolean        vertical;

        @Override
        public void onAttach(LxRecyclerView view) {
            attachView = view;
            totalHeight = attachView.getResources().getDisplayMetrics().heightPixels;
        }

        @Override
        public void onUpdate() {
            if (attachView.getLayoutManager() != null) {
                vertical = LxUtil.getRecyclerViewOrientation(attachView) == LinearLayoutManager.VERTICAL;
            }
        }

        public abstract void pull(float value);

        public abstract void push(float value);

        protected void animateRestore(float initValue, int edge) {
            if (restoreAnimator == null) {
                restoreAnimator = ValueAnimator.ofFloat();
                restoreAnimator.setDuration(300);
                restoreAnimator.setInterpolator(new DecelerateInterpolator(2f));

                restoreAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        attachView.setEnabled(false);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        attachView.setEnabled(true);
                        restoreAnimator.removeListener(this);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        attachView.setEnabled(true);
                        restoreAnimator.removeListener(this);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
            restoreAnimator.removeAllUpdateListeners();
            restoreAnimator.addUpdateListener(animation -> {
                float value = (float) animation.getAnimatedValue();
                if (edge == EDGE_START) {
                    pull(value);
                } else {
                    push(value);
                }
            });
            restoreAnimator.cancel();
            restoreAnimator.setFloatValues(initValue, 1f);
            restoreAnimator.setCurrentPlayTime(0);
            restoreAnimator.start();
        }

    }

    public static class OnEdgeDragCallbackScaleImpl extends OnEdgeDragCallbackImpl {

        private float maxScale;
        private float scale;

        public OnEdgeDragCallbackScaleImpl(float maxScale) {
            this.maxScale = maxScale;
        }

        @Override
        public void onDrag(float distance, int edge) {
            scale = calculateRate(distance);
            switch (edge) {
                case EDGE_START:
                    pull(scale);
                    break;
                case EDGE_END:
                    push(scale);
                    break;
                default:
            }
        }


        @Override
        public void onRelease(int edge) {
            animateRestore(scale, edge);
        }

        @Override
        public void onActive(int edge, int velocityY) {
            animateActive(edge);
        }

        private float calculateRate(float distance) {
            return 1f + ((distance * 1f / totalHeight) * (maxScale - 1));
        }

        private void animateActive(int edge) {
            float tempDistance = totalHeight / 3;
            if (activeAnimator == null) {
                activeAnimator = ValueAnimator.ofFloat();
                activeAnimator.setDuration(800);
                activeAnimator.setInterpolator(new DecelerateInterpolator(2f));
                activeAnimator.addUpdateListener(animation -> {
                    float value = (float) animation.getAnimatedValue();
                    onDrag(value, edge);
                });
                activeAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        attachView.setEnabled(false);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        attachView.setEnabled(true);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        attachView.setEnabled(true);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
            activeAnimator.cancel();
            activeAnimator.setFloatValues(0f, tempDistance, 0f);
            activeAnimator.setCurrentPlayTime(0);
            activeAnimator.start();
        }

        @Override
        public void pull(float scale) {
            if (vertical) {
                attachView.setPivotY(0);
                attachView.setScaleY(scale);
            } else {
                attachView.setPivotX(0);
                attachView.setScaleX(scale);
            }
        }

        @Override
        public void push(float scale) {
            if (vertical) {
                attachView.setPivotY(attachView.getHeight());
                attachView.setScaleY(scale);
            } else {
                attachView.setPivotX(attachView.getWidth());
                attachView.setScaleX(scale);
            }
        }

    }


    public static class OnEdgeDragCallbackTranslateImpl extends OnEdgeDragCallbackImpl {

        private float distance;
        private Rect  originRect;
        private float distanceRatio;

        public OnEdgeDragCallbackTranslateImpl(float distanceRatio) {
            this.distanceRatio = distanceRatio;
            originRect = new Rect();
        }


        public void onDrag(float distance, int edge) {
            if (originRect.isEmpty()) {
                initOriginRect();
            }
            this.distance = distance * distanceRatio;
            switch (edge) {
                case EDGE_START:
                    pull(this.distance);
                    break;
                case EDGE_END:
                    push(this.distance);
                    break;
                default:
            }
        }

        @Override
        public void onAttach(LxRecyclerView view) {
            super.onAttach(view);
            initOriginRect();
        }

        private void initOriginRect() {
            originRect.top = attachView.getTop();
            originRect.left = attachView.getLeft();
            originRect.right = attachView.getRight();
            originRect.bottom = attachView.getBottom();
        }

        @Override
        public void onRelease(int edge) {
            animateRestore(this.distance, edge);
        }

        @Override
        public void onActive(int edge, int velocityY) {
            animateActive(edge, velocityY);
        }

        private void animateActive(int edge, int velocityY) {
            float tempDistance = totalHeight / 3 * velocityY * 1f / 20000;
            if (activeAnimator == null) {
                activeAnimator = ValueAnimator.ofFloat();
                activeAnimator.setDuration(800);
                activeAnimator.setInterpolator(new DecelerateInterpolator(2f));
                activeAnimator.addUpdateListener(animation -> {
                    float value = (float) animation.getAnimatedValue();
                    onDrag(value, edge);
                });
                activeAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        attachView.setEnabled(false);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        attachView.setEnabled(true);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        attachView.setEnabled(true);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
            activeAnimator.cancel();
            activeAnimator.setFloatValues(0f, tempDistance, 0f);
            activeAnimator.setCurrentPlayTime(0);
            activeAnimator.start();
        }

        @Override
        public void pull(float distance) {
            if (vertical) {
                attachView.layout(originRect.left, (int) (originRect.top + distance), originRect.right, (int) (originRect.bottom + distance));
            } else {
                attachView.layout(((int) (originRect.left + distance)), originRect.top, (int) (originRect.right + distance), originRect.bottom);
            }
        }

        @Override
        public void push(float distance) {
            if (vertical) {
                attachView.layout(originRect.left, (int) (originRect.top - distance), originRect.right, (int) (originRect.bottom - distance));
            } else {
                attachView.layout(((int) (originRect.left - distance)), originRect.top, (int) (originRect.right - distance), originRect.bottom);
            }
        }

    }
}
