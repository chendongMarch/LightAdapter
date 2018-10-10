package com.zfy.component.basic.foundation.api.interceptors

import com.zfy.component.basic.foundation.api.Api
import okhttp3.Request

/**
 * CreateAt : 2018/9/8
 * Describe :
 *
 * @author chendong
 */
class HeaderInterceptor : AbstractInterceptor() {

    // 到达 netWorkInterceptor 时，默认 header 已经添加，不能使用替换的方式，要使用 addHeader 的方式
    // 到达 interceptor 时，还没有添加默认 header ,可以直接替换原来的 header ，后面会追加默认 header
    override fun proceedRequest(request: Request): Request {
        val builder = request.newBuilder()
        val headers = Api.initConfig.commHeaders
        if (headers.isEmpty()) {
            return super.proceedRequest(request)
        }
        for (key in headers.keys) {
            val value = headers[key] ?: continue
            builder.addHeader(key, value)
        }
        return builder.build()
    }
}