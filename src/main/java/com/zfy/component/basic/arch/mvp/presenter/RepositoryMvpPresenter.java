package com.zfy.component.basic.arch.mvp.presenter;

import com.zfy.component.basic.arch.IRepository;
import com.zfy.component.basic.arch.mvp.IMvpView;

/**
 * CreateAt : 2018/10/11
 * Describe : Presenter with Repository
 *
 * @author chendong
 */
public abstract class RepositoryMvpPresenter<R extends IRepository, V extends IMvpView> extends MvpPresenter<V> {

    protected IRepository mRepository;

    public RepositoryMvpPresenter() {
        try {
            mRepository = getRepositoryClazz().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract Class<R> getRepositoryClazz();
}
