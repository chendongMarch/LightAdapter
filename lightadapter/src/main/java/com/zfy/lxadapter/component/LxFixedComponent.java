package com.zfy.lxadapter.component;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.zfy.lxadapter.Lx;
import com.zfy.lxadapter.LxAdapter;
import com.zfy.lxadapter.decoration.FixedItemDecoration;

/**
 * CreateAt : 2019-09-02
 * Describe :
 *
 * @author chendong
 */
public class LxFixedComponent extends LxComponent {

    private ViewGroup actualViewContainer;
    @Lx.FixedMode private int fixedMode;

    public LxFixedComponent(ViewGroup viewGroup) {
        actualViewContainer = viewGroup;
        fixedMode = Lx.FIXED_USE_VIEW;
    }

    public LxFixedComponent() {
        fixedMode = Lx.FIXED_USE_DRAW;
    }

    @Override
    public void onAttachedToRecyclerView(LxAdapter adapter, @NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(adapter, recyclerView);
        FixedItemDecoration fixedItemDecoration = new FixedItemDecoration();
        fixedItemDecoration.setUseActualView(fixedMode == Lx.FIXED_USE_VIEW);
        fixedItemDecoration.setUseDrawDecor(fixedMode == Lx.FIXED_USE_DRAW);
        if (fixedMode == Lx.FIXED_USE_VIEW) {
            fixedItemDecoration.setOnFixedViewAttachListener(view -> {
                if (view == null) {
                    actualViewContainer.removeAllViews();
                    return;
                }
                if (actualViewContainer.indexOfChild(view) >= 0) {
                    return;
                }
                actualViewContainer.addView(view);
                if (actualViewContainer.getChildCount() >= 3) {
                    actualViewContainer.removeViewAt(0);
                }
            });
        }
        recyclerView.addItemDecoration(fixedItemDecoration);
    }
}
