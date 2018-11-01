package com.zfy.adapter.model;

import android.support.annotation.IdRes;

/**
 * CreateAt : 2017.09.28
 * Describe : 针对 id 列表使用
 *
 * @author chendong
 */
public class Ids {

    private int[] viewIds;

    private Ids(@IdRes int[] resIds) {
        viewIds = resIds;
    }

    public int[] ids() {
        return viewIds;
    }

    public Ids obtain(@IdRes int... resIds) {
        this.viewIds = resIds;
        return this;
    }

    public static Ids all(@IdRes int... resIds) {
        Ids ids = new Ids(resIds);
        return ids;
    }
}
