package com.zfy.component.basic.mvvm.app;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zfy.component.basic.mvvm.BaseViewModel;

/**
 * CreateAt : 2018/9/11
 * Describe :
 *
 * @author chendong
 */
public class BindingActivity<VM extends BaseViewModel, VDB extends ViewDataBinding>
        extends AppCompatActivity
        implements IBindingView<VM, VDB>, MvvmDelegate.IViewInit, IElegantView {

    protected MvvmDelegate<VM, VDB> mDelegate = new MvvmDelegate<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDelegate.bindActivity(this);
        // 初始化
        init();
    }

    @Override
    public void init() {

    }

    @Override
    public Lifecycle getLifecycle() {
        return mDelegate.getLifecycle();
    }

    @Override
    public VDB binding() {
        return mDelegate.binding();
    }

    @Override
    public VM viewModel() {
        return mDelegate.viewModel();
    }

    @Override
    public ViewConfig getViewConfig() {
        return null;
    }

    @Override
    public <E extends BaseViewModel> E provideViewModel(Class<E> clazz) {
        return mDelegate.provideViewModel(clazz);
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
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
    public Bundle getBundle() {
        return getIntent().getExtras();
    }
}
