package com.zfy.component.basic.mvx.mvp.app;

import com.zfy.component.basic.app.AppActivity;
import com.zfy.component.basic.app.AppDelegate;
import com.zfy.component.basic.mvx.mvp.IMvpPresenter;
import com.zfy.component.basic.mvx.mvp.IMvpView4Extends;

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
