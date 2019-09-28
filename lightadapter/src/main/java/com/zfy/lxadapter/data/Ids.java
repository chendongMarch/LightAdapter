package com.zfy.lxadapter.data;

import android.support.annotation.IdRes;

/**
 * CreateAt : 2017.09.28
 * Describe : 针对 id 列表使用
 *
 * @author chendong
 */
public class Ids {

    private static final ThreadLocal<Ids> sIds = new ThreadLocal<Ids>() {
        @Override
        protected Ids initialValue() {
            return new Ids();
        }
    };

    private int[] viewIds;

    private Ids() {

    }

    public int[] ids() {
        return viewIds;
    }

    private Ids obtain(@IdRes int... resIds) {
        this.viewIds = resIds;
        return this;
    }

    public static Ids all(@IdRes int... resIds) {
        return sIds.get().obtain(resIds);
    }
}
