package com.zfy.lxadapter.component;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.zfy.lxadapter.LxAdapter;
import com.zfy.lxadapter.LxViewHolder;

import java.util.List;

/**
 * CreateAt : 2019-09-01
 * Describe :
 *
 * @author chendong
 */
public class LxComponent {

    protected LxAdapter    adapter;
    protected RecyclerView view;

    public void onAttachedToAdapter(LxAdapter adapter) {
        this.adapter = adapter;
    }

    public void onAttachedToRecyclerView(LxAdapter adapter, @NonNull RecyclerView recyclerView) {
        this.view = recyclerView;
    }

    public void onBindViewHolder(LxAdapter adapter, @NonNull LxViewHolder holder, int position, @NonNull List<Object> payloads) {

    }

    public void onDataUpdate(LxAdapter adapter) {

    }

}
