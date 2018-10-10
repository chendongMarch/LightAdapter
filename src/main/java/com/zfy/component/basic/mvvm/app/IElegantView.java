package com.zfy.component.basic.mvvm.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

/**
 * CreateAt : 2017/12/7
 * Describe :
 * View 层基础方法封装
 *
 * @author chendong
 */
public interface IElegantView {

    Context getContext();

    Activity getActivity();

    void startActivity(Class clz);

    Bundle getBundle();
}
