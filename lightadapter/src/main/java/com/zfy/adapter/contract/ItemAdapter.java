package com.zfy.adapter.contract;

import com.zfy.adapter.LightEvent;
import com.zfy.adapter.LightHolder;
import com.zfy.adapter.able.Typeable;
import com.zfy.adapter.model.Extra;
import com.zfy.adapter.model.ModelType;

/**
 * CreateAt : 2018/11/12
 * Describe :
 *
 * @author chendong
 */
public interface ItemAdapter<D> extends Typeable, LightEvent.EventDispatcher<D> {

    /**
     * {@inheritDoc}
     * 获取支持的数据类型
     *
     * @return 支持的数据类型
     */
    @Override
    int getItemType();


    ModelType getModelType();

    /**
     * 数据绑定
     *
     * @param holder LightHolder
     * @param data   数据
     * @param extra    数据位置
     */
    void onBindView(LightHolder holder, D data, Extra extra);

    void onClickEvent(LightHolder holder, D data, Extra extra);

    void onLongPressEvent(LightHolder holder, D data, Extra extra);

    void onDbClickEvent(LightHolder holder, D data, Extra extra);

    void onChildViewClickEvent(LightHolder holder, D data, Extra extra);

    void onChildViewLongPressEvent(LightHolder holder, D data, Extra extra);

}
