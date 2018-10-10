package com.zfy.component.basic.foundation;

import android.util.SparseArray;

import com.march.common.mgrs.IMgr;

import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.ListCompositeDisposable;

/**
 * CreateAt : 2018/9/27
 * Describe : 请求队列统一管理
 *
 * @author chendong
 */
public class ApiQueueMgr implements IMgr {

    private SparseArray<ListCompositeDisposable> mDisposableMap;

    public ApiQueueMgr() {
        mDisposableMap = new SparseArray<>();
    }


    // 添加一个请求
    public void addRequest(Object host, Disposable disposable) {
        addRequest(host.hashCode(), disposable);
    }

    public void addRequest(int tag, Disposable disposable) {
        if (tag <= 0) {
            return;
        }
        ListCompositeDisposable disposableContainer = mDisposableMap.get(tag);
        if (disposableContainer == null) {
            disposableContainer = new ListCompositeDisposable();
            mDisposableMap.put(tag, disposableContainer);
        }
        disposableContainer.add(disposable);
    }

    // 删除一个请求成功或失败的请求
    public void removeRequest(Object host, Disposable disposable) {
        removeRequest(host.hashCode(), disposable);
    }

    public void removeRequest(int tag, Disposable disposable) {
        if (tag <= 0) {
            return;
        }
        ListCompositeDisposable disposableContainer = mDisposableMap.get(tag);
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
        disposableContainer.delete(disposable);
    }

    public void cancelRequest(Object host) {
        cancelRequest(host.hashCode());
    }

    // 取消指定 tag 的请求
    public void cancelRequest(int tag) {
        if (tag <= 0) {
            return;
        }
        ListCompositeDisposable disposableContainer = mDisposableMap.get(tag);
        if (disposableContainer != null) {
            if (!disposableContainer.isDisposed()) {
                disposableContainer.dispose();
            }
            mDisposableMap.remove(tag);
        }
    }

    // 取消所有请求
    public void cancelAllRequest() {
        for (int i = 0; i < mDisposableMap.size(); i++) {
            cancelRequest(mDisposableMap.keyAt(i));
        }
    }

    @Override
    public void recycle() {
        cancelAllRequest();
    }

    @Override
    public boolean isRecycled() {
        return mDisposableMap.size() == 0;
    }
}
