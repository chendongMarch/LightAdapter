package com.zfy.lxadapter.listener;

import com.zfy.lxadapter.data.LxContext;

/**
 * CreateAt : 2019-08-31
 * Describe :
 *
 * @author chendong
 */
public interface OnItemEventListener {
    void onEvent(LxContext context, int eventType);
}
