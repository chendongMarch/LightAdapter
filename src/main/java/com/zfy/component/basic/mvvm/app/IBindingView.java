package com.zfy.component.basic.mvvm.app;

import android.arch.lifecycle.LifecycleOwner;
import android.databinding.ViewDataBinding;

import com.zfy.component.basic.mvvm.BaseViewModel;

/**
 * CreateAt : 2018/9/12
 * Describe :
 *
 * @author chendong
 */
public interface IBindingView<VM extends BaseViewModel, VDB extends ViewDataBinding> extends LifecycleOwner {

    /**
     * @return 返回页面绑定的唯一 ViewModel
     */
    VM viewModel();

    /**
     * @return 返回页面绑定的唯一 Binding
     */
    VDB binding();

    /**
     * @param clazz ViewModel class
     * @param <E>   ViewModel 范型
     * @return 获取一个 ViewModel, 为了更好在 Fragment 之间进行数据共享做的封装，获取本页面 ViewModel {@link IBindingView#viewModel()}
     */
    <E extends BaseViewModel> E provideViewModel(Class<E> clazz);

    /**
     * 初始化数据
     */
    void init();

}
