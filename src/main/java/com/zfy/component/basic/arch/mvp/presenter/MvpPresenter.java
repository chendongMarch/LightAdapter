package com.zfy.component.basic.arch.mvp.presenter;

import com.zfy.component.basic.arch.mvp.IMvpPresenter;
import com.zfy.component.basic.arch.mvp.IMvpView;

/**
 * CreateAt : 2018/10/11
 * Describe : Presenter Impl
 *
 * @author chendong
 */
public abstract class MvpPresenter<V extends IMvpView> implements IMvpPresenter<V> {

    protected V mView;

    public MvpPresenter() {
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void setView(V view) {
        mView = view;
    }


    @Override
    public void init() {

    }
}
