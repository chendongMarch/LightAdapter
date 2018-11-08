package com.zfy.component.basic.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.zfy.component.basic.foundation.Exts;

import org.greenrobot.eventbus.Subscribe;

/**
 * CreateAt : 2018/10/11
 * Describe : Service
 *
 * @author chendong
 */
public abstract class AppService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Exts.registerEvent(this);
        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Exts.unRegisterEvent(this);
    }

    @Subscribe
    public void ignoreEvent(AppService thiz) {

    }

    protected abstract void init();

}
