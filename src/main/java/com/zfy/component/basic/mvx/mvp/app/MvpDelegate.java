package com.zfy.component.basic.mvx.mvp.app;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.march.common.exts.LogX;
import com.zfy.component.basic.ComponentX;
import com.zfy.component.basic.app.AppDelegate;
import com.zfy.component.basic.app.view.IViewInit;
import com.zfy.component.basic.app.view.ViewConfig;
import com.zfy.component.basic.mvx.mvp.IMvpPresenter;
import com.zfy.component.basic.mvx.mvp.IMvpView;
import com.zfy.component.basic.mvx.mvp.presenter.MvpPresenter;

/**
 * CreateAt : 2018/9/12
 * Describe :
 *
 * @author chendong
 */
public class MvpDelegate<P extends IMvpPresenter> extends AppDelegate {

    public static final String TAG = MvpDelegate.class.getSimpleName();

    private P mPresenter;

    private <T extends LifecycleOwner> void attach(T obj) {
        ComponentX.inject(obj);
        mHost = obj;
        mLifecycleOwner = obj;
        mLifecycleRegistry = new LifecycleRegistry(mLifecycleOwner);
        if (obj instanceof IViewInit && ((IViewInit) obj).getViewConfig() != null) {
            mViewConfig = ((IViewInit) obj).getViewConfig();
        } else {
            MvpV annotation = mHost.getClass().getAnnotation(MvpV.class);
            if (annotation != null) {
                int layout = annotation.layout();
                Class pClazz = annotation.p();
                if (layout != 0) {
                    mViewConfig = ViewConfig.makeMvp(layout, pClazz);
                }
            }
        }
        if (mViewConfig == null) {
            throw new IllegalStateException("require ViewConfig");
        }
    }

    @Override
    public View bindFragmentDispatch(LifecycleOwner owner, LayoutInflater inflater, ViewGroup container) {
        attach(owner);
        View inflate = inflater.inflate(mViewConfig.getLayout(), container, false);
        bindView(mHost, inflate);
        bindEvent();
        init();
        return inflate;
    }

    @Override
    public void bindActivityDispatch(LifecycleOwner owner) {
        attach(owner);
        ((Activity) owner).setContentView(mViewConfig.getLayout());
        bindView(mHost, null);
        bindEvent();
        init();
    }


    @Override
    public void bindNoLayoutViewDispatch(LifecycleOwner owner, Object binder) {
        if (!(owner instanceof NoLayoutMvpView)) {
            throw new IllegalArgumentException("owner must be no layout view");
        }
        attach(owner);
        bindView(mHost, binder);
        bindEvent();
        init();
    }

    @SuppressWarnings("unchecked")
    private void init() {
        if (mHost instanceof IMvpView) {
            Class pClazz = mViewConfig.getpClazz();
            try {
                if(pClazz !=null) {
                    mPresenter = (P) pClazz.newInstance();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (getPresenter() != null && getPresenter() instanceof MvpPresenter) {
                MvpPresenter presenter = (MvpPresenter) getPresenter();
                presenter.attachView((IMvpView) mHost);
                presenter.init();
            }
        } else {
            LogX.e(TAG, "Host not IMvpView");
        }
    }

    public P getPresenter() {
        return mPresenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getPresenter() != null) {
            getPresenter().onDestroy();
        }
    }


    public static class NoPresenter extends MvpPresenter {

        @Override
        public void init() {

        }
    }
}
