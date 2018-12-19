package com.zfy.adapter.contract;

import com.zfy.adapter.LightHolder;
import com.zfy.adapter.model.Extra;

/**
 * CreateAt : 2018/12/13
 * Describe :
 *
 * @author chendong
 */
public interface IAdapter<D> {

    /**
     * 绑定数据
     *
     * @param holder LightHolder
     * @param data   数据
     * @param extra    位置
     */
    void onBindView(LightHolder holder, D data, Extra extra);

}
