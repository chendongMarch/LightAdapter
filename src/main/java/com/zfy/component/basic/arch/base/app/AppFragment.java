package com.zfy.component.basic.arch.base.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zfy.component.basic.arch.base.IBaseView;
import com.zfy.component.basic.arch.base.IElegantView;
import com.zfy.component.basic.arch.base.IViewInit;
import com.zfy.component.basic.arch.base.ViewConfig;

import org.greenrobot.eventbus.Subscribe;

/**
 * CreateAt : 2018/10/11
 * Describe :
 *
 * @author chendong
 */
public abstract class AppFragment extends Fragment implements IElegantView, IViewInit, IBaseView {

    protected View mContentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = getAppDelegate().bindFragment(this, inflater, container);
        init();
        return mContentView;
    }


    // view init

    @Override
    public ViewConfig getViewConfig() {
        return null;
    }

    // elegant view

    @Override
    public void startActivity(Class clz) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.startActivity(new Intent(activity, clz));
        }
    }

    @Override
    public Bundle getData() {
        Bundle arguments = getArguments();
        if (arguments == null) {
            return new Bundle();
        }
        return arguments;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getAppDelegate() != null) {
            getAppDelegate().onDestroy();
        }
    }
    @Subscribe
    public void ignoreEvent(AppDelegate thiz) {

    }
}
