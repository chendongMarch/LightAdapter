package com.zfy.component.basic.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * CreateAt : 2018/9/11
 * Describe :
 *
 * @author chendong
 */
public abstract class AppApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
