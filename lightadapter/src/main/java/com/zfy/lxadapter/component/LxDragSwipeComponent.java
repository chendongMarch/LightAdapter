package com.zfy.lxadapter.component;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;

import com.zfy.lxadapter.Lx;
import com.zfy.lxadapter.LxAdapter;
import com.zfy.lxadapter.LxViewHolder;
import com.zfy.lxadapter.R;
import com.zfy.lxadapter.data.LxContext;
import com.zfy.lxadapter.data.TypeOpts;

import java.util.Collections;

/**
 * CreateAt : 2019-09-01
 * Describe :
 *
 * @author chendong
 */
public class LxDragSwipeComponent extends LxComponent {

    private static final int TAG_DRAG  = 100;
    private static final int TAG_SWIPE = 101;

    public static class DragSwipeOptions {

        public int     dragFlags; // 拖动方向
        public int     swipeFlags; // 滑动方向
        public boolean longPressItemView4Drag = true; // 长按自动触发拖拽
        public boolean touchItemView4Swipe    = true; // 滑动自动触发滑动
        public float   moveThreshold          = .5f; // 超过 0.5 触发 onMoved
        public float   swipeThreshold         = .5f; // 超过 0.5 触发 onSwipe

        public DragSwipeOptions() {
            this(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END,
                    ItemTouchHelper.START | ItemTouchHelper.END);
        }

        public DragSwipeOptions(int dragFlags, int swipeFlags) {
            this.dragFlags = dragFlags;
            this.swipeFlags = swipeFlags;
        }
    }

    public interface OnDragSwipeStateChangeListener {
        void stateChange(@Lx.DragSwipeState int state, LxViewHolder holder, LxContext context);
    }

    private ItemTouchHelper                itemTouchHelper;
    private DragSwipeOptions               options;
    private OnDragSwipeStateChangeListener onDragSwipeStateChangeListener;

    private LxAdapter adapter;

    public LxDragSwipeComponent() {
        this(new DragSwipeOptions(), null);
    }

    public LxDragSwipeComponent(OnDragSwipeStateChangeListener listener) {
        this(new DragSwipeOptions(), listener);
    }

    public LxDragSwipeComponent(DragSwipeOptions options) {
        this(options, null);
    }

    public LxDragSwipeComponent(DragSwipeOptions options, OnDragSwipeStateChangeListener listener) {
        this.options = options;
        this.onDragSwipeStateChangeListener = listener;
    }

    @Override
    public void onAttachedToRecyclerView(LxAdapter adapter, @NonNull RecyclerView recyclerView) {
        if (itemTouchHelper == null) {
            itemTouchHelper = new ItemTouchHelper(new ItemTouchCallbackImpl());
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }
        this.adapter = adapter;
    }


    public void dragOnLongPress(View view, LxViewHolder holder) {
        TypeOpts typeOpts = adapter.getTypeOpts(holder.getViewType());
        if (typeOpts != null) {
            typeOpts.enableDrag = true;
        }
        view.setOnLongClickListener(v -> {
            startDrag(holder);
            return true;
        });
    }

