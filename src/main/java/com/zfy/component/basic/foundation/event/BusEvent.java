package com.zfy.component.basic.foundation.event;

import org.greenrobot.eventbus.EventBus;

/**
 * CreateAt : 2018/9/18
 * Describe :
 *
 * @author chendong
 */
public class BusEvent {

    public String msg;

    public int code;

    public BusEvent(int code) {
        this.code = code;
    }

    public BusEvent(String msg) {
        this.msg = msg;
    }

    public static void post(BusEvent event) {
        EventBus.getDefault().post(event);
    }

    public static void postSticky(BusEvent event) {
        EventBus.getDefault().postSticky(event);
    }
}
