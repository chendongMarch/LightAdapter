package com.zfy.adapter.delegate.refs;

import android.support.annotation.LayoutRes;

import com.zfy.adapter.callback.BindCallback;

/**
 * CreateAt : 2018/11/10
 * Describe :
 *
 * @author chendong
 */
public interface FakeRef<D> {

    void showFake(int count, @LayoutRes int layoutId, BindCallback<D> callback);

    void hideFake();
}
