package com.march.lightadapter.annotation;

import com.march.lightadapter.module.HFModule;

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
public @interface HeaderFooter {

    /**
     * @return header 资源ID
     */
    int header() default -1;

    /**
     * @return footer 资源ID
     */
    int footer() default -1;

}
