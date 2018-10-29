package com.zfy.adapter.module;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zfy.adapter.LightAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateAt : 2018/2/24
 * Describe :
 *
 * @author chendong
 */
public class FullSpanModule extends BaseModule {

    private List<Integer> mFullSpanTypeList;

    public FullSpanModule() {
        addFullSpanType(LightAdapter.TYPE_HEADER, LightAdapter.TYPE_FOOTER);
    }

    public void addFullSpanType(int... types) {
        if (mFullSpanTypeList == null) {
            mFullSpanTypeList = new ArrayList<>();
        }
        for (int type : types) {
            mFullSpanTypeList.add(type);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = mAdapter.getItemViewType(position);
                    if (mFullSpanTypeList.contains(type)) {
                        return gridLayoutManager.getSpanCount();
                    } else {
                        return 1;
                    }
                }
            });
        }
    }
}
