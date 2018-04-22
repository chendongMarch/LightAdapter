package com.march.lightadapter.helper;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * CreateAt : 2018/4/22
 * Describe :
 *
 * @author chendong
 */
public class LightManager {
    public static LinearLayoutManager vLinear(Context context) {
        return new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
    }
}
