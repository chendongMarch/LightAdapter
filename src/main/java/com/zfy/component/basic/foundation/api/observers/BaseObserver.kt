package com.zfy.component.basic.foundation.api.observers

import android.content.Context
import com.march.common.model.WeakContext
import com.zfy.component.basic.foundation.api.Api
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * CreateAt : 2018/9/9
 * Describe :
 *
 * @author chendong
 */
open class BaseObserver<T>(context: Context) : Observer<T> {

    fun next(consumer: Consumer<T>): BaseObserver<T> {
        nextConsumer = consumer
        return this
    }

    fun error(consumer: Consumer<Throwable>): BaseObserver<T> {
        errorConsumer = consumer
        return this
    }

    fun complete(action: Action): BaseObserver<T> {
        completeAction = action
        return this
    }

    fun finish(action: Action): BaseObserver<T> {
        finishAction = action
        return this
    }

    private var nextConsumer: Consumer<T>? = null
    private var errorConsumer: Consumer<Throwable>? = null
    private var completeAction: Action? = null
    private var finishAction: Action? = null

    val ctxRef = WeakContext(context)
    val tag = context.hashCode()
    var disposable: Disposable? = null

    override fun onSubscribe(d: Disposable) {
        Api.addRequest(tag, d)
        disposable = d
    }

    override fun onNext(t: T) {
        onFinish()
        nextConsumer?.accept(t)
    }

    override fun onError(e: Throwable) {
        onFinish()
        errorConsumer?.accept(e)
    }

    override fun onComplete() {
        onFinish()
        completeAction?.run()
    }

    open fun onFinish() {
        Api.cancelRequest(tag)
        finishAction?.run()
    }
}