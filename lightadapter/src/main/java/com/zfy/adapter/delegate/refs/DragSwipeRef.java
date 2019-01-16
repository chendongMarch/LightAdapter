package com.zfy.adapter.delegate.refs;

import android.view.View;

import com.zfy.adapter.LightHolder;
import com.zfy.adapter.callback.BindCallback;
import com.zfy.adapter.model.DragSwipeOptions;
import com.zfy.adapter.model.DragSwipeState;

/**
 * CreateAt : 2018/11/10
 * Describe :
 *
 * @author chendong
 */
public interface DragSwipeRef {

    /**
     * {@inheritDoc}
     * 设置 drag swipe 状态改变监听
     *
     * @param dragSwipeCallback 状态改变监听,共有4种状态
     * @see DragSwipeState#ACTIVE_DRAG 开始 drag
     * @see DragSwipeState#ACTIVE_SWIPE 开始 swipe
     * @see DragSwipeState#RELEASE_DRAG 结束 drag
     * @see DragSwipeState#RELEASE_SWIPE 结束 swipe
     */
    void setDragSwipeCallback(BindCallback<DragSwipeState> dragSwipeCallback);


    /**
     * {@inheritDoc}
     * 设置 drag 和 swipe 的配置
     *
     * @param options 配置
     * @see DragSwipeOptions
     */
    void setOptions(DragSwipeOptions options);

    /**
     * {@inheritDoc}
     * 给指定的 view 设置长按拖动事件
     *
     * @param view   设置事件的 view
     * @param holder 对应的 holder
     * @see LightHolder#dragOnLongPress(int...)
     */
    void dragOnLongPress(View view, LightHolder holder);

    /**
     * {@inheritDoc}
     * 给指定的 view 设置触摸拖动事件
     *
     * @param view   设置事件的 view
     * @param holder 对应的 holder
     * @see LightHolder#dragOnTouch(int...) (int...)
     */
    void dragOnTouch(View view, LightHolder holder);

    /**
     * {@inheritDoc}
     * 给指定的 view 设置长按侧滑事件
     *
     * @param view   设置事件的 view
     * @param holder 对应的 holder
     * @see LightHolder#swipeOnLongPress(int...)
     */
    void swipeOnLongPress(View view, LightHolder holder);

    /**
     * {@inheritDoc}
     * 给指定的 view 设置触摸侧滑事件
     *
     * @param view   设置事件的 view
     * @param holder 对应的 holder
     * @see LightHolder#swipeOnTouch(int...) (int...) (int...)
     */
    void swipeOnTouch(View view, LightHolder holder);

    /**
     * 触发开始拖动事件
     *
     * @param holder 对应的 holder
     */
    void startDrag(LightHolder holder);

    /**
     * 触发开始侧滑事件
     *
     * @param holder 对应的 holder
     */
    void startSwipe(LightHolder holder);
}
