package com.zfy.adapter.x.component;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.zfy.adapter.x.LxAdapter;
import com.zfy.adapter.x.function.FixedItemDecoration;

/**
 * CreateAt : 2019-09-02
 * Describe :
 *
 * @author chendong
 */
public class LxFixedComponent extends LxComponent {

    @Override
    public void onAttachedToRecyclerView(LxAdapter adapter, @NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(adapter, recyclerView);
        recyclerView.addItemDecoration(new FixedItemDecoration());
    }
}
