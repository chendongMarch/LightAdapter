package com.zfy.lxadapter.component;

import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;

import com.zfy.lxadapter.LxAdapter;

/**
 * CreateAt : 2019-09-02
 * Describe :
 *
 * @author chendong
 */
public class LxItemAnimatorComponent extends LxComponent {

    private RecyclerView.ItemAnimator itemAnimator;
    private RecyclerView              view;

    public LxItemAnimatorComponent(RecyclerView.ItemAnimator itemAnimator) {
        this.itemAnimator = itemAnimator;
    }

    @Override
    public void onAttachedToRecyclerView(LxAdapter adapter, @NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(adapter, recyclerView);
        view = recyclerView;
        view.setItemAnimator(itemAnimator);
    }

    public void setEnableAnimator(boolean enable) {
        if (enable) {
            if (view != null) {
                view.setItemAnimator(itemAnimator);
            }
        } else {
            if (view != null) {
                DefaultItemAnimator animator = new DefaultItemAnimator();
                animator.setSupportsChangeAnimations(false);
                view.setItemAnimator(animator);
            }
        }
    }

}
