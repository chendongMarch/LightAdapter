package com.zfy.component.basic.arch.mvp.app;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.zfy.component.basic.arch.mvp.IMvpPresenter;
import com.zfy.component.basic.arch.mvp.IMvpView;
import com.zfy.component.basic.arch.mvp.IMvpView4Extends;

/**
 * CreateAt : 2018/10/9
 * Describe : 没有 布局的 View 层，用来做 View 层抽离
 * V 宿主范型
 * P Presenter 范型
 * @author chendong
 */
public class NoLayoutMvpView<V extends IMvpView, P extends IMvpPresenter> implements IMvpView4Extends<P> {

    protected MvpDelegate<P> mDelegate = new MvpDelegate<>();

    protected V mHostView;

    public NoLayoutMvpView(V IView) {
        mHostView = IView;
        mDelegate.bindNoLayoutView(this);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mHostView.getLifecycle();
    }

    @Override
    public Context getContext() {
        return mHostView.getContext();
    }

    @Override
    public Activity getActivity() {
        return mHostView.getActivity();
    }

    @Override
    public void startActivity(Class clz) {
        mHostView.startActivity(clz);
    }

    @NonNull
    @Override
    public Bundle getData() {
        return mHostView.getData();
    }

    @Override
    public P getPresenter() {
        return mDelegate.getPresenter();
    }
}
