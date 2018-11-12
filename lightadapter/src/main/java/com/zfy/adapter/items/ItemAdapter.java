package com.zfy.adapter.items;

import com.zfy.adapter.LightHolder;
import com.zfy.adapter.able.ModelTypeable;
import com.zfy.adapter.annotations.ModelIndex;
import com.zfy.adapter.model.ModelType;

/**
 * CreateAt : 2018/11/12
 * Describe :
 *
 * @author chendong
 */
public interface ItemAdapter<D> extends ModelTypeable {

    /**
     * {@inheritDoc}
     * 获取支持的数据类型
     *
     * @return 支持的数据类型
     */
    @Override
    int getModelType();

    /**
     * {@inheritDoc}
     * 配置 ModelType
     *
     * @param modelType ModelType
     * @see ModelType
     */
    void configModelType(ModelType modelType);

    /**
     * 数据绑定
     *
     * @param holder LightHolder
     * @param data   数据
     * @param pos    数据位置
     */
    void onBindView(LightHolder holder, D data, @ModelIndex int pos);

    /**
     * {@inheritDoc}
     * 使用 payload 进行数据绑定
     *
     * @param holder LightHolder
     * @param data   数据
     * @param pos    数据位置
     * @param msg    更新的信息
     */
    void onBindViewUsePayload(LightHolder holder, D data, @ModelIndex int pos, String msg);

    /**
     * {@inheritDoc}
     * 单击事件
     *
     * @param holder LightHolder
     * @param data   数据
     * @param pos    数据位置
     */
    void onClickEvent(LightHolder holder, D data, @ModelIndex int pos);

    /**
     * {@inheritDoc}
     * 长按事件
     *
     * @param holder LightHolder
     * @param data   数据
     * @param pos    数据位置
     */
    void onLongPressEvent(LightHolder holder, D data, @ModelIndex int pos);

    /**
     * {@inheritDoc}
     * 双击事件
     *
     * @param holder LightHolder
     * @param data   数据
     * @param pos    数据位置
     */
    void onDbClickEvent(LightHolder holder, D data, @ModelIndex int pos);

}
