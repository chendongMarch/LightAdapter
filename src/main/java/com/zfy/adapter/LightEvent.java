package com.zfy.adapter;

import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.LightHolder;
import com.zfy.adapter.listener.OnItemListener;
import com.zfy.adapter.listener.SimpleItemListener;
import com.zfy.adapter.model.ModelType;

/**
 * CreateAt : 2018/11/2
 * Describe :
 *
 * @author chendong
 */
public class LightEvent<D> {

    private LightAdapter<D> mAdapter;
    private OnItemListener mOnItemListener;

    public LightEvent(LightAdapter<D> adapter) {
        mAdapter = adapter;
    }

    public void initEvent(LightHolder holder, ModelType modelType) {
        if (modelType.isEnableDbClick()) {
            setMultiEvent(holder, modelType);
        } else {
            setSimpleEvent(holder, modelType);
        }
    }

    public void setOnItemListener(final OnItemListener<D> onItemListener) {
        mOnItemListener = new SimpleItemListener<D>() {
            @Override
            public void onClick(int pos, LightHolder holder, D data) {
                int position = mAdapter.toModelIndex(holder.getAdapterPosition());
                D item = mAdapter.getItem(position);
                ModelType type = mAdapter.getType(item);
                if (type != null && type.isEnableClick()) {
                    onItemListener.onClick(position, holder, item);
                }
            }

            @Override
            public void onLongPress(int pos, LightHolder holder, D data) {
                int position = mAdapter.toModelIndex(holder.getAdapterPosition());
                D item = mAdapter.getItem(position);
                ModelType type = mAdapter.getType(item);
                if (type != null && type.isEnableLongPress()) {
                    onItemListener.onLongPress(position, holder, item);
                }
            }

            @Override
            public void onDoubleClick(int pos, LightHolder holder, D data) {
                int position = mAdapter.toModelIndex(holder.getAdapterPosition());
                D item = mAdapter.getItem(position);
                ModelType type = mAdapter.getType(item);
                if (type != null && type.isEnableDbClick()) {
                    onItemListener.onDoubleClick(position, holder, item);
                }
            }
        };
    }


    @SuppressWarnings("unchecked")
    private void setMultiEvent(final LightHolder holder, ModelType modelType) {
        if (!modelType.isEnableDbClick()) {
            return;
        }
        View itemView = holder.getItemView();
        GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (mOnItemListener != null) {
                    mOnItemListener.onClick(0, holder, null);
                }
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return super.onSingleTapUp(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (mOnItemListener != null) {
                    mOnItemListener.onDoubleClick(0, holder, null);
                }
                return super.onDoubleTap(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                if (mOnItemListener != null) {
                    mOnItemListener.onLongPress(0, holder, null);
                }
            }
        };
        final GestureDetectorCompat gestureDetector = new GestureDetectorCompat(mAdapter.getContext(), gestureListener);
        itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mOnItemListener != null && modelType.isEnableDbClick()) {
                    gestureDetector.onTouchEvent(motionEvent);
                    return true;
                } else {
                    return false;
                }
            }
        });

        itemView.setOnClickListener(null);
        itemView.setOnLongClickListener(null);
    }


    @SuppressWarnings("unchecked")
    private void setSimpleEvent(LightHolder holder, ModelType modelType) {
        View itemView = holder.getItemView();
        if (modelType.isEnableClick()) {
            itemView.setOnClickListener(view -> {
                if (mOnItemListener != null) {
                    mOnItemListener.onClick(0, holder, null);
                }
            });
        }
        if (modelType.isEnableLongPress()) {
            itemView.setOnLongClickListener(view -> {
                if (mOnItemListener != null) {
                    mOnItemListener.onLongPress(0, holder, null);
                }
                return true;
            });
        }
    }
}
