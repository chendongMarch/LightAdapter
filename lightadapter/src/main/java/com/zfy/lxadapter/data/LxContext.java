package com.zfy.lxadapter.data;

import android.os.Bundle;

import com.zfy.lxadapter.LxAdapter;
import com.zfy.lxadapter.LxViewHolder;

import java.util.List;

/**
 * CreateAt : 2019-08-31
 * Describe :
 *
 * @author chendong
 */
public class LxContext {

    public Object       data;
    public LxModel      model;
    public int          layoutPosition;
    public int          dataPosition;
    public LxViewHolder holder;
    public int          viewType;
    public List<String> payloads;
    public Bundle       condition;


    public void clear() {
        if (condition != null) {
            condition.clear();
            condition = null;
        }
        if (payloads != null) {
            payloads.clear();
            payloads = null;
        }
    }

    public int getDataPosition(LxAdapter adapter) {
        return adapter.getData().indexOf(model);
    }
}
