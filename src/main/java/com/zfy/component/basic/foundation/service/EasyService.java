package com.zfy.component.basic.foundation.service;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * CreateAt : 2018/1/12
 * Describe : 一些内置的服务
 *
 * @author chendong
 */
public interface EasyService {

    @GET
    Observable<ResponseBody> download(@Url String url);

}
