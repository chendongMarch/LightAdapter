package com.zfy.adapter.items;

import com.zfy.adapter.LightHolder;
import com.zfy.adapter.able.ModelTypeable;
import com.zfy.adapter.model.ModelType;

/**
 * CreateAt : 2018/11/12
 * Describe :
 *
 * @author chendong
 */
public interface IItemAdapter<D> extends ModelTypeable {

    void configModelType(ModelType modelType);

    void onBindView(LightHolder holder, D data, int pos);

    void onBindViewUsePayload(LightHolder holder, D data, int pos, String msg);


    void onClickEvent(LightHolder holder, D data, int pos);

    void onLongPressEvent(LightHolder holder, D data, int pos);

    void onDbClickEvent(LightHolder holder, D data, int pos);

}
