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
public @interface PreLoading {

    /**
     * @return 顶部预加载更多，提前加载的项数
     */
    int top() default -1;

    /**
     * @return 底部预加载更多，提前加载的项数
     */
    int bottom() default -1;

}
