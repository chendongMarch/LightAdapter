package com.zfy.component.basic.mvx.mvp.app;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.march.common.able.Destroyable;
import com.zfy.component.basic.app.AppDelegate;
import com.zfy.component.basic.app.view.IOnResultView;
import com.zfy.component.basic.mvx.mvp.IMvpPresenter;
import com.zfy.component.basic.mvx.mvp.IMvpView;
import com.zfy.component.basic.mvx.mvp.IMvpView4Extends;

import org.greenrobot.eventbus.Subscribe;

/**
 * CreateAt : 2018/10/9
 * Describe : 没有 布局的 View 层，用来做 View 层抽离
 * V 宿主范型
 * P Presenter 范型
 *
 * @author chendong
 */
public class NoLayoutMvpView<HOST extends IMvpView, P extends IMvpPresenter> implements IMvpView4Extends<P>, Destroyable, IOnResultView {

    protected MvpDelegate<P> mDelegate = new MvpDelegate<>();

    protected HOST mHostView;

    public NoLayoutMvpView(HOST view) {
        mHostView = view;
        mDelegate.bindNoLayoutView(this, mHostView);
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
    public void launchActivity(Intent data, int code) {
        mHostView.launchActivity(data, code);
    }

    @NonNull
    @Override
    public Bundle getData() {
        return mDelegate.getBundle();
    }

    @Override
    public void finishUI(Intent intent, int code) {
        mHostView.finishUI(intent, code);
    }

    @Override
    public P getPresenter() {
        return mDelegate.getPresenter();
    }

    @Override
    public void onDestroy() {
        mDelegate.onDestroy();
    }

    @Subscribe
    public void ignoreEvent(AppDelegate thiz) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }
}
