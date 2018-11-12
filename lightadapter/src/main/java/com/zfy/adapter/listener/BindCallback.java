package com.zfy.adapter.listener;

import com.zfy.adapter.LightHolder;
import com.zfy.adapter.model.Position;

/**
 * CreateAt : 2018/11/5
 * Describe : 事件回调
 *
 * @author chendong
 */
public interface BindCallback<D> {

    void bind(LightHolder holder, Position pos, D data);
}
