package com.zfy.component.basic.arch.mvp.app;

import com.zfy.component.basic.arch.base.app.AppActivity;
import com.zfy.component.basic.arch.base.app.AppDelegate;
import com.zfy.component.basic.arch.mvp.IMvpPresenter;
import com.zfy.component.basic.arch.mvp.IMvpView4Extends;

/**
 * CreateAt : 2018/10/11
 * Describe :
 *
 * @author chendong
 */
public abstract class MvpActivity<P extends IMvpPresenter> extends AppActivity implements IMvpView4Extends<P> {

    protected MvpDelegate<P> mDelegate = new MvpDelegate<>();

    @Override
    public AppDelegate getAppDelegate() {
        return mDelegate;
    }

    @Override
    public P getPresenter() {
        return mDelegate.getPresenter();
    }
}
