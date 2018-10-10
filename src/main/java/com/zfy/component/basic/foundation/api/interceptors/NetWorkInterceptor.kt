package com.zfy.component.basic.foundation.api.interceptors

import com.march.common.exts.NetX
import com.zfy.component.basic.exception.RequestException
import okhttp3.Request

/**
 * CreateAt : 2018/9/9
 * Describe :
 *
 * @author chendong
 */
class NetWorkInterceptor : AbstractInterceptor() {

    override fun proceedRequest(request: Request): Request {
        if (!NetX.isNetworkConnected()) {
            throw RequestException(RequestException.ERROR_NETWORK)
        }
        return super.proceedRequest(request)
    }
}