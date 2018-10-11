package com.zfy.component.basic.arch.mvp;

import android.arch.lifecycle.LifecycleOwner;

/**
 * CreateAt : 2018/10/11
 * Describe : Mvp view
 *
 * @author chendong
 */
public interface IMvpView<P> extends LifecycleOwner {

    P getPresenter();

}
