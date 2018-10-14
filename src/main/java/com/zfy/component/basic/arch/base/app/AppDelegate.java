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
import com.march.common.exts.EmptyX;
import com.march.common.exts.ListX;
import com.zfy.component.basic.arch.base.ViewConfig;
import com.zfy.component.basic.arch.mvp.app.NoLayoutMvpView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

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

    private List<Destroyable> mDestroyableList;

    public void addDestroyable(Destroyable destroyable) {
        if (mDestroyableList == null) {
            mDestroyableList = new ArrayList<>();
        }
        mDestroyableList.add(destroyable);
    }

    public abstract View bindFragment(LifecycleOwner owner, LayoutInflater inflater, ViewGroup container);

    public abstract void bindActivity(LifecycleOwner owner);

    public void bindNoLayoutView(LifecycleOwner owner, Object host) {

    }

    /**
     * @param host   当前需要绑定的 对象
     * @param binder findViewById 对象
     */
    protected void bindView(Object host, Object binder) {
        if (host instanceof AppActivity) {
            mUnBinder = ButterKnife.bind((AppActivity) host);
        } else if (host instanceof AppFragment && binder instanceof View) {
            mUnBinder = ButterKnife.bind(host, (View) binder);
        } else if (host instanceof NoLayoutMvpView) {
            if (binder instanceof AppActivity) {
                mUnBinder = ButterKnife.bind(host, (AppActivity) binder);
            } else if (binder instanceof View) {
                mUnBinder = ButterKnife.bind(host, (View) binder);
            }
        }
    }

    protected void bindEvent() {
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
        if (!EmptyX.isEmpty(mDestroyableList)) {
            ListX.foreach(mDestroyableList, Destroyable::onDestroy);
            mDestroyableList.clear();
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
