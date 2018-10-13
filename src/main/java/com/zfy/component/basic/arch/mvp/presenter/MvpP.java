package com.zfy.component.basic.arch.mvp.presenter;


import android.support.annotation.NonNull;

import com.zfy.component.basic.arch.model.AppRepository;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CreateAt : 2018/9/12
 * Describe : 注解帮助生成 Presenter
 *
 * @author chendong
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MvpP {

    @NonNull Class repo() default AppRepository.class;
}
