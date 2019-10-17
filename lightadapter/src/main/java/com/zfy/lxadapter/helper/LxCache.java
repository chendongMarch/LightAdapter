package com.zfy.lxadapter.helper;

import android.os.Bundle;
import android.support.annotation.IdRes;

import com.zfy.lxadapter.data.LxModel;
import com.zfy.lxadapter.function._Function;

import java.util.HashMap;
import java.util.Map;

/**
 * CreateAt : 2019-10-17
 * Describe : 避免列表重复计算
 *
 * @author chendong
 */
public class LxCache<D> {

    private Map<String, _Function<D, ?>> mapperMap;

    public void addMapper(@IdRes int id, _Function<D, Object> mapper) {
        if (mapperMap == null) {
            mapperMap = new HashMap<>();
        }
        mapperMap.put(String.valueOf(id), mapper);
    }

    public String getString(@IdRes int id, LxModel model) {
        String key = String.valueOf(id);
        Bundle extra = model.getExtra();
        if (!extra.containsKey(key)) {
            _Function<D, ?> mapper = mapperMap.get(key);
            if (mapper == null) {
                throw new IllegalStateException("CAN NOT FIND MAPPER");
            }
            String value = (String) mapper.map(model.unpack());
            extra.putString(key, value);
            return value;
        } else {
            return extra.getString(key, "");
        }
    }


    public int getInt(@IdRes int id, LxModel model) {
        String key = String.valueOf(id);
        Bundle extra = model.getExtra();
        if (!extra.containsKey(key)) {
            _Function<D, ?> mapper = mapperMap.get(key);
            if (mapper == null) {
                throw new IllegalStateException("CAN NOT FIND MAPPER");
            }
            int value = (Integer) mapper.map(model.unpack());
            extra.putInt(key, value);
            return value;
        } else {
            return extra.getInt(key);
        }
    }

    public long getLong(@IdRes int id, LxModel model) {
        String key = String.valueOf(id);
        Bundle extra = model.getExtra();
        if (!extra.containsKey(key)) {
            _Function<D, ?> mapper = mapperMap.get(key);
            if (mapper == null) {
                throw new IllegalStateException("CAN NOT FIND MAPPER");
            }
            long value = (Long) mapper.map(model.unpack());
            extra.putLong(key, value);
            return value;
        } else {
            return extra.getLong(key);
        }
    }

    public boolean getBool(@IdRes int id, LxModel model) {
        String key = String.valueOf(id);
        Bundle extra = model.getExtra();
        if (!extra.containsKey(key)) {
            _Function<D, ?> mapper = mapperMap.get(key);
            if (mapper == null) {
                throw new IllegalStateException("CAN NOT FIND MAPPER");
            }
            boolean value = (Boolean) mapper.map(model.unpack());
            extra.putBoolean(key, value);
            return value;
        } else {
            return extra.getBoolean(key);
        }
    }


    public float getFloat(@IdRes int id, LxModel model) {
        String key = String.valueOf(id);
        Bundle extra = model.getExtra();
        if (!extra.containsKey(key)) {
            _Function<D, ?> mapper = mapperMap.get(key);
            if (mapper == null) {
                throw new IllegalStateException("CAN NOT FIND MAPPER");
            }
            float value = (Float) mapper.map(model.unpack());
            extra.putFloat(key, value);
            return value;
        } else {
            return extra.getFloat(key);
        }
    }

    public static void remove(@IdRes int id, LxModel model) {
        model.getExtra().remove(String.valueOf(id));
    }
}
