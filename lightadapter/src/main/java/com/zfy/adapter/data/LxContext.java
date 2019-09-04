package com.zfy.adapter.data;

import com.zfy.adapter.Lx;
import com.zfy.adapter.LxVh;

import java.util.List;

/**
 * CreateAt : 2019-08-31
 * Describe :
 *
 * @author chendong
 */
public class LxContext {

    public               Object       data;
    public               LxModel      model;
    public               int          position;
    public               LxVh         holder;
    public               int          viewType;
    @Lx.EventType public int          eventType;
    public               List<String> payloads;

}
