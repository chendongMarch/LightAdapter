package com.zfy.adapter.diff;

import java.util.List;

/**
 * CreateAt : 2019-09-09
 * Describe :
 *
 * @author chendong
 */
public class UpdateResult<E> {

    public List<E> list;
    public E       item;
    public int     index;
    public boolean removeResult;
    public boolean addResult;


    public UpdateResult<E> ofAddResult(List<E> list, boolean result) {
        this.list = list;
        this.addResult = result;
        return this;
    }


}
