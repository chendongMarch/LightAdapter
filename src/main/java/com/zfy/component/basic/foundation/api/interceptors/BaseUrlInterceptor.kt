package com.zfy.component.basic.foundation.api.interceptors

import com.zfy.component.basic.foundation.api.Api
import okhttp3.HttpUrl
import okhttp3.Request


/**
 * CreateAt : 2018/9/8
 * Describe :
 *
 * @author chendong
 */
class BaseUrlInterceptor : AbstractInterceptor() {
    override fun proceedRequest(request: Request): Request {
        val header = request.header(Api.DOMAIN_KEY)
        val baseUrl = Api.initConfig.domainBaseUrlMapping[header] ?: return super.proceedRequest(request)
        val mapUrl = HttpUrl.parse(baseUrl) ?: return super.proceedRequest(request)
        val httpUrl = request.url().newBuilder()
                .scheme(mapUrl.scheme())
                .host(mapUrl.host())
                .port(mapUrl.port())
                .build()
        return request.newBuilder().url(httpUrl).build()
    }
}