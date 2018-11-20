package com.zfy.adapter.delegate.impl;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;

import com.zfy.adapter.LightHolder;
import com.zfy.adapter.delegate.refs.DragSwipeRef;
import com.zfy.adapter.listener.BindCallback;
import com.zfy.adapter.model.DragSwipeOptions;
import com.zfy.adapter.model.DragSwipeState;
import com.zfy.adapter.model.ModelType;
import com.zfy.adapter.model.Position;

import java.util.Collections;

/**
 * CreateAt : 2018/11/7
 * Describe :
 *
 * @author chendong
 */
public class DragSwipeDelegate extends BaseDelegate implements DragSwipeRef {

    public static final int TAG_DRAG = 100;
    public static final int TAG_SWIPE = 101;

    private ItemTouchHelper mItemTouchHelper;
    private DragSwipeOptions mOptions;
    private DragSwipeState mDragSwipeState;
    private BindCallback<DragSwipeState> mDragSwipeCallback;

    public DragSwipeDelegate() {
        mDragSwipeState = new DragSwipeState();
    }

    @Override
    public int getKey() {
        return DRAG_SWIPE;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (mItemTouchHelper == null) {
            mItemTouchHelper = new ItemTouchHelper(new ItemTouchCallbackImpl());
            mItemTouchHelper.attachToRecyclerView(recyclerView);
        }
    }

    @Override
    public void setOptions(DragSwipeOptions options) {
        mOptions = options;
    }

    @Override
    public void setDragSwipeCallback(BindCallback<DragSwipeState> dragSwipeCallback) {
        mDragSwipeCallback = dragSwipeCallback;
    }

    @Override
    public void dragOnLongPress(View view, LightHolder holder) {
        ModelType modelType = mAdapter.getModelType(holder.getItemViewType());
        if (modelType != null) {
            modelType.enableDrag = true;
        }
        view.setOnLongClickListener(v -> {
            startDrag(holder);
            return true;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void dragOnTouch(View view, LightHolder holder) {
        ModelType modelType = mAdapter.getModelType(holder.getItemViewType());
        if (modelType != null) {
            modelType.enableDrag = true;
        }
        view.setOnTouchListener((v, event) -> {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                startDrag(holder);
            }
            return false;
        });
    }

    @Override
    public void swipeOnLongPress(View view, LightHolder holder) {
        ModelType modelType = mAdapter.getModelType(holder.getItemViewType());
        if (modelType != null) {
            modelType.enableSwipe = true;
        }
        view.setOnLongClickListener(v -> {
            startSwipe(holder);
            return true;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void swipeOnTouch(View view, LightHolder holder) {
        ModelType modelType = mAdapter.getModelType(holder.getItemViewType());
        if (modelType != null) {
            modelType.enableSwipe = true;
        }
        view.setOnTouchListener((v, event) -> {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                startSwipe(holder);
            }
            return false;
        });
    }

    @Override
    public void startDrag(LightHolder holder) {
        if (mItemTouchHelper == null) {
            return;
        }
        mItemTouchHelper.startDrag(holder);
    }

    @Override
    public void startSwipe(LightHolder holder) {
        if (mItemTouchHelper == null) {
            return;
        }
        mItemTouchHelper.startSwipe(holder);
    }

    class ItemTouchCallbackImpl extends ItemTouchHelper.Callback {

        @Override
        public boolean isLongPressDragEnabled() {
            return mOptions.itemViewLongPressDragEnable;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return mOptions.itemViewAutoSwipeEnable;
        }

        @Override
        public float getMoveThreshold(RecyclerView.ViewHolder viewHolder) {
            return mOptions.moveThreshold;
        }

        @Override
        public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
            return mOptions.swipeThreshold;
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            if (viewHolder == null || !(viewHolder instanceof LightHolder)) {
                return;
            }
            LightHolder lightHolder = (LightHolder) viewHolder;
            switch (actionState) {
                case ItemTouchHelper.ACTION_STATE_DRAG:
                    lightHolder.setExtra(TAG_DRAG);
                    mDragSwipeState.state = DragSwipeState.ACTIVE_DRAG;
                    break;
                case ItemTouchHelper.ACTION_STATE_SWIPE:
                    lightHolder.setExtra(TAG_SWIPE);
                    mDragSwipeState.state = DragSwipeState.ACTIVE_SWIPE;
                    break;
            }
            if (mDragSwipeCallback != null) {
                Position position = mAdapter.obtainPositionByLayoutIndex(viewHolder.getAdapterPosition());
                mDragSwipeCallback.bind(lightHolder, position, mDragSwipeState);
            }
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            if (!(viewHolder instanceof LightHolder)) {
                return;
            }
            LightHolder lightHolder = (LightHolder) viewHolder;
            switch (lightHolder.getExtra()) {
                case TAG_DRAG:
                    mDragSwipeState.state = DragSwipeState.RELEASE_DRAG;
                    break;
                case TAG_SWIPE:
                    mDragSwipeState.state = DragSwipeState.RELEASE_SWIPE;
                    break;
            }
            if (mDragSwipeCallback != null) {
                Position position = mAdapter.obtainPositionByLayoutIndex(viewHolder.getAdapterPosition());
                mDragSwipeCallback.bind(lightHolder, position, mDragSwipeState);
            }
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = mOptions.dragFlags;
            int swipeFlags = mOptions.swipeFlags;
            ModelType type = mAdapter.getModelType(viewHolder.getItemViewType());
            if (!type.enableDrag) {
                dragFlags = 0;
            }
            if (!type.enableSwipe) {
                swipeFlags = 0;
            }
            return makeMovementFlags(dragFlags, swipeFlags);
        }


        // drag 开始移动
        @Override
        public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
            super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            int fromDataPosition = mAdapter.toModelIndex(fromPosition);
            int toDataPosition = mAdapter.toModelIndex(toPosition);
            if (fromDataPosition < toDataPosition) {
                for (int i = fromDataPosition; i < toDataPosition; i++) {
                    Collections.swap(mAdapter.getDatas(), i, i + 1);
                }
            } else {
                for (int i = fromDataPosition; i > toDataPosition; i--) {
                    Collections.swap(mAdapter.getDatas(), i, i - 1);
                }
            }
            mAdapter.notifyItemMoved(fromPosition, toPosition);
        }

        // drag 是否可以移动
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source,
                RecyclerView.ViewHolder target) {
            return source.getItemViewType() == target.getItemViewType();
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            mAdapter.getDatas().remove(mAdapter.toModelIndex(position));
            mAdapter.notifyItem().remove(position);
        }
    }
}
