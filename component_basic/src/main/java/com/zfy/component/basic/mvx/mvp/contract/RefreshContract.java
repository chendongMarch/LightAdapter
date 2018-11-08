package com.zfy.component.basic.mvx.mvp.contract;

/**
 * CreateAt : 2018/9/21
 * Describe : 刷新的 V P
 *
 * @author chendong
 */
public interface RefreshContract {

    interface V {

        void finishRefresh();

        void setRefreshEnable(boolean enable);
    }

    interface P {

        void refresh();
    }
}
