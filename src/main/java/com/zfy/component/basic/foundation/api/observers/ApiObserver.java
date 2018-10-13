package com.zfy.component.basic.foundation.api.observers;


import com.march.common.exts.ToastX;
import com.zfy.component.basic.foundation.api.Api;
import com.zfy.component.basic.foundation.api.config.ReqConfig;
import com.zfy.component.basic.foundation.api.exception.ApiException;

import com.march.common.Common;

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

    protected Disposable          disposable;
    protected Consumer<D>         nextConsumer;
    protected Consumer<Throwable> errorConsumer;
    protected Action              finishAction;
    protected ReqConfig           requestConfig;
    private   int                 tag;

    public ApiObserver(Object host) {
        this.tag = host.hashCode();
        this.requestConfig = ReqConfig.create();
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
        if (Common.exports.appConfig.DEBUG) {
            ToastX.showLong("请求错误 -- " + e.getMessage());
        }
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


    public void setNextConsumer(Consumer<D> nextConsumer) {
        this.nextConsumer = nextConsumer;
    }

    public void setErrorConsumer(Consumer<Throwable> errorConsumer) {
        this.errorConsumer = errorConsumer;
    }

    public void setFinishAction(Action finishAction) {
        this.finishAction = finishAction;
    }

    public void setRequestConfig(ReqConfig requestConfig) {
        this.requestConfig = requestConfig;
    }
}
