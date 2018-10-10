package com.zfy.component.basic.foundation.config;

import com.march.common.exts.EmptyX;

import java.util.HashMap;
import java.util.Map;

/**
 * CreateAt : 2018/1/12
 * Describe :
 * - 多 baseUrl 支持
 * - common Header 支持
 *
 * @author chendong
 */
public class ApiConfig {

    private String baseUrl;
    private Map<String, String> baseUrlMap = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();

    private ApiConfig(String baseUrl) {
        if (EmptyX.isEmpty(baseUrl)) {
            throw new IllegalArgumentException();
        }
        this.baseUrl = baseUrl;
    }

    public static ApiConfig create(String baseUrl) {
        return new ApiConfig(baseUrl);
    }

    // 添加多 baseUrl,使用 domain 区分
    public ApiConfig addBaseUrl(String domain, String baseUrl) {
        if (!EmptyX.isAnyEmpty(domain, baseUrl)) {
            this.baseUrlMap.put(domain, baseUrl);
        }
        return this;
    }

    // 添加通用 header
    public ApiConfig addHeader(String key, String value) {
        if (!EmptyX.isAnyEmpty(key, value)) {
            this.headers.put(key, value);
        }
        return this;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public Map<String, String> getBaseUrlMap() {
        return baseUrlMap;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
