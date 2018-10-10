package com.zfy.component.basic.foundation.api.interceptors

import io.reactivex.annotations.NonNull
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * CreateAt : 2018/9/8
 * Describe :
 *
 * @author chendong
 */
open class AbstractInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        return try {
            proceedResponse(chain.proceed(proceedRequest(chain.request())))
        } catch (e: Exception) {
            chain.proceed(chain.request())
        }
    }

    @NonNull
    open fun proceedRequest(request: Request): Request {
        return request
    }

    @NonNull
    open fun proceedResponse(response: Response): Response {
        return response
    }
}