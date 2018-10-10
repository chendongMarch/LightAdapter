package com.zfy.component.basic.foundation.interceptors;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * CreateAt : 2018/1/12
 * Describe : base Interceptor
 *
 * @author chendong
 */
public abstract class AbstractInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        return proceedResponse(chain.proceed(proceedRequest(chain.request())));
    }

    protected @NonNull
    Request proceedRequest(Request request) {
        return request;
    }

    protected @NonNull
    Response proceedResponse(Response response) {
        return response;
    }
}
