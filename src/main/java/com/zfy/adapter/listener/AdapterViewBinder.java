package com.zfy.adapter.listener;

import com.zfy.adapter.LightHolder;

/**
 * CreateAt : 2018/2/3
 * Describe :
 *
 * @author chendong
 */
public interface AdapterViewBinder<D> {
    void onBindViewHolder(LightHolder holder, D data, int pos, int type);
}
