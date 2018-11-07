package com.zfy.component.basic.app;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.march.common.able.Destroyable;
import com.march.common.exts.EmptyX;
import com.march.common.exts.ListX;
import com.zfy.component.basic.app.view.IOnResultView;
import com.zfy.component.basic.app.view.ViewConfig;
import com.zfy.component.basic.foundation.Exts;
import com.zfy.component.basic.mvx.mvp.IMvpView;
import com.zfy.component.basic.mvx.mvp.app.NoLayoutMvpView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

/**
 * CreateAt : 2018/10/11
 * Describe :
 *
 * @author chendong
 */
public abstract class AppDelegate implements Destroyable, LifecycleOwner, IOnResultView {

    protected Bundle mBundle;

    protected LifecycleOwner    mLifecycleOwner;
    protected LifecycleRegistry mLifecycleRegistry;

    protected Object     mHost;
    protected ViewConfig mViewConfig;
    private   Unbinder   mUnBinder;

    private List<Destroyable>   mDestroyableList;
    private List<Disposable>    mDisposables;
    private List<IOnResultView> mOnResultViews;

    public void addObserver(@NonNull LifecycleObserver observer) {
        mLifecycleRegistry.addObserver(observer);
    }

    public void addOnResultView(IOnResultView view) {
        if (mOnResultViews == null) {
            mOnResultViews = new ArrayList<>();
        }
        mOnResultViews.add(view);
    }

    public void addDisposable(Disposable disposable) {
        if (mDisposables == null) {
            mDisposables = new ArrayList<>();
        }
        mDisposables.add(disposable);
    }

    public void addDestroyable(Destroyable destroyable) {
        if (mDestroyableList == null) {
            mDestroyableList = new ArrayList<>();
        }
        mDestroyableList.add(destroyable);
    }

    public View bindFragment(AppFragment appFragment, LayoutInflater inflater, ViewGroup container) {
        mBundle = appFragment.getArguments();
        return bindFragmentDispatch(appFragment, inflater, container);
    }

    public void bindActivity(AppActivity appActivity) {
        mBundle = appActivity.getIntent().getExtras();
        bindActivityDispatch(appActivity);
    }

    public void bindNoLayoutView(NoLayoutMvpView noLayoutMvpView, Object host) {
        if (host instanceof IMvpView) {
            mBundle = ((IMvpView) host).getData();
        }
        // NoLayoutView 绑定到 Host 生命周期等
        AppDelegate hostDelegate = null;
        if (host instanceof AppActivity) {
            hostDelegate = ((AppActivity) host).getAppDelegate();
        } else if (host instanceof AppFragment) {
            hostDelegate = ((AppFragment) host).getAppDelegate();
        }
        if (hostDelegate != null) {
            hostDelegate.addDestroyable(noLayoutMvpView);
            hostDelegate.addOnResultView(noLayoutMvpView);
        }
        bindNoLayoutViewDispatch(noLayoutMvpView, host);
    }


    public abstract View bindFragmentDispatch(LifecycleOwner owner, LayoutInflater inflater, ViewGroup container);

    public abstract void bindActivityDispatch(LifecycleOwner owner);

    public void bindNoLayoutViewDispatch(LifecycleOwner owner, Object host) {

    }

    public Bundle getBundle() {
        if (mBundle == null) {
            mBundle = new Bundle();
        }
        return mBundle;
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
            } else if (binder instanceof AppFragment) {
                mUnBinder = ButterKnife.bind(host, ((AppFragment) binder).mContentView);
            } else if (binder instanceof View) {
                mUnBinder = ButterKnife.bind(host, (View) binder);
            }
        }
    }

    protected void bindEvent() {
        Exts.registerEvent(mHost);
    }

    @Override
    public void onDestroy() {
        Exts.unRegisterEvent(mHost);
        if (mUnBinder != null) {
            mUnBinder.unbind();
            mUnBinder = null;
        }
        if (!EmptyX.isEmpty(mDestroyableList)) {
            ListX.foreach(mDestroyableList, Destroyable::onDestroy);
            mDestroyableList.clear();
        }
        if (!EmptyX.isEmpty(mDisposables)) {
            ListX.foreach(mDisposables, Disposable::dispose);
            mDisposables.clear();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ListX.foreach(mOnResultViews, view -> view.onRequestPermissionsResult(requestCode, permissions, grantResults));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ListX.foreach(mOnResultViews, view -> view.onActivityResult(requestCode, resultCode, data));
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }
}
