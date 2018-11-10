package com.zfy.adapter.delegate.refs;

import com.zfy.adapter.delegate.impl.SelectorDelegate;
import com.zfy.adapter.listener.BindCallback;

import java.util.List;

/**
 * CreateAt : 2018/11/10
 * Describe :
 *
 * @author chendong
 */
public interface SelectorRef<D> {

    interface OnSelectListener<D> {
        boolean onSelect(D data);
    }

    /**
     * {@inheritDoc}
     * 设置单选数据绑定
     *
     * @param bindCallback 数据绑定
     */
    void setSingleSelector(BindCallback<D> bindCallback);

    /**
     * {@inheritDoc}
     * 设置多选数据绑定
     *
     * @param bindCallback 数据绑定
     */
    void setMultiSelector(BindCallback<D> bindCallback);


    /**
     * {@inheritDoc}
     * 设置选中的监听，当元素被选中时触发
     *
     * @param onSelectListener 选中时的监听
     */
    void setOnSelectListener(SelectorDelegate.OnSelectListener<D> onSelectListener);

    /**
     * {@inheritDoc}
     * 获取多选结果
     *
     * @return 多选结果
     */
    List<D> getResults();

    /**
     * {@inheritDoc}
     * 获取单选结果
     *
     * @param defaultValue 默认值，如果没有选择返回默认值
     * @return 单选的结果
     */
    D getResult(D defaultValue);


    /**
     * {@inheritDoc}
     * 判断该元素有没有被选中
     *
     * @param data 数据
     * @return 有没有被选中
     */
    boolean isSelect(D data);


    /**
     * {@inheritDoc}
     * 选中某一项
     *
     * @param data 继承 Selectable 接口
     */
    void selectItem(D data);

    /**
     * {@inheritDoc}
     * 释放某一项
     *
     * @param data 继承 Selectable 接口
     */
    void releaseItem(D data);

    /**
     * {@inheritDoc}
     * 选中改为不选中，不选中改为选中
     *
     * @param data 继承 Selectable 接口
     */
    void toggleItem(D data);
}