    public void dragOnTouch(View view, LxViewHolder holder) {
        TypeOpts typeOpts = adapter.getTypeOpts(holder.getViewType());
        if (typeOpts != null) {
            typeOpts.enableDrag = true;
        }
        view.setOnClickListener(null);
        view.setOnLongClickListener(null);
        view.setOnTouchListener((v, event) -> {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                startDrag(holder);
            }
            v.performClick();
            return false;
        });
    }

    public void swipeOnLongPress(View view, LxViewHolder holder) {
        TypeOpts typeOpts = adapter.getTypeOpts(holder.getViewType());
        if (typeOpts != null) {
            typeOpts.enableSwipe = true;
        }
        view.setOnLongClickListener(v -> {
            startSwipe(holder);
            return true;
        });
    }

    public void swipeOnTouch(View view, LxViewHolder holder) {
        TypeOpts typeOpts = adapter.getTypeOpts(holder.getViewType());
        if (typeOpts != null) {
            typeOpts.enableSwipe = true;
        }
        view.setOnClickListener(null);
        view.setOnLongClickListener(null);
        view.setOnTouchListener((v, event) -> {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                startSwipe(holder);
            }
            v.performClick();
            return false;
        });
    }

    public void startDrag(LxViewHolder holder) {
        if (itemTouchHelper == null) {
            return;
        }
        itemTouchHelper.startDrag(holder);
    }

    public void startSwipe(LxViewHolder holder) {
        if (itemTouchHelper == null) {
            return;
        }
        itemTouchHelper.startSwipe(holder);
    }

    class ItemTouchCallbackImpl extends ItemTouchHelper.Callback {

        @Override
        public boolean isLongPressDragEnabled() {
            return options.longPressItemView4Drag;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return options.touchItemView4Swipe;
        }

        @Override
        public float getMoveThreshold(RecyclerView.ViewHolder viewHolder) {
            return options.moveThreshold;
        }

        @Override
        public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
            return options.swipeThreshold;
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            if (!(viewHolder instanceof LxViewHolder)) {
                return;
            }
            @Lx.DragSwipeState int state = actionState == ItemTouchHelper.ACTION_STATE_DRAG ? Lx.DragState.NONE : Lx.SwipeState.NONE;
            LxViewHolder holder = (LxViewHolder) viewHolder;
            switch (actionState) {
                case ItemTouchHelper.ACTION_STATE_DRAG:
                    holder.itemView.setTag(R.id.drag_swipe_state, TAG_DRAG);
                    state = Lx.DragState.ACTIVE;
                    break;
                case ItemTouchHelper.ACTION_STATE_SWIPE:
                    holder.itemView.setTag(R.id.drag_swipe_state, TAG_SWIPE);
                    state = Lx.SwipeState.ACTIVE;
                    break;
            }
            if (onDragSwipeStateChangeListener != null) {
                onDragSwipeStateChangeListener.stateChange(state, holder, holder.getLxContext());
            }
        }

        @Override
        public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            if (!(viewHolder instanceof LxViewHolder)) {
                return;
            }
            LxViewHolder holder = (LxViewHolder) viewHolder;
            Object tag = holder.itemView.getTag(R.id.drag_swipe_state);
            if (!(tag instanceof Integer)) {
                return;
            }
            int actionState = (int) tag;
            int state = actionState == TAG_DRAG ? Lx.DragState.NONE : Lx.SwipeState.NONE;
            switch (actionState) {
                case TAG_DRAG:
                    state = Lx.DragState.RELEASE;
                    break;
                case TAG_SWIPE:
                    state = Lx.SwipeState.RELEASE;
                    break;
            }
            if (onDragSwipeStateChangeListener != null) {
                onDragSwipeStateChangeListener.stateChange(state, holder, holder.getLxContext());
            }
        }

        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            LxViewHolder lxVh = (LxViewHolder) viewHolder;
            int dragFlags = options.dragFlags;
            int swipeFlags = options.swipeFlags;
            TypeOpts typeOpts = adapter.getTypeOpts(lxVh.getViewType());
            if (!typeOpts.enableDrag) {
                dragFlags = 0;
            }
            if (!typeOpts.enableSwipe) {
                swipeFlags = 0;
            }
            return makeMovementFlags(dragFlags, swipeFlags);
        }


        // drag 开始移动
        @Override
        public void onMoved(@NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            int fromPos,
                            @NonNull RecyclerView.ViewHolder target,
                            int toPos,
                            int x,
                            int y) {
            super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(adapter.getData(), i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(adapter.getData(), i, i - 1);
                }
            }
            adapter.notifyItemMoved(fromPosition, toPosition);
        }

        // drag 是否可以移动
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder source,
                              @NonNull RecyclerView.ViewHolder target) {
            LxViewHolder sourceHolder = (LxViewHolder) source;
            LxViewHolder targetHolder = (LxViewHolder) target;
            return sourceHolder.getViewType() == targetHolder.getViewType();
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            adapter.getData().remove(position);
            adapter.notifyItemRemoved(position);
        }
    }
}
