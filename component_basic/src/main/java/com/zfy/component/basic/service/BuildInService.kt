package com.zfy.component.basic.service

import android.content.Context
import android.widget.ImageView
import com.alibaba.android.arouter.facade.service.SerializationService
import com.bumptech.glide.request.RequestOptions
import com.zfy.component.basic.ComponentX
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * CreateAt : 2018/9/8
 * Describe :
 *
 * @author chendong
 */

/**
 * 内建服务 key
 */
object BuildInService {

    const val IMAGE = "/basic/image"
    const val JSON = "/basic/json"
    const val API = "/biz/api"

    @JvmStatic
    fun image(): IImageService {
        return ComponentX.discover(IImageService::class.java)
    }

    @JvmStatic
    fun json(): IJsonService {
        return ComponentX.discover(IJsonService::class.java)
    }
}

/**
 * 图片处理服务
 */
interface IImageService : IService {
    fun load(context: Context?, url: String?, imageView: ImageView?)
    fun load(context: Context?, url: String?, imageView: ImageView?, width: Int = -1, height: Int = -1)
    fun loadGlide(context: Context?, url: String?, imageView: ImageView?, opts: RequestOptions?)
}

/**
 * json 解析服务
 */
interface IJsonService : SerializationService {
    fun toJson(obj: Any?): String?
    fun <T> toObj(json: String?, clazz: Class<T>?): T?
    fun <T> toList(json: String?, clazz: Class<T>?): MutableList<T>?
    fun <K, V> toMap(json: String?, kClazz: Class<K>?, vClazz: Class<V>?): MutableMap<K, V>?
}


/**
 * 网络请求初始化服务
 */
interface IApiInitService : IService {

    data class HttpConfig(
            val baseUrl: String,
            val domainBaseUrlMapping: Map<String, String>,
            val commHeaders: Map<String, String>
    )

    fun readConfig(): HttpConfig = HttpConfig("", mapOf(), mapOf())

    fun buildOkHttp(builder: OkHttpClient.Builder): OkHttpClient.Builder = builder

    fun buildRetrofit(builder: Retrofit.Builder): Retrofit.Builder = builder
}