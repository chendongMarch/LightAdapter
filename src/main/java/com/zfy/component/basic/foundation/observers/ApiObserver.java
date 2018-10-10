package com.zfy.component.basic.foundation.observers;


import com.zfy.component.basic.foundation.Api;
import com.zfy.component.basic.foundation.config.ReqConfig;
import com.zfy.component.basic.foundation.exception.ApiException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * CreateAt : 2017/7/5
 * Describe : 观察类
 *
 * @author chendong
 */
public class ApiObserver<D> implements Observer<D> {

    public static final String TAG = ApiObserver.class.getSimpleName();

    protected Disposable disposable;

    private int tag;

    protected Consumer<D>         nextConsumer;
    protected Consumer<Throwable> errorConsumer;
    protected Action              finishAction;
    protected ReqConfig           requestConfig;

    ApiObserver(Object host) {
        this.tag = host.hashCode();
        this.requestConfig = ReqConfig.loading(false);
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        disposable = d;
        Api.queue().addRequest(tag, disposable);
    }

    @Override
    public void onNext(@NonNull D t) {
        if (nextConsumer != null) {
            try {
                nextConsumer.accept(t);
            } catch (Exception e) {
                onError(e);
            }
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
        ApiException.handleApiException(e);
        if (errorConsumer != null) {
            try {
                errorConsumer.accept(e);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        onFinish();
    }

    @Override
    public void onComplete() {
        onFinish();
    }

    // onError or onComplete
    protected void onFinish() {
        Api.queue().removeRequest(tag, disposable);
        if (finishAction != null) {
            try {
                finishAction.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
