package com.zfy.component.basic.arch.base.app;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zfy.component.basic.arch.base.IBaseView;
import com.zfy.component.basic.arch.base.IElegantView;
import com.zfy.component.basic.arch.base.IViewInit;
import com.zfy.component.basic.arch.base.ViewConfig;

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
        getAppDelegate().bindActivity(this);
        init();
    }

    // view init

    @Override
    public ViewConfig getViewConfig() {
        return null;
    }


    @Override
    public void init() {

    }


    // elegant view

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void startActivity(Class clz) {
        startActivity(new Intent(this, clz));
    }

    @Override
    public Bundle getData() {
        if (getIntent() == null) {
            return new Bundle();
        }
        return getIntent().getExtras();
    }

    @Override
    public Lifecycle getLifecycle() {
        return getAppDelegate().getLifecycle();
    }
}
