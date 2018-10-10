package com.zfy.component.basic.foundation.api

import android.content.Context
import android.util.SparseArray
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.Gson
import com.march.common.Common
import com.zfy.component.basic.ComponentX
import com.zfy.component.basic.foundation.api.converts.StringConvertFactory
import com.zfy.component.basic.foundation.api.interceptors.BaseUrlInterceptor
import com.zfy.component.basic.foundation.api.interceptors.HeaderInterceptor
import com.zfy.component.basic.foundation.api.interceptors.LogInterceptor
import com.zfy.component.basic.foundation.api.interceptors.NetWorkInterceptor
import com.zfy.component.basic.service.IApiInitService
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.ListCompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * CreateAt : 2018/9/8
 * Describe :
 *
 * @author chendong
 */
object Api {

    const val DOMAIN_KEY = "domain_key"

    // 初始化服务
    private val initService by lazy {
        ComponentX.discover(IApiInitService::class.java)
    }
    // 初始化配置
    val initConfig by lazy { initService.readConfig() }
    // 网络请求服务缓存
    private val serviceCache by lazy { mutableMapOf<String, Any>() }
    // retrofit
    private val retrofit by lazy {
        provideRetrofit(provideOkHttp(Common.app()))
    }
    // request queue
    private val disposableMap by lazy { SparseArray<ListCompositeDisposable>() }

    /**
     * 获取一个请求服务
     */
    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun <S> use(clazz: Class<S>): S {
        val cached = serviceCache[clazz.name]
        if (cached != null) {
            return cached as S
        }
        val service = retrofit.create(clazz)
        if (service != null) {
            serviceCache[clazz.name] = service as Any
        }
        if(service == null) {
            throw IllegalStateException("service is null")
        }
        return service
    }

    /**
     * 添加一个请求到队列管理中
     */
    @JvmStatic
    fun addRequest(tag: Int, disposable: Disposable) {
        var disposableContainer: ListCompositeDisposable? = disposableMap.get(tag)
        if (disposableContainer == null) {
            disposableContainer = ListCompositeDisposable()
            disposableMap.put(tag, disposableContainer)
        }
        disposableContainer.add(disposable)
    }

    /**
     * 从队列管理中删除一个请求
     */
    @JvmStatic
    fun removeRequest(tag: Int, disposable: Disposable) {
        val disposableContainer = disposableMap.get(tag)
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
        disposableContainer.delete(disposable)
    }

    /**
     * 取消指定的 tag 对应的请求
     */
    @JvmStatic
    fun cancelRequest(tag: Int) {
        val disposableContainer = disposableMap.get(tag)
        if (disposableContainer != null) {
            if (!disposableContainer.isDisposed) {
                disposableContainer.dispose()
            }
            disposableMap.remove(tag)
        }
    }

    /**
     * 取消队列中所有请求
     */
    @JvmStatic
    fun cancelAllRequest() {
        for (i in 0 until disposableMap.size()) {
            cancelRequest(disposableMap.keyAt(i))
        }
    }

    private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val builder = Retrofit.Builder()
        // client
        builder.client(okHttpClient)
        // baseUrl
        builder.baseUrl(Api.initConfig.baseUrl)
        // rxJava 调用 adapter
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        // 转换为 String
        builder.addConverterFactory(StringConvertFactory.create())
        // 转换为 Json Model
        builder.addConverterFactory(GsonConverterFactory.create(Gson()))

        return Api.initService.buildRetrofit(builder).build()
    }

    private fun provideOkHttp(context: Context): OkHttpClient {
        val builder = OkHttpClient.Builder()
        // 连接超时
        builder.connectTimeout((5 * 1000).toLong(), TimeUnit.MILLISECONDS)
        // 读超时
        builder.readTimeout((5 * 1000).toLong(), TimeUnit.MILLISECONDS)
        // 写超时
        builder.writeTimeout((5 * 1000).toLong(), TimeUnit.MILLISECONDS)
        // 失败后重试
        builder.retryOnConnectionFailure(true)
        // 检查网络
        builder.addInterceptor(NetWorkInterceptor())
        // 动态 base url
        builder.addInterceptor(BaseUrlInterceptor())
        // 用来添加全局 Header
        builder.addInterceptor(HeaderInterceptor())
        // 进行日志打印，扩展自 HttpLoggingInterceptor
        builder.addInterceptor(LogInterceptor())
        // cookie
        builder.cookieJar(PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context)))
        // token校验，返回 403 时
        // builder.authenticator(new TokenAuthenticator());
        return Api.initService.buildOkHttp(builder).build()
    }
}