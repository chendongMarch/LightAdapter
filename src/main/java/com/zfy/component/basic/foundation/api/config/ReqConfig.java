package com.zfy.component.basic.foundation.api.config;

/**
 * CreateAt : 2018/9/29
 * Describe :
 *
 * @author chendong
 */
public class ReqConfig {

    public boolean enableLoading ;

    private ReqConfig() {
    }

    public static ReqConfig loading(boolean loading) {
        ReqConfig reqConfig = new ReqConfig();
        reqConfig.enableLoading = loading;
        return reqConfig;
    }
}
