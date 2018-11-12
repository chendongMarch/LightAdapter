package com.zfy.adapter.items;

import com.zfy.adapter.LightHolder;

/**
 * CreateAt : 2018/11/12
 * Describe :
 *
 * @author chendong
 */
@TypeOption(type = 100, enableDrag = true)
public abstract class ItemAdapter<D> implements IItemAdapter<D> {

    @Override
    public void onBindViewUsePayload(LightHolder holder, D data, int pos, String msg) {

    }

    @Override
    public void onClickEvent(LightHolder holder, D data, int pos) {

    }

    @Override
    public void onLongPressEvent(LightHolder holder, D data, int pos) {

    }

    @Override
    public void onDbClickEvent(LightHolder holder, D data, int pos) {

    }

}
