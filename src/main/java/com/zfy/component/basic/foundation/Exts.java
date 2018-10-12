package com.zfy.component.basic.foundation;

import org.greenrobot.eventbus.EventBus;

/**
 * CreateAt : 2018/10/12
 * Describe :
 *
 * @author chendong
 */
public class Exts {

    public static void registerEvent(Object host) {
        if (host == null) {
            return;
        }
        if (!EventBus.getDefault().isRegistered(host)) {
            EventBus.getDefault().register(host);
        }
    }

    public static void unRegisterEvent(Object host) {
        if (host == null) {
            return;
        }
        if (EventBus.getDefault().isRegistered(host)) {
            EventBus.getDefault().unregister(host);
        }
    }
}
