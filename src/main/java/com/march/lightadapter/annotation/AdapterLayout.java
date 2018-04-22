package com.march.lightadapter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CreateAt : 2018/2/24
 * Describe :
 *
 * @author chendong
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AdapterLayout {

    /**
     * @return 默认 item 资源ID
     */
    int value() default -1;

    /**
     * @return 默认 item 资源ID
     */
    int itemLayoutId() default -1;

    /**
     * @return item type 数组
     */
    int[] itemTypes() default {};

    /**
     * @return item layout 资源数组
     */
    int[] itemLayoutIds() default {};
}
