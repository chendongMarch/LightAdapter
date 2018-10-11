package com.zfy.component.basic.arch.mvp.app;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.march.common.exts.LogX;
import com.zfy.component.basic.ComponentX;
import com.zfy.component.basic.arch.base.IViewInit;
import com.zfy.component.basic.arch.base.ViewConfig;
import com.zfy.component.basic.arch.base.app.AppActivity;
import com.zfy.component.basic.arch.base.app.AppDelegate;
import com.zfy.component.basic.arch.base.app.AppFragment;
import com.zfy.component.basic.arch.mvp.IMvpPresenter;
import com.zfy.component.basic.arch.mvp.IMvpView;

/**
 * CreateAt : 2018/9/12
 * Describe :
 *
 * @author chendong
 */
public class MvpDelegate<P extends IMvpPresenter> extends AppDelegate implements IMvpView<P> {

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
    public View bindFragment(LifecycleOwner owner, LayoutInflater inflater, ViewGroup container) {
        if (!(owner instanceof AppFragment)) {
            throw new IllegalArgumentException("owner must be fragment");
        }
        attach(owner);
        View inflate = inflater.inflate(mViewConfig.getLayout(), container, false);
        bindViewAndEvent(inflate);
        init();
        return inflate;

    }


    @Override
    public void bindActivity(LifecycleOwner owner) {
        if (!(owner instanceof AppActivity)) {
            throw new IllegalArgumentException("owner must be activity");
        }
        attach(owner);
        ((AppActivity) mHost).setContentView(mViewConfig.getLayout());
        bindViewAndEvent(null);
        init();
    }

    @SuppressWarnings("unchecked")
    private void init() {
        if (mHost instanceof IMvpView) {
            Class pClazz = mViewConfig.getpClazz();
            try {
                mPresenter = (P) pClazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mPresenter != null) {
                mPresenter.setView((IMvpView) mHost);
                mPresenter.init();
            }
        } else {
            LogX.e(TAG, "Host not IMvpView");
        }
    }

    @Override
    public P getPresenter() {
        return mPresenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }
}
