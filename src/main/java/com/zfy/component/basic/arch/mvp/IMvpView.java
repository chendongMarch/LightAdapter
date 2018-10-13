package com.zfy.component.basic.arch.mvp;

import android.arch.lifecycle.LifecycleOwner;

import com.zfy.component.basic.arch.base.IElegantView;

/**
 * CreateAt : 2018/10/11
 * Describe : Mvp view
 *
 * @author chendong
 */
public interface IMvpView<P extends IMvpPresenter> extends LifecycleOwner, IElegantView {

    P getPresenter();

}
