package com.zfy.component.basic.arch.mvp.presenter;

import com.zfy.component.basic.arch.model.IRepository;
import com.zfy.component.basic.arch.mvp.IMvpPresenter;
import com.zfy.component.basic.arch.mvp.IMvpView;
import com.zfy.component.basic.foundation.Exts;

/**
 * CreateAt : 2018/10/11
 * Describe : Presenter Impl
 *
 * @author chendong
 */
public abstract class MvpPresenter<R extends IRepository, V extends IMvpView> implements IMvpPresenter<V> {

    protected V mView;
    protected R mRepo;

    public MvpPresenter() {
        mRepo = makeRepo();
    }

    @SuppressWarnings("unchecked")
    private R makeRepo() {
        try {
            MvpP annotation = getClass().getAnnotation(MvpP.class);
            if (annotation != null) {
                Class repo = annotation.repo();
                mRepo = ((R) Exts.newInst(repo));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("resp class error", e);
        }
        return null;
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
