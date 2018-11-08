package com.zfy.component.basic.mvx.mvvm;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleObserver;
import android.content.Context;
import android.support.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * CreateAt : 2018/9/11
 * Describe : ViewModel 基类
 *
 * @author chendong
 */
public abstract class BaseViewModel extends AndroidViewModel implements LifecycleObserver {

    public BaseViewModel(@NonNull Application application) {
        super(application);
        EventBus.getDefault().register(this);
    }

    /**
     * View 层会在 ViewModel 创建后调用 init() 方法
     * init() 在构造方法之后调用
     */
    public void init() {

    }

    public Context getCtx() {
        return getApplication();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        EventBus.getDefault().unregister(this);
    }

    // for event bus compiler
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ignore(BaseViewModel ignore) {
    }

}
