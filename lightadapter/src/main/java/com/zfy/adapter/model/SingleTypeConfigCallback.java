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

    private int                      mSingleType;
    private LightConsumer<ModelType> mConsumer;

    public SingleTypeConfigCallback(int singleType, LightConsumer<ModelType> consumer) {
        mSingleType = singleType;
        mConsumer = consumer;
    }

    public SingleTypeConfigCallback(LightConsumer<ModelType> consumer) {
        mConsumer = consumer;
    }

    public SingleTypeConfigCallback(ModelType modelType) {
        mSingleType = modelType.type;
        mConsumer = mt -> mt.updateByOtherModelType(modelType);
    }

    public SingleTypeConfigCallback setSingleType(int singleType) {
        this.mSingleType = singleType;
        return this;
    }

    @Override
    public void call(ModelType modelType) {
        if (modelType.type == mSingleType) {
            mConsumer.accept(modelType);
        }
    }
}
