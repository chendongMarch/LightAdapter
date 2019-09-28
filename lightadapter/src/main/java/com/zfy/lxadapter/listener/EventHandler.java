package com.zfy.lxadapter.listener;

import com.zfy.lxadapter.LxAdapter;

/**
 * CreateAt : 2019-09-04
 * Describe :
 *
 * @author chendong
 */
public interface EventHandler {

    void intercept(String event, LxAdapter adapter, Object extra);
}
