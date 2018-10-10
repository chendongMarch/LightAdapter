package com.zfy.component.basic.mvvm.app;


import android.support.annotation.NonNull;

import com.zfy.component.basic.mvvm.BaseViewModel;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * CreateAt : 2018/9/12
 * Describe : 注解帮助生成 ViewConfig
 *
 * @author chendong
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface VM {

    int layout();

    @NonNull Class vm() default BaseViewModel.class;

    int vmId() default 0;
}
