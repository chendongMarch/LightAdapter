package com.march.lightadapter.binder;

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
}
