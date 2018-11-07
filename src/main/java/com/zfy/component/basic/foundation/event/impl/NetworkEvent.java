package com.zfy.component.basic.foundation.event.impl;

import com.zfy.component.basic.foundation.event.BusEvent;

/**
 * CreateAt : 2018/10/16
 * Describe :
 *
 * @author chendong
 */
public class NetworkEvent extends BusEvent {

    public static final String NETWORK_CHANGED = "NETWORK_CHANGED";


    public NetworkEvent(String msg) {
        super(msg);
    }

    public static void post() {
        post(new NetworkEvent(NETWORK_CHANGED));
    }
}
