package com.zfy.component.basic.mvx.mvp.app;

import com.zfy.component.basic.app.AppDelegate;
import com.zfy.component.basic.app.AppFragment;
import com.zfy.component.basic.mvx.mvp.IMvpPresenter;
import com.zfy.component.basic.mvx.mvp.IMvpView4Extends;

/**
 * CreateAt : 2018/10/11
 * Describe :
 *
 * @author chendong
 */
public abstract class MvpFragment<P extends IMvpPresenter> extends AppFragment implements IMvpView4Extends {

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
