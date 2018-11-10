package com.zfy.adapter.model;

import com.zfy.adapter.function.LightConsumer;
import com.zfy.adapter.listener.ModelTypeConfigCallback;

/**
 * CreateAt : 2018/11/8
 * Describe :
 *
 * @author chendong
 */
public class SingleTypeConfigCallback implements ModelTypeConfigCallback {

    private int singleType;
    private LightConsumer<ModelType> mConsumer;

    public SingleTypeConfigCallback(int singleType, LightConsumer<ModelType> consumer) {
        this.singleType = singleType;
        mConsumer = consumer;
    }

    public SingleTypeConfigCallback(LightConsumer<ModelType> consumer) {
        this.singleType = singleType;
        mConsumer = consumer;
    }

    public SingleTypeConfigCallback setSingleType(int singleType) {
        this.singleType = singleType;
        return this;
    }

    @Override
    public void call(ModelType modelType) {
        if (modelType.type == singleType) {
            mConsumer.accept(modelType);
        }
    }
}
