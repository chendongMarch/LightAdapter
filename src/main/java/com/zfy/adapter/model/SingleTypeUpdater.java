package com.zfy.adapter.model;

import com.zfy.adapter.function.LightConsumer;
import com.zfy.adapter.listener.ModelTypeUpdater;

/**
 * CreateAt : 2018/11/8
 * Describe :
 *
 * @author chendong
 */
public class SingleTypeUpdater implements ModelTypeUpdater {

    private int singleType;
    private LightConsumer<ModelType> mConsumer;

    public SingleTypeUpdater(int singleType, LightConsumer<ModelType> consumer) {
        this.singleType = singleType;
        mConsumer = consumer;
    }

    @Override
    public void update(ModelType modelType) {
        if (modelType.type == singleType) {
            mConsumer.accept(modelType);
        }
    }
}
