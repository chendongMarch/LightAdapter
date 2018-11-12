package com.zfy.adapter.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CreateAt : 2018/11/12
 * Describe :
 *
 * @author chendong
 */
@Inherited
@Documented

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PARAMETER)
public @interface ModelIndex {

}
