package com.zfy.adapter.delegate.impl;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.Toast;

import com.zfy.adapter.model.ModelType;

import java.util.Collections;

/**
 * CreateAt : 2018/11/7
 * Describe :
 *
 * @author chendong
 */
public class DragSwipeDelegate extends BaseDelegate {

    private ItemTouchHelper mItemTouchHelper;
    private ItemTouchCallbackImpl mItemTouchCallback;
    private DragSwipeOptions mOptions;

    public static class DragSwipeOptions {

        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;

        public DragSwipeOptions() {
        }

        public DragSwipeOptions(int dragFlags, int swipeFlags) {
            this.dragFlags = dragFlags;
            this.swipeFlags = swipeFlags;
        }
    }

    @Override
    public int getKey() {
        return DRAG_SWIPE;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (mItemTouchHelper == null) {
            mItemTouchCallback = new ItemTouchCallbackImpl();
            mItemTouchHelper = new ItemTouchHelper(mItemTouchCallback);
            mItemTouchHelper.attachToRecyclerView(recyclerView);
        }
    }

    public void setOptions(DragSwipeOptions options) {
        mOptions = options;
    }

    class ItemTouchCallbackImpl extends ItemTouchHelper.Callback {

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            switch (actionState) {
                case ItemTouchHelper.ACTION_STATE_DRAG:
                    Toast.makeText(mView.getContext(), "开始拖拽", Toast.LENGTH_SHORT).show();
                    break;
                case ItemTouchHelper.ACTION_STATE_SWIPE:
                    Toast.makeText(mView.getContext(), "开始滑动", Toast.LENGTH_SHORT).show();
                    break;
                case ItemTouchHelper.ACTION_STATE_IDLE:
                    Toast.makeText(mView.getContext(), "停止", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            Log.e("chendong", "clear view");
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
