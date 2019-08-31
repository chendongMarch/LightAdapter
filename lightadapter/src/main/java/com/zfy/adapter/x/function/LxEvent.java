package com.zfy.adapter.x.function;

import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.zfy.adapter.R;
import com.zfy.adapter.x.listener.OnItemEventListener;
import com.zfy.adapter.x.Lx;
import com.zfy.adapter.x.LxContext;
import com.zfy.adapter.x.LxVh;

/**
 * CreateAt : 2019-08-31
 * Describe : 支持单击、双击、长按事件
 *
 * @author chendong
 */
public class LxEvent {

    public static void setClickEvent(LxVh holder, OnItemEventListener listener) {
        holder.itemView.setOnClickListener(v -> {
            LxContext ctx = (LxContext) v.getTag(R.id.item_context);
            listener.onEvent(ctx, Lx.EVENT_CLICK);
        });
    }

    public static void setLongPressEvent(LxVh holder, OnItemEventListener listener) {
        holder.itemView.setOnLongClickListener(v -> {
            LxContext ctx = (LxContext) v.getTag(R.id.item_context);
            listener.onEvent(ctx, Lx.EVENT_LONG_PRESS);
            return true;
        });
    }

    public static void setDoubleClickEvent(LxVh holder, boolean setClick, boolean setLongPress, OnItemEventListener listener) {
        View view = holder.itemView;
        GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                // 单击事件
                if (setClick) {
                    LxContext ctx = (LxContext) view.getTag(R.id.item_context);
                    listener.onEvent(ctx, Lx.EVENT_CLICK);
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
                LxContext ctx = (LxContext) view.getTag(R.id.item_context);
                listener.onEvent(ctx, Lx.EVENT_DOUBLE_CLICK);
                return super.onDoubleTap(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                // 长按事件
                if (setLongPress) {
                    LxContext ctx = (LxContext) view.getTag(R.id.item_context);
                    listener.onEvent(ctx, Lx.EVENT_LONG_PRESS);
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


    public static void setEvent(LxVh holder, boolean setClick, boolean setLongPress, boolean setDoubleClick, OnItemEventListener listener) {
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
}
