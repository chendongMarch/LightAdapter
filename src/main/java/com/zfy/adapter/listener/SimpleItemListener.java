package com.zfy.adapter.listener;


import com.zfy.adapter.LightHolder;

/**
 * CreateAt : 2016/11/9
 * Describe : 点击事件
 *
 * @author chendong
 */

public abstract class SimpleItemListener<D> implements OnItemListener<D> {

    @Override
    public void onClick(int pos, LightHolder holder, D data) {
    }

    @Override
    public void onLongPress(int pos, LightHolder holder, D data) {

    }

    @Override
    public void onDoubleClick(int pos, LightHolder holder, D data) {

    }
}
