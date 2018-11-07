package com.zfy.component.basic.foundation.api.exception;

import com.google.gson.JsonParseException;
import com.march.common.exts.ToastX;


/**
 * CreateAt : 2018/9/27
 * Describe : 异常
 *
 * @author chendong
 */
public class ApiException extends IllegalStateException {

    public static final int ERR_NETWORK      = 1; // 网络没有链连接
    public static final int ERR_DATA_PROCESS = 2; // 数据处理错误

    private int    code;
    private String msg;

    public ApiException(String message) {
        super(message);
    }

    public ApiException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ApiException(int errorCode) {
        code = errorCode;
        switch (code) {
            case ERR_NETWORK:
                msg = "网络未连接";
                break;
            case ERR_DATA_PROCESS:
                msg = "数据处理错误";
                break;
            default:
                break;
        }
    }

    public static boolean handleApiException(Throwable e) {
        if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            switch (apiException.code) {
                case ERR_NETWORK:
                    ToastX.show("网络未连接");
                    return true;
                default:
                    break;
            }
        }
        if (e instanceof JsonParseException) {
            ToastX.show(" 数据解析失败");
            e.printStackTrace();
            return true;
        }

        return false;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
