package com.zfy.adapter.type;

import android.util.SparseArray;

import com.zfy.adapter.listener.ModelTypeConfigCallback;
import com.zfy.adapter.model.ModelType;

/**
 * CreateAt : 2018/11/20
 * Describe : 简化多类型的构建
 *
 * @author chendong
 */
public class ModelTypeRegistry implements ModelTypeConfigCallback {

    private SparseArray<ModelType> mTypeSparseArray;

    public static ModelTypeRegistry create() {
        return new ModelTypeRegistry();
    }

    private ModelTypeRegistry() {
        mTypeSparseArray = new SparseArray<>();
    }

    public ModelTypeRegistry add(ModelType modelType) {
        mTypeSparseArray.append(modelType.type, modelType);
        return this;
    }

    public ModelTypeRegistry add(int type, int layoutId) {
        mTypeSparseArray.append(type, new ModelType(type, layoutId));
        return this;
    }

    public ModelTypeRegistry add(int type, int layoutId, int spanSize) {
        mTypeSparseArray.append(type, new ModelType(type, layoutId, spanSize));
        return this;
    }

    @Override
    public void call(ModelType modelType) {
        ModelType type = mTypeSparseArray.get(modelType.type);
        if (type != null) {
            modelType.update(type);
        }
    }
}
