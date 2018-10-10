package com.zfy.component.basic.mvvm.app;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zfy.component.basic.mvvm.BaseViewModel;

/**
 * CreateAt : 2018/9/11
 * Describe :
 *
 * @author chendong
 */
public class BindingFragment<VM extends BaseViewModel, VDB extends ViewDataBinding>
        extends Fragment
        implements IBindingView<VM, VDB>, MvvmDelegate.IViewInit, IElegantView {

    private MvvmDelegate<VM, VDB> mDelegate = new MvvmDelegate<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return mDelegate.bindFragment(this, inflater, container);
    }

    @Override
    public VM viewModel() {
        return mDelegate.viewModel();
    }

    @Override
    public VDB binding() {
        return mDelegate.binding();
    }

    public ViewConfig getViewConfig() {
        return null;
    }

    @Override
    public void init() {

    }

    @Override
    public <E extends BaseViewModel> E provideViewModel(Class<E> clazz) {
        return mDelegate.provideViewModel(clazz);
    }

    @Override
    public void startActivity(Class clz) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.startActivity(new Intent(activity, clz));
        }
    }

    @Override
    public Bundle getBundle() {
        return getArguments();
    }
}
