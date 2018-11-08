package com.zfy.component.basic.mvx.mvvm;

import android.arch.lifecycle.LifecycleOwner;
import android.databinding.ViewDataBinding;

/**
 * CreateAt : 2018/9/12
 * Describe : binding view
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

}
