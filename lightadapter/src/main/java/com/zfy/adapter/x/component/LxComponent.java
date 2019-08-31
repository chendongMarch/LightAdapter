package com.zfy.adapter.x.component;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.zfy.adapter.x.LxAdapter;
import com.zfy.adapter.x.LxVh;

import java.util.List;

/**
 * CreateAt : 2019-09-01
 * Describe :
 *
 * @author chendong
 */
public class LxComponent {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void onAttachedToRecyclerView(LxAdapter adapter, @NonNull RecyclerView recyclerView) {

    }

    public void onBindViewHolder(LxAdapter adapter, @NonNull LxVh holder, int position, @NonNull List<Object> payloads) {

    }

}
