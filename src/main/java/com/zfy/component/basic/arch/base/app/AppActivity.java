package com.zfy.component.basic.arch.base.app;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zfy.component.basic.arch.base.IBaseView;
import com.zfy.component.basic.arch.base.IElegantView;
import com.zfy.component.basic.arch.base.IViewInit;
import com.zfy.component.basic.arch.base.ViewConfig;
import com.zfy.component.basic.foundation.api.Api;

import org.greenrobot.eventbus.Subscribe;

/**
 * CreateAt : 2018/10/11
 * Describe :
 *
 * @author chendong
 */
public abstract class AppActivity extends AppCompatActivity implements IElegantView, IViewInit, IBaseView {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeViewInit();
        getAppDelegate().bindActivity(this);
        init();
    }

    protected void beforeViewInit() {

    }

    @Override
    public ViewConfig getViewConfig() {
        return null;
    }

    // elegant view

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public AppActivity getActivity() {
        return this;
    }

    @Override
    public void startActivity(Class clz) {
        startActivity(new Intent(this, clz));
    }

    @Override
    public @NonNull
    Bundle getData() {
        if (getIntent() == null || getIntent().getExtras() == null) {
            return new Bundle();
        }
        return getIntent().getExtras();
    }

    @Override
    public Lifecycle getLifecycle() {
        return getAppDelegate().getLifecycle();
    }

    @Subscribe
    public void ignoreEvent(AppDelegate thiz) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getAppDelegate().onDestroy();
        Api.queue().cancelRequest(hashCode());
    }
}
