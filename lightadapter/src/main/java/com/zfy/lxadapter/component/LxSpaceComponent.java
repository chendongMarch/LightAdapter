package com.zfy.lxadapter.component;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.zfy.lxadapter.LxAdapter;
import com.zfy.lxadapter.LxList;
import com.zfy.lxadapter.data.LxModel;
import com.zfy.lxadapter.data.TypeOpts;
import com.zfy.lxadapter.helper.LxSpan;
import com.zfy.lxadapter.helper.LxUtil;

/**
 * CreateAt : 2019-10-31
 * Describe :
 *
 * @author chendong
 */
public class LxSpaceComponent extends LxComponent {

    public static final String KEY_START = "KEY_START";
    public static final String KEY_END   = "KEY_END";

    private int         allLineCount = -1;
    private int         space;
    private int         spanCount;
    private SpaceOpts   opts;
    private SpaceSetter setter;

    public static class SpaceOpts {
        public boolean left;
        public boolean top;
        public boolean right;
        public boolean bottom;
        public int     spanCount;
        public int     spanSize;
        public int     startSpanPosition;
        public int     endSpanPosition;
        public int     position;
        public View    view;

        public void reset() {
            left = false;
            top = false;
            right = false;
            bottom = false;
            spanCount = -1;
            spanSize = -1;
            startSpanPosition = -1;
            endSpanPosition = -1;
            position = -1;
            view = null;
        }
    }

    public interface SpaceSetter {
        void set(LxSpaceComponent comp, Rect outRect, SpaceOpts opts);
    }

    public LxSpaceComponent(int space, SpaceSetter setter) {
        this.space = space;
        this.setter = setter;
        this.opts = new SpaceOpts();
    }

    public LxSpaceComponent(int space) {
        this(space, null);
    }

    @Override
    public void onAttachedToRecyclerView(LxAdapter adapter, @NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(adapter, recyclerView);
        recyclerView.addItemDecoration(new SpaceItemDecoration());
        spanCount = LxUtil.getRecyclerViewSpanCount(recyclerView);
        allLineCount = -1;
    }

    @Override
    public void onAttachedToAdapter(LxAdapter adapter) {
        super.onAttachedToAdapter(adapter);
        allLineCount = -1;
    }

    @Override
    public void onDataUpdate(LxAdapter adapter) {
        super.onDataUpdate(adapter);
        allLineCount = -1;
    }

    private void resetAllLineCount() {
        int moreSpan = 0;
        LxList list = adapter.getData();
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                int lastSpan = getSpanCount(adapter, list.get(i), spanCount) - 1;
                int startSpanPosition = i + moreSpan;
                int endSpanPosition = startSpanPosition + lastSpan;
                allLineCount = endSpanPosition / spanCount;
            }
            // 累加 moreSpan
            moreSpan += getSpanCount(adapter, list.get(i), spanCount) - 1;
        }
    }


    private void resetAllSpanCache() {
        Log.e("chendong", "重新计算");
        int moreSpan = 0;
        int emptySpan = 0;
        LxList list = adapter.getData();
        for (int i = 0; i < list.size(); i++) {
            int lastSpan = getSpanCount(adapter, list.get(i), spanCount) - 1;
            int startSpanPosition = i + moreSpan + emptySpan;
            int endSpanPosition = startSpanPosition + lastSpan;
            LxModel lxModel = list.get(i);
            Bundle extra = lxModel.getExtra();
            extra.putInt(KEY_START, startSpanPosition);
            extra.putInt(KEY_END, endSpanPosition);
            // 累加 moreSpan
            moreSpan += getSpanCount(adapter, list.get(i), spanCount) - 1;

            if (i != list.size() - 1) {
                LxModel nextModel = list.get(i + 1);
                int remainCount = (spanCount - 1) - (endSpanPosition + spanCount) % spanCount;
                int nextSpanCount = getSpanCount(adapter, nextModel, this.spanCount);
                if (nextSpanCount > remainCount) {
                    // 剩下了
                    emptySpan += remainCount;
                }
            }
        }
        allLineCount = list.get(list.size() - 1).getExtra().getInt(KEY_END) / spanCount;
    }


    private int getSpanCount(LxAdapter adapter, LxModel lxModel, int spanCount) {
        TypeOpts typeOpts = adapter.getTypeOpts(lxModel.getItemType());
        return LxSpan.getSpanSize(typeOpts.spanSize, spanCount);
    }


    class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.onDraw(c, parent, state);
        }

        @Override
        public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.onDrawOver(c, parent, state);
        }


        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            LxAdapter adapter = (LxAdapter) parent.getAdapter();
            if (adapter == null) {
                super.getItemOffsets(outRect, view, parent, state);
                return;
            }

            opts.reset();

            LxModel lxModel = adapter.getData().get(position);
            Bundle extra = lxModel.getExtra();

            if (allLineCount < 0 || !extra.containsKey(KEY_START) || !extra.containsKey(KEY_END)) {
                resetAllSpanCache();
            }

            opts.startSpanPosition = extra.getInt(KEY_START);
            opts.endSpanPosition = extra.getInt(KEY_END);

            opts.left = opts.startSpanPosition % spanCount == 0;
            opts.right = opts.endSpanPosition % spanCount == spanCount - 1;
            opts.top = opts.startSpanPosition / spanCount == 0;
            opts.bottom = opts.endSpanPosition / spanCount == allLineCount;

            int left, top, right, bottom;
            left = top = right = bottom = space / 2;

            int edgeSpace = space;
            if (opts.top) {
                top = edgeSpace;
            }
            if (opts.bottom) {
                bottom = edgeSpace;
            }
            if (opts.left) {
                left = edgeSpace;
            }
            if (opts.right) {
                right = edgeSpace;
            }

            int orientation = LxUtil.getRecyclerViewOrientation(parent);
            if (orientation == RecyclerView.HORIZONTAL) {
                outRect.set(top, left, bottom, right);
            } else {
                outRect.set(left, top, right, bottom);
            }


            if (setter != null) {
                opts.spanCount = spanCount;
                opts.spanSize = LxSpan.getSpanSize(adapter.getTypeOpts(lxModel.getItemType()).spanSize, spanCount);
                opts.view = view;
                opts.position = position;
                setter.set(LxSpaceComponent.this, outRect, opts);
                opts.reset();
            }
        }
    }
}
