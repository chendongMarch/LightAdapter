package com.zfy.lxadapter.diff;

import android.support.annotation.MainThread;
import android.support.annotation.Nullable;

import com.zfy.lxadapter.data.Diffable;

import java.util.List;

/**
 * CreateAt : 2019-09-08
 * Describe :
 *
 * @author chendong
 */
public interface IDiffDispatcher<E extends Diffable<E>> {

    @MainThread
    void update(@Nullable List<E> newItems);

    @MainThread
    default void update(@Nullable List<E> newItems, int flag) {
    }

    List<E> list();

}
