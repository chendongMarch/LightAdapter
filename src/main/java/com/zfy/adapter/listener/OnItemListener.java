package com.zfy.adapter.listener;

import com.zfy.adapter.LightHolder;

/**
 * CreateAt : 2016/11/9
 * Describe : 点击事件
 *
 * @author chendong
 */
public interface OnItemListener<D> {

    // 单击事件
    void onClick(int pos, LightHolder holder, D data);

    // 长按事件
    void onLongPress(int pos, LightHolder holder, D data);

    // 双击事件
    void onDoubleClick(int pos, LightHolder holder, D data);
}
