package com.zfy.adapter.model;

/**
 * CreateAt : 2017.09.28
 * Describe : 针对id列表使用
 *
 * @author chendong
 */
public class Ids {

    private int[] viewIds;

    public Ids(int... resIds) {
        viewIds = resIds;
    }

    public int[] getViewIds() {
        return viewIds;
    }

    public static Ids all(int... resIds) {
        return new Ids(resIds);
    }
}
