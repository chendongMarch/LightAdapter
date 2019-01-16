package com.zfy.adapter.extend.decoration;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.LightHolder;
import com.zfy.adapter.model.ModelType;

/**
 * CreateAt : 2018/11/8
 * Describe :
 * 头部悬停效果实现
 *
 * 原理：
 * 是在滑动时在列表顶部绘制一个  ItemDecoration，他会采集划出屏幕的需要悬停的那个 View
 * 然后绘制在 Canvas 上面
 *
 * @see com.zfy.adapter.delegate.impl.SectionDelegate
 *
 * @author chendong
 */
public class PinItemDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        if (!(parent.getAdapter() instanceof LightAdapter) || parent.getChildCount() <= 0) {
            return;
        }
        LightAdapter adapter = (LightAdapter) parent.getAdapter();
        View firstView = parent.getChildAt(0);
        int firstPosition = parent.getChildAdapterPosition(firstView);
        int lastPinPosition = getLastPinPosition(firstPosition, adapter);
        if (lastPinPosition < 0) {
            return;
        }
        // 根据查找到的那个创建 view，用来绘制
        LightHolder pinHolder = adapter.onCreateViewHolder(parent, adapter.getItemViewType(lastPinPosition));
        adapter.onBindViewHolder(pinHolder, lastPinPosition);
        View pinView = pinHolder.itemView;
        measurePinView(pinView, parent);
        // 计算两个 enablePin 的 view 推动的偏移，下面的那个推动上面那个，需要找到第二个的距离
        int pinOffset = 0;
        View secondPinView = null;
        for (int index = 0; index < parent.getChildCount(); index++) {
            int position = parent.getChildAdapterPosition(parent.getChildAt(index));
            ModelType type = adapter.getModelType(adapter.getItemViewType(position));
            if (type.enablePin) {
                View sectionView = parent.getChildAt(index);
                if (!firstView.equals(sectionView)) {
                    secondPinView = sectionView;
                    break;
                }
            }
        }
        if (secondPinView != null) {
            int sectionTop = secondPinView.getTop();
            int pinViewHeight = pinView.getHeight();
            int offset = sectionTop - pinViewHeight;
            if (offset < 0) {
                pinOffset = offset;
            }
        }
        int saveCount = c.save();
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) pinView.getLayoutParams();
        int leftMargin = 0;
        if (layoutParams != null) {
            leftMargin = layoutParams.leftMargin;
        }
        c.translate(leftMargin, pinOffset);
        c.clipRect(0, 0, parent.getWidth(), pinView.getMeasuredHeight());
        pinView.draw(c);
        c.restoreToCount(saveCount);
        //(0, 0, parent.getWidth(), pinView.getMeasuredHeight() + sectionPinOffset);
    }


    // 根据第一个显示的位置，反向向上查找需要固定显示的那一个
    private int getLastPinPosition(int adapterFirstVisible, LightAdapter adapter) {
        for (int index = adapterFirstVisible; index >= 0; index--) {
            ModelType type = adapter.getModelType(adapter.getItemViewType(index));
            if (type.enablePin) {
                return index;
            }
        }
        return -1;
    }

    // 测量 enablePin view，用来绘制
    private void measurePinView(View pinView, RecyclerView recyclerView) {
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