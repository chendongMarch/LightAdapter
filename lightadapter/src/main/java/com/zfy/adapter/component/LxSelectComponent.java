package com.zfy.adapter.component;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewParent;

import com.zfy.adapter.LxAdapter;
import com.zfy.adapter.data.LxModel;
import com.zfy.adapter.decoration.LxSlidingSelectLayout;

import java.util.List;

/**
 * CreateAt : 2019-09-02
 * Describe :
 *
 * @author chendong
 */
public class LxSelectComponent extends LxComponent {

    public static final int SINGLE = 1;
    public static final int MULTI  = 2;

    public interface SelectInterceptor {
        boolean intercept(LxModel data, boolean toSelect);
    }

    private int               selectMode;
    private SelectInterceptor interceptor;
    private LxAdapter         adapter;

    public LxSelectComponent(int mode) {
        this(mode, null);
    }

    public LxSelectComponent(int mode, SelectInterceptor interceptor) {
        this.selectMode = mode;
        this.interceptor = interceptor;
    }

    @Override
    public void onAttachedToRecyclerView(LxAdapter adapter, @NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(adapter, recyclerView);
        this.adapter = adapter;
        ViewParent parent = recyclerView.getParent();
        if (parent instanceof LxSlidingSelectLayout) {
            setSlidingSelectLayout((LxSlidingSelectLayout) parent);
        }
    }

    private void setSlidingSelectLayout(LxSlidingSelectLayout slidingSelectLayout) {
        slidingSelectLayout.setOnSlidingSelectListener(data -> select((LxModel) data));
    }


    // 单选时，选中一个，取消选中其他的
    private void unSelectOther(LxModel selectable) {
        List<LxModel> datas = adapter.getData();
        for (LxModel data : datas) {
            if (data.equals(selectable)) {
                continue;
            }
            if (data.isSelected()) {
                unSelect(data);
            }
        }
    }

    private void doSelect(LxModel data) {
        if (selectMode == SINGLE) {
            unSelectOther(data);
        }
        if (data.isSelected()) {
            return;
        }
        if (interceptor != null && !interceptor.intercept(data, true)) {
            return;
        }
        data.setSelected(true);
        int modelIndex = adapter.getData().indexOf(data);
        adapter.notifyItemChanged(modelIndex);
    }

    private void unSelect(LxModel data) {
        if (!data.isSelected()) {
            return;
        }
        if (interceptor != null && !interceptor.intercept(data, false)) {
            return;
        }
        data.setSelected(false);
        int modelIndex = adapter.getData().indexOf(data);
        adapter.notifyItemChanged(modelIndex);
    }

    private void toggleSelectItem(LxModel data) {
        if (data.isSelected()) {
            unSelect(data);
        } else {
            doSelect(data);
        }
    }

    public void select(LxModel data) {
        if (selectMode == SINGLE) {
            doSelect(data);
        } else {
            toggleSelectItem(data);
        }
    }

}
