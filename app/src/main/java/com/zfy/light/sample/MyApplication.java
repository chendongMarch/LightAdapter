package com.zfy.light.sample;

import com.march.common.Common;
import com.zfy.component.basic.ComponentX;
import com.zfy.component.basic.app.AppApplication;

/**
 * CreateAt : 2018/11/8
 * Describe :
 *
 * @author chendong
 */
public class MyApplication extends AppApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        ComponentX.init(this, true);
        Common.init(this, BuildConfig.class);
    }
}
