package com.zfy.adapter.items;

import com.zfy.adapter.LightHolder;
import com.zfy.adapter.able.Typeable;
import com.zfy.adapter.model.ModelType;
import com.zfy.adapter.model.Position;

/**
 * CreateAt : 2018/11/12
 * Describe :
 *
 * @author chendong
 */
public interface ItemAdapter<D> extends Typeable {

    /**
     * {@inheritDoc}
     * 获取支持的数据类型
     *
     * @return 支持的数据类型
     */
    @Override
    int getItemType();

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
    void onBindView(LightHolder holder, D data, Position pos);

    /**
     * {@inheritDoc}
     * 使用 payload 进行数据绑定
     *
     * @param holder LightHolder
     * @param data   数据
     * @param pos    数据位置
     * @param msg    更新的信息
     */
    void onBindViewUsePayload(LightHolder holder, D data, Position pos, String msg);

    /**
     * {@inheritDoc}
     * 单击事件
     *
     * @param holder LightHolder
     * @param data   数据
     * @param pos    数据位置
     */
    void onClickEvent(LightHolder holder, D data, Position pos);

    /**
     * {@inheritDoc}
     * 长按事件
     *
     * @param holder LightHolder
     * @param data   数据
     * @param pos    数据位置
     */
    void onLongPressEvent(LightHolder holder, D data, Position pos);

    /**
     * {@inheritDoc}
     * 双击事件
     *
     * @param holder LightHolder
     * @param data   数据
     * @param pos    数据位置
     */
    void onDbClickEvent(LightHolder holder, D data, Position pos);

}
