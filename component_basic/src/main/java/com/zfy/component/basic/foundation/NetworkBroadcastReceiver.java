package com.zfy.component.basic.foundation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.zfy.component.basic.foundation.event.impl.NetworkEvent;

/**
 * CreateAt : 2018/10/16
 * Describe : 网络状态变化检测
 *
 * @author chendong
 */
public class NetworkBroadcastReceiver extends BroadcastReceiver {

    public static final int DELAY = 2000;

    private Runnable mNetChangedRunnable = NetworkEvent::post;
    private Handler  mHandler            = new Handler(Looper.getMainLooper());

    @Override
    public void onReceive(Context context, Intent intent) {
        synchronized (NetworkBroadcastReceiver.class) {
            mHandler.removeCallbacks(mNetChangedRunnable);
            mHandler.postDelayed(mNetChangedRunnable, DELAY);
        }
    }
}
