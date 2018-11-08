package com.zfy.component.basic.mvx.mvp.contract;

import com.zfy.component.basic.mvx.mvp.IMvpContract;

/**
 * CreateAt : 2018/10/19
 * Describe :
 *
 * @author chendong
 */
public interface LazyLoadContract extends IMvpContract {

    interface V {
        void setLazyLoadEnable(boolean enable);
    }
}
