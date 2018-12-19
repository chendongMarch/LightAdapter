package com.zfy.adapter.function;

/**
 * CreateAt : 2018/11/21
 * Describe :
 *
 * @author chendong
 */
public interface LightFunction<V, R> {
    R map(V value);
}
