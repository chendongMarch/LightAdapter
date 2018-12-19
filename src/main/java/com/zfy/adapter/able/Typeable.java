package com.zfy.adapter.able;

/**
 * CreateAt : 2018/11/12
 * Describe :
 * 类型接口，实现该接口表明数据为多类型数据，
 * 分类适配的对象需要实现的接口，目的是约束实体类实现 getType 方法
 *
 * @author chendong
 */
public interface Typeable {
    int getItemType();
}