package com.zfy.adapter.diff;

import android.support.annotation.MainThread;
import android.support.annotation.Nullable;

import com.zfy.adapter.data.Diffable;

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

    List<E> list();

}
