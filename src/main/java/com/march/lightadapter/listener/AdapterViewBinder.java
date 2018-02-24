package com.march.lightadapter.listener;

import com.march.lightadapter.LightHolder;

/**
 * CreateAt : 2018/2/3
 * Describe :
 *
 * @author chendong
 */
public interface AdapterViewBinder<D> {
    void onBindViewHolder(LightHolder holder, D data, int pos, int type);
}
