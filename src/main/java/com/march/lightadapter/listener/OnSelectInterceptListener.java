package com.march.lightadapter.listener;

/**
 * CreateAt : 2017/6/14
 * Describe : 选择器截断监听
 * 在使用选择器时选中或者取消选中时回先使用截断器
 *
 * @author chendong
 */
public interface OnSelectInterceptListener<D> {
    boolean isInterceptSelect(D data,int pos, boolean select);
}
