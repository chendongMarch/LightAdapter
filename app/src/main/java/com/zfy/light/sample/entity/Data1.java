package com.zfy.light.sample.entity;

import com.zfy.adapter.data.Copyable;
import com.zfy.adapter.data.Diffable;

/**
 * CreateAt : 2018/11/9
 * Describe :
 *
 * @author chendong
 */
public class Data1 implements Diffable<Data1>, Copyable<Data1> {

    public String title;

    public static Data1 sample(String title) {
        Data1 data = new Data1();
        data.title = title;
        return data;
    }

    @Override
    public Data1 copyNewOne() {
        return Data1.sample(title);
    }
}
