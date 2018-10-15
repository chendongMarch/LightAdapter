package com.zfy.component.basic.mvx.mvvm.app;

import android.databinding.ViewDataBinding;

import com.zfy.component.basic.app.AppDelegate;
import com.zfy.component.basic.app.AppFragment;
import com.zfy.component.basic.mvx.mvvm.BaseViewModel;
import com.zfy.component.basic.mvx.mvvm.IBindingView;

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
