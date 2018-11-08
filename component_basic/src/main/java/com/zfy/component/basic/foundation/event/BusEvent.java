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

    protected BusEvent() {

    }

    protected BusEvent(int code) {
        this.code = code;
    }

    protected BusEvent(String msg) {
        this.msg = msg;
    }

    protected static void post(BusEvent event) {
        EventBus.getDefault().post(event);
    }

    protected static void postSticky(BusEvent event) {
        EventBus.getDefault().postSticky(event);
    }
}
