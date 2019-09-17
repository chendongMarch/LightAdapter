package com.zfy.adapter.listener;

import com.zfy.adapter.LxAdapter;

/**
 * CreateAt : 2019-09-04
 * Describe :
 *
 * @author chendong
 */
public interface EventHandler {

    void intercept(String event, LxAdapter adapter, Object extra);
}
