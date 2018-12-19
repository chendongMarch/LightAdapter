package com.zfy.adapter;

import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.zfy.adapter.model.Extra;
import com.zfy.adapter.model.ModelType;

/**
 * CreateAt : 2018/11/2
 * Describe :
 *
 * @author chendong
 */
public class LightEvent<D> {

    public interface EventDispatcher<D> {
        void onEventDispatch(int eventType,LightHolder holder, Extra extra, D data);
    }

    public static final int TYPE_ITEM_CLICK       = 0;
    public static final int TYPE_ITEM_LONG_PRESS  = 1;
    public static final int TYPE_ITEM_DB_CLICK    = 2;
    public static final int TYPE_CHILD_CLICK      = 3;
    public static final int TYPE_CHILD_LONG_PRESS = 4;

    private LightAdapter<D>    mAdapter;
    private EventDispatcher<D> mDispatcher;


    public LightEvent(LightAdapter<D> adapter, EventDispatcher<D> dispatcher) {
        mAdapter = adapter;
        mDispatcher = dispatcher;
    }

    public void initEvent(LightHolder holder, ModelType modelType) {
        if (modelType.enableDbClick) {
            setMultiEvent(holder, modelType);
        } else {
            setSimpleEvent(holder, modelType);
        }
    }

    @SuppressWarnings("unchecked")
    private void setMultiEvent(final LightHolder holder, ModelType modelType) {
        if (!modelType.enableDbClick) {
            return;
        }
        View itemView = holder.getItemView();
        GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                tryCallClickEvent(holder);
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return super.onSingleTapUp(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                tryCallDbClickEvent(holder);
                return super.onDoubleTap(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                tryCallLongPressEvent(holder);
            }
        };
        final GestureDetectorCompat gestureDetector = new GestureDetectorCompat(mAdapter.getContext(), gestureListener);
        itemView.setClickable(true);
        itemView.setOnClickListener(null);
        itemView.setOnLongClickListener(null);
        itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // 将事件发送给 view 显示触摸状态，但是不会回调事件监听
                itemView.onTouchEvent(motionEvent);
                gestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });

        itemView.setOnClickListener(null);
        itemView.setOnLongClickListener(null);
    }

    @SuppressWarnings("unchecked")
    private void setSimpleEvent(LightHolder holder, ModelType modelType) {
        View itemView = holder.getItemView();
        if (modelType.enableClick) {
            itemView.setOnClickListener(view -> {
                tryCallClickEvent(holder);
            });
        }
        if (modelType.enableLongPress) {
            itemView.setOnLongClickListener(view -> {
                tryCallLongPressEvent(holder);
                return true;
            });
        }
    }

    public void setChildViewClickListener(LightHolder holder, View view) {
        if (view.getId() == View.NO_ID) {
            throw new IllegalArgumentException("View.getId() is NO_ID;");
        }
        view.setOnClickListener(v -> {
            Extra extra = makeExtra(holder);
            extra.viewId = v.getId();
            D item = mAdapter.getItem(extra.modelIndex);
            mDispatcher.onEventDispatch(TYPE_CHILD_CLICK, holder, extra, item);
        });
    }

    public void setChildViewLongPressListener(LightHolder holder, View view) {
        if (view.getId() == View.NO_ID) {
            throw new IllegalArgumentException("View.getId() is NO_ID;");
        }
        view.setOnLongClickListener(v -> {
            Extra extra = makeExtra(holder);
            extra.viewId = v.getId();
            D item = mAdapter.getItem(extra.modelIndex);
            mDispatcher.onEventDispatch(TYPE_CHILD_LONG_PRESS, holder, extra, item);
            return true;
        });
    }

    private Extra makeExtra(LightHolder holder) {
        return mAdapter.obtainExtraByLayoutIndex(holder.getAdapterPosition());
    }

    // 点击事件
    private void tryCallClickEvent(LightHolder holder) {
        Extra extra = makeExtra(holder);
        D item = mAdapter.getItem(extra.modelIndex);
        ModelType type = mAdapter.getModelType(item);
        if (type != null && type.enableClick) {
            mDispatcher.onEventDispatch(TYPE_ITEM_CLICK, holder, extra, item);
        }
    }

    // 长按事件
    private void tryCallLongPressEvent(LightHolder holder) {
        Extra extra = makeExtra(holder);
        D item = mAdapter.getItem(extra.modelIndex);
        ModelType type = mAdapter.getModelType(item);
        if (type != null && type.enableLongPress) {
            mDispatcher.onEventDispatch(TYPE_ITEM_LONG_PRESS, holder, extra, item);
        }
    }

    // 双击事件
    private void tryCallDbClickEvent(LightHolder holder) {
        Extra extra = makeExtra(holder);
        D item = mAdapter.getItem(extra.modelIndex);
        ModelType type = mAdapter.getModelType(item);
        if (type != null && type.enableDbClick) {
            mDispatcher.onEventDispatch(TYPE_ITEM_DB_CLICK, holder, extra, item);
        }
    }
}
