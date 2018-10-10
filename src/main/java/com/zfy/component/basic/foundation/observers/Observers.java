package com.zfy.component.basic.foundation.observers;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import xiongdixingqiu.haier.com.xiongdixingqiu.api.config.ReqConfig;

/**
 * CreateAt : 2018/9/29
 * Describe :
 *
 * @author chendong
 */
public class Observers {

    public static <D> BizObserver<D> make(Object host, Consumer<D> nextConsumer) {
        BizObserver<D> observer = new BizObserver<>(host);
        observer.nextConsumer = nextConsumer;
        return observer;
    }

    public static <D> BizObserver<D> make(Object host, Consumer<D> nextConsumer, Consumer<Throwable> errorConsumer) {
        BizObserver<D> observer = new BizObserver<>(host);
        observer.nextConsumer = nextConsumer;
        observer.errorConsumer = errorConsumer;
        return observer;
    }

    public static <D> BizObserver<D> make(Object host, Consumer<D> nextConsumer, Consumer<Throwable> errorConsumer, Action finishAction) {
        BizObserver<D> observer = new BizObserver<>(host);
        observer.nextConsumer = nextConsumer;
        observer.errorConsumer = errorConsumer;
        observer.finishAction = finishAction;
        return observer;
    }

    public static <D> BizObserver<D> make(Object host, ReqConfig reqConfig, Consumer<D> nextConsumer) {
        BizObserver<D> observer = new BizObserver<>(host);
        observer.nextConsumer = nextConsumer;
        observer.requestConfig = reqConfig;
        return observer;
    }

    public static <D> BizObserver<D> make(Object host, ReqConfig reqConfig, Consumer<D> nextConsumer, Consumer<Throwable> errorConsumer) {
        BizObserver<D> observer = new BizObserver<>(host);
        observer.nextConsumer = nextConsumer;
        observer.errorConsumer = errorConsumer;
        observer.requestConfig = reqConfig;
        return observer;
    }

    public static <D> BizObserver<D> make(Object host, ReqConfig reqConfig, Consumer<D> nextConsumer, Consumer<Throwable> errorConsumer, Action finishAction) {
        BizObserver<D> observer = new BizObserver<>(host);
        observer.nextConsumer = nextConsumer;
        observer.errorConsumer = errorConsumer;
        observer.finishAction = finishAction;
        observer.requestConfig = reqConfig;
        return observer;
    }

}
