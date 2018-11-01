package com.zfy.adapter.model;

import android.os.Parcelable;

/**
 * CreateAt : 2018/11/1
 * Describe :
 *
 * @author chendong
 */
public interface Diffable<T> extends Parcelable {


    boolean areItemsTheSame(T newItem);

    boolean areContentsTheSame(T newItem);

}
