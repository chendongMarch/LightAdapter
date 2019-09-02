package com.zfy.adapter.listener;

import com.zfy.adapter.data.LxContext;

/**
 * CreateAt : 2019-08-31
 * Describe :
 *
 * @author chendong
 */
public interface OnItemEventListener {
    void onEvent(LxContext context, int eventType);
}
