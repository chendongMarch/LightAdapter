package com.zfy.component.basic.foundation.api.interceptors;


import com.march.common.exts.EmptyX;
import com.zfy.component.basic.foundation.api.Api;

import java.util.Map;

import okhttp3.Request;

/**
 * CreateAt : 2017/7/1
 * Describe : 添加全局 header
 *
 * @author chendong
 */
public final class HeaderInterceptor extends AbstractInterceptor {

    // 到达 netWorkInterceptor 时，默认 header 已经添加，不能使用替换的方式，要使用 addHeader 的方式
    // 到达 interceptor 时，还没有添加默认 header ,可以直接替换原来的 header ，后面会追加默认 header
    @Override
    public Request proceedRequest(Request request) {
        Request.Builder builder = request.newBuilder();
        Map<String, String> headers = Api.config().getHeaders();
        if (EmptyX.isEmpty(headers)) {
            return super.proceedRequest(request);
        }
        for (String key : headers.keySet()) {
            String value = headers.get(key);
            if(!EmptyX.isEmpty(value)) {
                builder.addHeader(key, value);
            }
        }
        return builder.build();
    }
}
