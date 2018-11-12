package com.zfy.adapter.listener;

import com.zfy.adapter.LightHolder;
import com.zfy.adapter.annotations.ModelIndex;

/**
 * CreateAt : 2018/11/5
 * Describe : 事件回调
 *
 * @author chendong
 */
public interface EventCallback<D> {

    void call(LightHolder holder, @ModelIndex int pos, D data);
}
