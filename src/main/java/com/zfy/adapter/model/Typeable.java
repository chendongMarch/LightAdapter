package com.zfy.adapter.model;

/**
 * 接口，分类适配的对象需要实现的接口，目的是约束实体类实现getType方法
 *
 * @author chendong
 */
public interface Typeable {
    int getModelType();
}