package com.zfy.adapter.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CreateAt : 2018/11/12
 * Describe : 表明是数据里面的 index, 他和 RecyclerView 的 index 不同需要转换后使用
 *
 * @author chendong
 */
@Inherited
@Documented

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PARAMETER)
public @interface ModelIndex {

}
