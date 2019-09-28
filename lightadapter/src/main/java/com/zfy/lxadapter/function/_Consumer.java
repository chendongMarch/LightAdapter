package com.zfy.lxadapter.function;

/**
 * CreateAt : 2018/11/8
 * Describe :
 *
 * @author chendong
 */
public interface _Consumer<T> {

    void accept(T data);
}
