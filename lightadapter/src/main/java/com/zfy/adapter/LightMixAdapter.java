package com.zfy.adapter;

import android.util.SparseArray;

import com.zfy.adapter.items.ItemAdapter;
import com.zfy.adapter.listener.ModelTypeConfigCallback;
import com.zfy.adapter.model.ModelType;
import com.zfy.adapter.model.Position;

import java.util.List;

/**
 * CreateAt : 2018/11/12
 * Describe :
 *
 * @author chendong
 */
class LightMixAdapter<D> extends LightAdapter<D> {

    private SparseArray<ItemAdapter<D>> mItemAdapterArray;

    public LightMixAdapter(List<D> datas, SparseArray<ItemAdapter<D>> array, ModelTypeConfigCallback callback) {
        super( datas, callback);
        this.mItemAdapterArray = array;
        setClickEvent((holder, pos, data) -> {
            ModelType modelType = getModelType(data);
            if (modelType != null) {
                array.get(modelType.type).onClickEvent(holder, data, pos);
            }
        });

        setLongPressEvent((holder, pos, data) -> {
            ModelType modelType = getModelType(data);
            if (modelType != null) {
                array.get(modelType.type).onLongPressEvent(holder, data, pos);
            }
        });

        setDbClickEvent((holder, pos, data) -> {
            ModelType modelType = getModelType(data);
            if (modelType != null) {
                array.get(modelType.type).onDbClickEvent(holder, data, pos);
            }
        });
    }

    @Override
    public void onBindView(LightHolder holder, D data, Position pos) {
        ModelType modelType = getModelType(data);
        if (modelType != null) {
            mItemAdapterArray.get(modelType.type).onBindView(holder, data, pos);
        }
    }

    @Override
    public void onBindViewUsePayload(LightHolder holder, D data, Position pos, String msg) {
        ModelType modelType = getModelType(data);
        if (modelType != null) {
            mItemAdapterArray.get(modelType.type).onBindViewUsePayload(holder, data, pos, msg);
        }
    }
}
