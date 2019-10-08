package com.zfy.lxadapter.listener;

import com.zfy.lxadapter.LxAdapter;

/**
 * CreateAt : 2019-09-04
 * Describe :
 *
 * @author chendong
 */
public interface AdapterEventDispatcher {

    void dispatch(String event, LxAdapter adapter, Object extra);
}
