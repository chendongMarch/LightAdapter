package com.zfy.adapter.contract;

import com.zfy.adapter.callback.EventCallback;

/**
 * CreateAt : 2018/12/13
 * Describe :
 *
 * @author chendong
 */
public interface IEventContract<D> {

    void setClickEvent(EventCallback<D> clickCallback);

    void setLongPressEvent(EventCallback<D> longPressCallback);

    void setDbClickEvent(EventCallback<D> dbClickCallback);

    void setChildViewClickEvent(EventCallback<D> childViewClickEvent);

    void setChildViewLongPressEvent(EventCallback<D> dbClickCallback);
}
