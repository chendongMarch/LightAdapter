package com.zfy.adapter.contract;

import com.zfy.adapter.LightEvent;
import com.zfy.adapter.LightHolder;
import com.zfy.adapter.data.Typeable;
import com.zfy.adapter.model.Extra;
import com.zfy.adapter.model.ModelType;

/**
 * CreateAt : 2018/11/12
 * Describe :
 *
 * @author chendong
 */
public interface ItemBinder<D> extends Typeable, LightEvent.EventDispatcher<D>, IEventContract<D>, IAdapter<D> {

    /**
     * {@inheritDoc}
     * 获取支持的数据类型
     *
     * @return 支持的数据类型
     */
    @Override
    int getItemType();


    /**
     * 初始化 ModelType
     *
     * @return modelType
     */
    ModelType newModelType();


    /**
     * 获取 ModelType
     *
     * @return modelType
     */
    ModelType getModelType();


    /**
     * 供  LightAdapter 调用，子类重写 onBindView 即可
     *
     * @param holder holder
     * @param data   数据
     * @param extra  附加对象
     */
    void onBindViewUseItemBinder(LightHolder holder, D data, Extra extra);

}
