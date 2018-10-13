package com.zfy.component.basic.arch.mvp;

/**
 * CreateAt : 2018/10/11
 * Describe : Mvp view
 *
 * @author chendong
 */
public interface IMvpView4Extends<P extends IMvpPresenter> extends IMvpView {

    P getPresenter();
}
