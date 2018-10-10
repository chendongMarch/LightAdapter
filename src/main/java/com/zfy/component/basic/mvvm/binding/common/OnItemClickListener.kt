package com.zfy.component.basic.mvvm.binding.common

import android.view.View

/**
 * CreateAt : 2018/9/13
 * Describe :
 *
 * @author chendong
 */
interface OnItemClickListener<T> {
    fun onClick(view: View, data: T)
}