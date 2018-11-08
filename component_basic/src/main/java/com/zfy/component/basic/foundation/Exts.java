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

    public static <T> T newInst(Class<T> clazz) {
        T t;
        try {
            t = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("实例创建失败", e);
        }
        return t;
    }

}
