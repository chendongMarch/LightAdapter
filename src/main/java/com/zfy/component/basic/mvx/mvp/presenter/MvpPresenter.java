package com.zfy.component.basic.mvx.mvp.presenter;

import com.march.common.exts.LogX;
import com.zfy.component.basic.mvx.model.IRepository;
import com.zfy.component.basic.mvx.mvp.IMvpPresenter;
import com.zfy.component.basic.mvx.mvp.IMvpView;
import com.zfy.component.basic.foundation.Exts;

/**
 * CreateAt : 2018/10/11
 * Describe : Presenter Impl
 *
 * @author chendong
 */
public abstract class MvpPresenter<R extends IRepository, V extends IMvpView> implements IMvpPresenter {

    protected V mView;
    protected R mRepo;

    public MvpPresenter() {
        mRepo = makeRepo();
    }

    public void attachView(V view) {
        mView = view;
    }

    public abstract void init();

    @SuppressWarnings("unchecked")
    private R makeRepo() {
        R repo = null;
        try {
            MvpP annotation = getClass().getAnnotation(MvpP.class);
            if (annotation != null) {
                Class<R> repoClazz = annotation.repo();
                repo = Exts.newInst(repoClazz);
            }
        } catch (Exception e) {
            LogX.e("no repo presenter");
            e.printStackTrace();
        }
        return repo;
    }

    @Override
    public void onDestroy() {

    }


    public static <P> P attachView(Class<P> pClass, IMvpView mvpView) {
        P presenter = Exts.newInst(pClass);
        if (presenter instanceof MvpPresenter) {
            ((MvpPresenter) presenter).attachView(mvpView);
            ((MvpPresenter) presenter).init();
        }
        return presenter;
    }
}
