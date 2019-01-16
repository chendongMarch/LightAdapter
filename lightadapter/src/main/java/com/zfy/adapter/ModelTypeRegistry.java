package com.zfy.adapter;

import android.support.annotation.LayoutRes;
import android.util.SparseArray;

import com.zfy.adapter.contract.ItemBinder;
import com.zfy.adapter.model.ModelType;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateAt : 2018/11/20
 * Describe : 简化多类型的构建
 *
 * @author chendong
 */
public class ModelTypeRegistry {

    private SparseArray<ModelType> mTypeSparseArray;
    private List<ItemBinder>       mItemBinders;

    public static ModelTypeRegistry create() {
        return new ModelTypeRegistry();
    }

    public static ModelTypeRegistry create(ItemBinder adapter) {
        ModelTypeRegistry registry = new ModelTypeRegistry();
        registry.add(adapter);
        return registry;
    }

    public ModelTypeRegistry() {
        mTypeSparseArray = new SparseArray<>();
        mItemBinders = new ArrayList<>();
    }

    public void add(ModelType modelType) {
        mTypeSparseArray.put(modelType.type, modelType);
    }

    public void add(int type, @LayoutRes int layoutId) {
        mTypeSparseArray.put(type, new ModelType(type, layoutId));
    }

    public void add(int type, @LayoutRes int layoutId, int spanSize) {
        mTypeSparseArray.put(type, new ModelType(type, layoutId, spanSize));
    }

    public void add(ItemBinder part) {
        mItemBinders.add(part);
    }


    public SparseArray<ModelType> getTypeSparseArray() {
        return mTypeSparseArray;
    }

    public List<ItemBinder> getItemBinders() {
        return mItemBinders;
    }
}
