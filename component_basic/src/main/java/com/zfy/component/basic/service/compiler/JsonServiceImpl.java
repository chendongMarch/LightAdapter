package com.zfy.component.basic.service.compiler;


import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.zfy.component.basic.service.BuildInService;
import com.zfy.component.basic.service.GsonServiceDelegateKt;
import com.zfy.component.basic.service.IJsonService;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;


/**
 * CreateAt : 2018/9/10
 * Describe :
 *
 * @author chendong
 */
@Route(path = BuildInService.JSON, name = "JSON解析服务")
public class JsonServiceImpl implements IJsonService {

    private GsonServiceDelegateKt mGsonService = new GsonServiceDelegateKt();

    @Nullable
    @Override
    public String toJson(@Nullable Object obj) {
        return mGsonService.toJson(obj);
    }

    @Nullable
    @Override
    public <T> T toObj(@Nullable String json, @Nullable Class<T> clazz) {
        return mGsonService.toObj(json, clazz);
    }

    @Nullable
    @Override
    public <T> List<T> toList(@Nullable String json, @Nullable Class<T> clazz) {
        return mGsonService.toList(json, clazz);

    }

    @Nullable
    @Override
    public <K, V> Map<K, V> toMap(@Nullable String json, @Nullable Class<K> kClazz, @Nullable Class<V> vClazz) {
        return mGsonService.toMap(json, kClazz, vClazz);

    }

    @Override
    public <T> T json2Object(String input, Class<T> clazz) {
        return mGsonService.json2Object(input, clazz);

    }

    @Override
    public String object2Json(Object instance) {
        return mGsonService.object2Json(instance);
    }

    @Override
    public <T> T parseObject(String input, Type clazz) {
        return mGsonService.parseObject(input, clazz);
    }

    @Override
    public void init(Context context) {
        mGsonService.init(context);
    }
}
