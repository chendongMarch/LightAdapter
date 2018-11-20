package com.zfy.adapter;

import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.zfy.adapter.listener.EventCallback;
import com.zfy.adapter.model.ModelType;
import com.zfy.adapter.model.Position;

/**
 * CreateAt : 2018/11/2
 * Describe :
 *
 * @author chendong
 */
public class LightEvent<D> {

    public interface EventSetting<D> {

        void setClickEvent(EventCallback<D> clickCallback);

        void setLongPressEvent(EventCallback<D> longPressCallback);

        void setDbClickEvent(EventCallback<D> dbClickCallback);
    }

    private LightAdapter<D> mAdapter;

    private EventCallback<D> mClickCallback;
    private EventCallback<D> mLongPressCallback;
    private EventCallback<D> mDbClickCallback;

    public LightEvent(LightAdapter<D> adapter) {
        mAdapter = adapter;
    }

    public void initEvent(LightHolder holder, ModelType modelType) {
        if (modelType.enableDbClick) {
            setMultiEvent(holder, modelType);
        } else {
            setSimpleEvent(holder, modelType);
        }
    }

    public void setClickCallback(EventCallback<D> clickCallback) {
        mClickCallback = clickCallback;
    }

    public void setLongPressCallback(EventCallback<D> longPressCallback) {
        mLongPressCallback = longPressCallback;
    }

    public void setDbClickCallback(EventCallback<D> dbClickCallback) {
        mDbClickCallback = dbClickCallback;
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
                if (mClickCallback != null) {
                    tryCallClickEvent(holder);
                }
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return super.onSingleTapUp(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (mDbClickCallback != null) {
                    tryCallDbClickEvent(holder);
                }
                return super.onDoubleTap(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                if (mLongPressCallback != null) {
                    tryCallLongPressEvent(holder);
                }
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
                if (mClickCallback != null) {
                    tryCallClickEvent(holder);
                }
            });
        }
        if (modelType.enableLongPress) {
            itemView.setOnLongClickListener(view -> {
                if (mLongPressCallback != null) {
                    tryCallLongPressEvent(holder);
                }
                return true;
            });
        }
    }

    private Position makePosition(LightHolder holder) {
        return mAdapter.obtainPositionByLayoutIndex(holder.getAdapterPosition());
    }

    // 点击事件
    private void tryCallClickEvent(LightHolder holder) {
        Position position = makePosition(holder);
        D item = mAdapter.getItem(position.modelIndex);
        ModelType type = mAdapter.getModelType(item);
        if (type != null && type.enableClick) {
            mClickCallback.call(holder, position, item);
        }
    }

    // 长按事件
    private void tryCallLongPressEvent(LightHolder holder) {
        Position position = makePosition(holder);
        D item = mAdapter.getItem(position.modelIndex);
        ModelType type = mAdapter.getModelType(item);
        if (type != null && type.enableLongPress) {
            mLongPressCallback.call(holder, position, item);
        }
    }

    // 双击事件
    private void tryCallDbClickEvent(LightHolder holder) {
        Position position = makePosition(holder);
        D item = mAdapter.getItem(position.modelIndex);
        ModelType type = mAdapter.getModelType(item);
        if (type != null && type.enableDbClick) {
            mDbClickCallback.call(holder, position, item);
        }
    }
}
