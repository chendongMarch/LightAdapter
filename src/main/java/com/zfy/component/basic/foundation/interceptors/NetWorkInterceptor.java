package com.zfy.component.basic.foundation.interceptors;


import com.march.common.exts.NetX;
import com.zfy.component.basic.foundation.exception.ApiException;

import okhttp3.Request;

/**
 * CreateAt : 2017/7/1
 * Describe : 提前检测网络
 *
 * @author chendong
 */
public class NetWorkInterceptor extends AbstractInterceptor {

    @Override
    protected Request proceedRequest(Request request) {
        if (!NetX.isNetworkConnected()) {
            throw new ApiException(ApiException.ERR_NETWORK);
        }
        return super.proceedRequest(request);
    }
}