package com.zfy.component.basic.app.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.march.common.pool.ExecutorsPool;

/**
 * CreateAt : 2017/12/7
 * Describe : View 层统一的实现的方法
 *
 * @author chendong
 */
public interface IElegantView {

    Context getContext();

    Activity getActivity();

    void launchActivity(Intent data, int code);

    @NonNull
    Bundle getData();

    void finishUI(Intent intent, int code);

    default void post(Runnable runnable, int delay) {
        ExecutorsPool.ui(runnable, delay);
    }
}
