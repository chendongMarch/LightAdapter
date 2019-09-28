package com.zfy.lxadapter.helper;

import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.zfy.lxadapter.Lx;
import com.zfy.lxadapter.LxViewHolder;
import com.zfy.lxadapter.listener.OnItemEventListener;

/**
 * CreateAt : 2019-08-31
 * Describe : 支持单击、双击、长按事件
 *
 * @author chendong
 */
public class LxEvent {

    private static void setClickEvent(LxViewHolder holder, OnItemEventListener listener) {
        holder.itemView.setOnClickListener(v -> listener.onEvent(holder.getLxContext(), Lx.EVENT_CLICK));
    }

    private static void setLongPressEvent(LxViewHolder holder, OnItemEventListener listener) {
        holder.itemView.setOnLongClickListener(v -> {
            listener.onEvent(holder.getLxContext(), Lx.EVENT_LONG_PRESS);
            return true;
        });
    }

    private static void setDoubleClickEvent(LxViewHolder holder, boolean setClick, boolean setLongPress, OnItemEventListener listener) {
        View view = holder.itemView;
        GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                // 单击事件
                if (setClick) {
                    listener.onEvent(holder.getLxContext(), Lx.EVENT_CLICK);
                }
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return super.onSingleTapUp(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                // 双击事件
                listener.onEvent(holder.getLxContext(), Lx.EVENT_DOUBLE_CLICK);
                return super.onDoubleTap(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                // 长按事件
                if (setLongPress) {
                    listener.onEvent(holder.getLxContext(), Lx.EVENT_LONG_PRESS);
                }
            }
        };
        GestureDetectorCompat detector = new GestureDetectorCompat(view.getContext(), gestureListener);
        view.setClickable(true);
        view.setOnClickListener(null);
        view.setOnLongClickListener(null);
        view.setOnTouchListener((v, motionEvent) -> {
            // 将事件发送给 view 显示触摸状态，但是不会回调事件监听
            v.onTouchEvent(motionEvent);
            detector.onTouchEvent(motionEvent);
            v.performClick();
            return true;
        });
    }


    public static void setEvent(LxViewHolder holder, boolean setClick, boolean setLongPress, boolean setDoubleClick, OnItemEventListener listener) {
        if (setDoubleClick) {
            setDoubleClickEvent(holder, setClick, setLongPress, listener);
        } else {
            if (setClick) {
                setClickEvent(holder, listener);
            }
            if (setLongPress) {
                setLongPressEvent(holder, listener);
            }
        }
    }

    public static void setFocusEvent(LxViewHolder holder, OnItemEventListener listener) {
        holder.itemView.setFocusable(true);
        holder.itemView.setFocusableInTouchMode(true);
        holder.itemView.setOnFocusChangeListener((v, hasFocus) -> {
            listener.onEvent(holder.getLxContext(), hasFocus ? Lx.EVENT_FOCUS_ATTACH : Lx.EVENT_FOCUS_DETACH);
            listener.onEvent(holder.getLxContext(), Lx.EVENT_FOCUS_CHANGE);
        });
    }
}
