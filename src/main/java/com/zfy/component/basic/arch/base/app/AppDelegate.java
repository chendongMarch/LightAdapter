package com.zfy.component.basic.arch.base.app;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.march.common.able.Destroyable;
import com.zfy.component.basic.arch.base.ViewConfig;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * CreateAt : 2018/10/11
 * Describe :
 *
 * @author chendong
 */
public abstract class AppDelegate implements Destroyable, LifecycleOwner {

    protected LifecycleOwner    mLifecycleOwner;
    protected LifecycleRegistry mLifecycleRegistry;

    protected Object     mHost;
    protected ViewConfig mViewConfig;
    private   Unbinder   mUnBinder;

    public abstract View bindFragment(LifecycleOwner owner, LayoutInflater inflater, ViewGroup container);

    public abstract void bindActivity(LifecycleOwner owner);

    protected void bindViewAndEvent(View view) {
        if (mHost instanceof AppActivity) {
            mUnBinder = ButterKnife.bind(mHost, (AppActivity) mHost);
        } else if (mHost instanceof AppFragment) {
            mUnBinder = ButterKnife.bind(mHost, view);
        }
        if (!EventBus.getDefault().isRegistered(mHost)) {
            EventBus.getDefault().register(mHost);
        }
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(mHost)) {
            EventBus.getDefault().unregister(mHost);
        }
        if (mUnBinder != null) {
            mUnBinder.unbind();
            mUnBinder = null;
        }
    }

    public void addObserver(@NonNull LifecycleObserver observer) {
        mLifecycleRegistry.addObserver(observer);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }
}
