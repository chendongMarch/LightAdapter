package com.zfy.component.basic.arch.mvvm.app;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zfy.component.basic.arch.base.app.AppDelegate;
import com.zfy.component.basic.arch.base.app.AppFragment;
import com.zfy.component.basic.arch.mvvm.BaseViewModel;
import com.zfy.component.basic.arch.mvvm.IBindingView;

/**
 * CreateAt : 2018/9/11
 * Describe :
 *
 * @author chendong
 */
public abstract class BindingFragment<VM extends BaseViewModel, VDB extends ViewDataBinding>
        extends AppFragment
        implements IBindingView<VM, VDB> {

    private MvvmDelegate<VM, VDB> mDelegate = new MvvmDelegate<>();

    @Override
    public VM viewModel() {
        return mDelegate.viewModel();
    }

    @Override
    public VDB binding() {
        return mDelegate.binding();
    }

    @Override
    public <E extends BaseViewModel> E provideViewModel(Class<E> clazz) {
        return mDelegate.provideViewModel(clazz);
    }

    @Override
    public AppDelegate getAppDelegate() {
        return mDelegate;
    }
}
