package com.zfy.adapter.callback;

import com.zfy.adapter.LightHolder;
import com.zfy.adapter.model.Extra;

/**
 * CreateAt : 2018/11/5
 * Describe : 事件回调
 *
 * @author chendong
 */
public interface PayloadBindCallback<D> {

    void bind(LightHolder holder, Extra extra, D data, String payload);
}
