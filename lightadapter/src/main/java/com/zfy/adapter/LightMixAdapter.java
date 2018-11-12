package com.zfy.adapter;

import android.content.Context;
import android.util.SparseArray;

import com.zfy.adapter.items.IItemAdapter;
import com.zfy.adapter.listener.ModelTypeConfigCallback;
import com.zfy.adapter.model.ModelType;

import java.util.List;

/**
 * CreateAt : 2018/11/12
 * Describe :
 *
 * @author chendong
 */
class LightMixAdapter<D> extends LightAdapter<D> {

    private SparseArray<IItemAdapter<D>> mItemAdapterArray;

    public LightMixAdapter(Context context, List<D> datas, SparseArray<IItemAdapter<D>> array, ModelTypeConfigCallback callback) {
        super(context, datas, callback);
        this.mItemAdapterArray = array;

        setClickCallback((holder, pos, data) -> {
            ModelType modelType = getModelType(data);
            if (modelType != null) {
                array.get(modelType.type).onClickEvent(holder, data, pos);
            }
        });

        setLongPressCallback((holder, pos, data) -> {
            ModelType modelType = getModelType(data);
            if (modelType != null) {
                array.get(modelType.type).onLongPressEvent(holder, data, pos);
            }
        });

        setDbClickCallback((holder, pos, data) -> {
            ModelType modelType = getModelType(data);
            if (modelType != null) {
                array.get(modelType.type).onDbClickEvent(holder, data, pos);
            }
        });
    }

    @Override
    public void onBindView(LightHolder holder, D data, int pos) {
        ModelType modelType = getModelType(data);
        if (modelType != null) {
            mItemAdapterArray.get(modelType.type).onBindView(holder, data, pos);
        }
    }

    @Override
    public void onBindViewUsePayload(LightHolder holder, D data, int pos, String msg) {
        ModelType modelType = getModelType(data);
        if (modelType != null) {
            mItemAdapterArray.get(modelType.type).onBindViewUsePayload(holder, data, pos, msg);
        }
    }
}
