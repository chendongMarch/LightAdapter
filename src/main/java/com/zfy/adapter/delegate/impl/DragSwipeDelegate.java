package com.zfy.adapter.delegate.impl;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;

import com.zfy.adapter.LightHolder;
import com.zfy.adapter.listener.BindCallback;
import com.zfy.adapter.model.DragSwipeOptions;
import com.zfy.adapter.model.DragSwipeState;
import com.zfy.adapter.model.ModelType;

import java.util.Collections;

/**
 * CreateAt : 2018/11/7
 * Describe :
 *
 * @author chendong
 */
public class DragSwipeDelegate extends BaseDelegate {

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

    public void setDragSwipeCallback(BindCallback<DragSwipeState> dragSwipeCallback) {
        mDragSwipeCallback = dragSwipeCallback;
    }

    public void setOptions(DragSwipeOptions options) {
        mOptions = options;
    }

    public void dragOnLongPress(View view, LightHolder holder) {
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startDrag(holder);
                return true;
            }
        });
    }

    public void dragOnTouch(View view, LightHolder holder) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    startDrag(holder);
                }
                return false;
            }
        });
    }

    public void swipeOnLongPress(View view, LightHolder holder) {
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startSwipe(holder);
                return true;
            }
        });
    }

    public void swipeOnTouch(View view, LightHolder holder) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    startSwipe(holder);
                }
                return false;
            }
        });
    }


    public void startDrag(LightHolder holder) {
        if (mItemTouchHelper == null) {
            return;
        }
        mItemTouchHelper.startDrag(holder);
    }


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
            return mOptions.itemViewSwipeEnable;
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
                int modelIndex = mAdapter.toModelIndex(viewHolder.getAdapterPosition());
                mDragSwipeCallback.bind(lightHolder, modelIndex, mDragSwipeState);
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
                int modelIndex = mAdapter.toModelIndex(lightHolder.getAdapterPosition());
                mDragSwipeCallback.bind(lightHolder, modelIndex, mDragSwipeState);
            }
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = mOptions.dragFlags;
            int swipeFlags = mOptions.swipeFlags;
            ModelType type = mAdapter.getType(viewHolder.getItemViewType());
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
