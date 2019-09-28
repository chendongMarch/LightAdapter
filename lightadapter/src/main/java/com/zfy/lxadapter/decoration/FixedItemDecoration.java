package com.zfy.lxadapter.decoration;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zfy.lxadapter.LxAdapter;
import com.zfy.lxadapter.LxViewHolder;
import com.zfy.lxadapter.data.TypeOpts;
import com.zfy.lxadapter.helper.LxUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateAt : 2018/11/8
 * Describe :
 * 头部悬停效果实现
 * <p>
 * 原理：
 * 是在滑动时在列表顶部绘制一个  ItemDecoration，他会采集划出屏幕的需要悬停的那个 View
 * 然后绘制在 Canvas 上面
 *
 * @author chendong
 */
public class FixedItemDecoration extends RecyclerView.ItemDecoration {

    private static final List<Object> PAY_LOADS = new ArrayList<>();

    public interface OnFixedViewAttachListener {
        void onAttach(View view);
    }

    private boolean                   useDrawDecor     = true;
    private boolean                   useActualView    = false;
    private OnFixedViewAttachListener onFixedViewAttachListener;
    private int                       lastCalcPosition = -100;
    private View                      lastFixItemView;
    private View                      lastSecondItemView;


    public void setOnFixedViewAttachListener(OnFixedViewAttachListener onFixedViewAttachListener) {
        this.onFixedViewAttachListener = onFixedViewAttachListener;
    }

    public void setUseDrawDecor(boolean useDrawDecor) {
        this.useDrawDecor = useDrawDecor;
    }

    public void setUseActualView(boolean useActualView) {
        this.useActualView = useActualView;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        if (!(parent.getAdapter() instanceof LxAdapter) || parent.getChildCount() <= 0) {
            return;
        }
        LxAdapter adapter = (LxAdapter) parent.getAdapter();
        View firstView = parent.getChildAt(0);
        int firstPosition = LxUtil.getFirstVisiblePosition(parent);
        int lastFixedPosition = getLastPinPosition(firstPosition, adapter);
        if (lastFixedPosition < 0) {
            publishActualViewAttach(null);
            return;
        }

        if (lastCalcPosition == lastFixedPosition) {
            draw(c, parent, adapter, firstView, lastFixItemView, lastSecondItemView);
            publishActualViewAttach(lastFixItemView);
            return;
        }
        // 根据查找到的那个创建 view，用来绘制
        RecyclerView.ViewHolder holder = null;
        // useActualView ? null : parent.findViewHolderForAdapterPosition(lastFixedPosition);
        if (holder == null) {
            int itemViewType = adapter.getItemViewType(lastFixedPosition);
            holder = adapter.onCreateViewHolder(parent, itemViewType);
            LxViewHolder lxVh = (LxViewHolder) holder;
            lxVh.setItemViewType(itemViewType);
            adapter.onBindViewHolder(lxVh, lastFixedPosition, PAY_LOADS);
        }
        View fixedItemView = holder.itemView;
        if (fixedItemView == null) {
            publishActualViewAttach(null);
            return;
        }
        lastFixItemView = fixedItemView;
        lastCalcPosition = lastFixedPosition;

        publishActualViewAttach(lastFixItemView);
        draw(c, parent, adapter, firstView, fixedItemView, null);
    }


    private void publishActualViewAttach(View o) {
        if (useActualView) {
            if (onFixedViewAttachListener != null) {
                onFixedViewAttachListener.onAttach(o);
            }
        }
    }

    private void draw(Canvas c, RecyclerView parent, LxAdapter adapter, View firstItemView, View fixedItemView, View secondView) {
        if (!useDrawDecor) {
            return;
        }
        measureFixedView(fixedItemView, parent);
        // 计算两个 enableFixed 的 view 推动的偏移，下面的那个推动上面那个，需要找到第二个的距离
        int pinOffset = 0;
        View secondFixedItemView = secondView;
        if (secondFixedItemView == null) {
            for (int index = 0; index < parent.getChildCount(); index++) {
                int position = parent.getChildAdapterPosition(parent.getChildAt(index));
                if (position < 0) {
                    continue;
                }
                TypeOpts type = adapter.getTypeOpts(adapter.getItemViewType(position));
                if (type.enableFixed) {
                    View sectionView = parent.getChildAt(index);
                    if (!firstItemView.equals(sectionView)) {
                        secondFixedItemView = sectionView;
                        break;
                    }
                }
            }
            lastSecondItemView = secondFixedItemView;
        }
        if (secondFixedItemView != null) {
            int sectionTop = secondFixedItemView.getTop();
            int pinViewHeight = fixedItemView.getHeight();
            int offset = sectionTop - pinViewHeight;
            if (offset < 0) {
                pinOffset = offset;
            }
        }
        actualDraw(c, parent, fixedItemView, pinOffset);
    }


    private void actualDraw(Canvas c, RecyclerView parent, View fixedItemView, int pinOffset) {
        int saveCount = c.save();
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) fixedItemView.getLayoutParams();
        int leftMargin = 0;
        if (layoutParams != null) {
            leftMargin = layoutParams.leftMargin;
        }
        c.translate(leftMargin, pinOffset);
        c.clipRect(0, 0, parent.getWidth(), fixedItemView.getMeasuredHeight());
        fixedItemView.draw(c);
        c.restoreToCount(saveCount);
    }


    // 根据第一个显示的位置，反向向上查找需要固定显示的那一个
    private int getLastPinPosition(int adapterFirstVisible, LxAdapter adapter) {
        for (int index = adapterFirstVisible; index >= 0; index--) {
            TypeOpts type = adapter.getTypeOpts(adapter.getItemViewType(index));
            if (type.enableFixed) {
                return index;
            }
        }
        return -1;
    }

    // 测量 enableFixed view，用来绘制
    private void measureFixedView(View pinView, RecyclerView recyclerView) {
        if (pinView.isLayoutRequested()) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) pinView.getLayoutParams();
            if (layoutParams == null) {
                return;
            }
            int widthSpec = View.MeasureSpec.makeMeasureSpec(
                    recyclerView.getMeasuredWidth() - layoutParams.leftMargin - layoutParams.rightMargin,
                    View.MeasureSpec.EXACTLY);
            int heightSpec;
            if (layoutParams.height > 0) {
                heightSpec = View.MeasureSpec.makeMeasureSpec(layoutParams.height, View.MeasureSpec.EXACTLY);
            } else {
                heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            }
            pinView.measure(widthSpec, heightSpec);
            pinView.layout(0, 0, pinView.getMeasuredWidth(), pinView.getMeasuredHeight());
        }
    }

}