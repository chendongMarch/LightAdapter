package com.zfy.component.basic.foundation.api.observers;

import com.zfy.component.basic.foundation.api.Api;
import com.zfy.component.basic.foundation.api.config.ReqConfig;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * CreateAt : 2018/9/29
 * Describe :
 *
 * @author chendong
 */
public class Observers {

    @SuppressWarnings("unchecked")
    public static <D> ApiObserver<D> make(Object host) {
        return Api.getInst().getObserverMaker().apply(host);
    }


    public static <D> ApiObserver<D> make(Object host, Consumer<D> nextConsumer) {
        ApiObserver<D> observer = make(host);
        observer.setNextConsumer(nextConsumer);
        return observer;
    }


    public static <D> ApiObserver<D> make(Object host, Consumer<D> nextConsumer, Consumer<Throwable> errorConsumer) {
        ApiObserver<D> observer = make(host);
        observer.setNextConsumer(nextConsumer);
        observer.setErrorConsumer(errorConsumer);
        return observer;
    }

    public static <D> ApiObserver<D> make(Object host, Consumer<D> nextConsumer, Consumer<Throwable> errorConsumer, Action finishAction) {
        ApiObserver<D> observer = make(host);
        observer.setNextConsumer(nextConsumer);
        observer.setErrorConsumer(errorConsumer);
        observer.setFinishAction(finishAction);
        return observer;
    }

    public static <D> ApiObserver<D> make(Object host, ReqConfig reqConfig, Consumer<D> nextConsumer) {
        ApiObserver<D> observer = make(host);
        observer.setNextConsumer(nextConsumer);
        observer.setRequestConfig(reqConfig);
        return observer;
    }

    public static <D> ApiObserver<D> make(Object host, ReqConfig reqConfig, Consumer<D> nextConsumer, Consumer<Throwable> errorConsumer) {
        ApiObserver<D> observer = make(host);
        observer.setNextConsumer(nextConsumer);
        observer.setErrorConsumer(errorConsumer);
        observer.setRequestConfig(reqConfig);
        return observer;
    }

    public static <D> ApiObserver<D> make(Object host, ReqConfig reqConfig, Consumer<D> nextConsumer, Consumer<Throwable> errorConsumer, Action finishAction) {
        ApiObserver<D> observer = make(host);
        observer.setNextConsumer(nextConsumer);
        observer.setErrorConsumer(errorConsumer);
        observer.setFinishAction(finishAction);
        observer.setRequestConfig(reqConfig);
        return observer;
    }

}
