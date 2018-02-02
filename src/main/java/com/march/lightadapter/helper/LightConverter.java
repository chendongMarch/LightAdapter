package com.march.lightadapter.helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 陈栋 on 16/4/8.
 * 功能: 数据转换
 */
public class LightConverter {

    public static <T> List<T> listOf(T... ts) {
        List<T> datas = new ArrayList<>();
        for (T t : ts) {
            if (t != null) {
                datas.add(t);
            }
        }
        return datas;
    }

    public static <T> List<T> arrayToList(T[] ts) {
        List<T> datas = new ArrayList<>();
        for (T t : ts) {
            if (t != null) {
                datas.add(t);
            }
        }
        return datas;
    }
}
