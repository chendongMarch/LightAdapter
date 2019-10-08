package com.zfy.lxadapter.component;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewParent;

import com.zfy.lxadapter.Lx;
import com.zfy.lxadapter.LxAdapter;
import com.zfy.lxadapter.data.LxModel;
import com.zfy.lxadapter.decoration.LxSlidingSelectLayout;

import java.util.List;

/**
 * CreateAt : 2019-09-02
 * Describe :
 *
 * @author chendong
 */
public class LxSelectComponent extends LxComponent {

    public interface SelectInterceptor {
        boolean intercept(LxModel data, boolean toSelect);
    }

    @Lx.SelectMode private int               selectMode;
    private                SelectInterceptor interceptor;
    private                LxAdapter         adapter;

    public LxSelectComponent(@Lx.SelectMode int mode) {
        this(mode, null);
    }

    public LxSelectComponent(@Lx.SelectMode int mode, SelectInterceptor interceptor) {
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

    private void doSelect(LxModel model) {
        if (selectMode == Lx.SELECT_SINGLE) {
            unSelectOther(model);
        }
        if (model.isSelected()) {
            return;
        }
        if (interceptor != null && interceptor.intercept(model, true)) {
            return;
        }
        model.setSelected(true);
        int modelIndex = adapter.getData().indexOf(model);
        adapter.notifyItemChanged(modelIndex);
    }

    private void unSelect(LxModel model) {
        if (!model.isSelected()) {
            return;
        }
        if (interceptor != null && interceptor.intercept(model, false)) {
            return;
        }
        model.setSelected(false);
        int modelIndex = adapter.getData().indexOf(model);
        adapter.notifyItemChanged(modelIndex);
    }

    private void toggleSelectItem(LxModel model) {
        if (model.isSelected()) {
            unSelect(model);
        } else {
            doSelect(model);
        }
    }

    public void select(LxModel model) {
        if (selectMode == Lx.SELECT_SINGLE) {
            doSelect(model);
        } else {
            toggleSelectItem(model);
        }
    }

    public <D> List<D> getResult() {
        return adapter.getData().filterTo(LxModel::isSelected, LxModel::unpack);
    }
}
