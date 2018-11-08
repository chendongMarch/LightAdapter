package com.zfy.component.basic.foundation.api.config;

import java.util.HashMap;
import java.util.Map;

/**
 * CreateAt : 2018/9/29
 * Describe :
 *
 * @author chendong
 */
public class ReqConfig {

    private boolean             loadingEnable;
    private Map<String, Object> params;

    private ReqConfig() {
        params = new HashMap<>();
        loadingEnable = false;
    }

    public static ReqConfig create() {
        return new ReqConfig();
    }

    public ReqConfig loading(boolean loading) {
        loadingEnable = loading;
        return this;
    }

    public ReqConfig params(String key, Object value) {
        params.put(key, value);
        return this;
    }

    public boolean isLoadingEnable() {
        return loadingEnable;
    }

    public Map<String, Object> getParams() {
        return params;
    }
}
