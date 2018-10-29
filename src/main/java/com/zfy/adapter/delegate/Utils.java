package com.zfy.adapter.delegate;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * CreateAt : 2018/10/29
 * Describe :
 *
 * @author chendong
 */
public class Utils {


    public static View inflateView(Context context, int layoutId) {
        View inflate = LayoutInflater.from(context).inflate(layoutId, null);
        return inflate;
    }


    public static int getRecyclerViewOrientation(RecyclerView view) {
        int orientation = LinearLayout.VERTICAL;
        RecyclerView.LayoutManager layoutManager = view.getLayoutManager();
        if (layoutManager == null) {
            throw new AdapterException(AdapterException.LAYOUT_MANAGER_NOT_SET);
        }
        if (layoutManager instanceof LinearLayoutManager) {
            orientation = ((LinearLayoutManager) layoutManager).getOrientation();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
        }
        return orientation;
    }
}
