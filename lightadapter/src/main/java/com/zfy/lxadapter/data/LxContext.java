package com.zfy.lxadapter.data;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.zfy.lxadapter.LxList;
import com.zfy.lxadapter.LxViewHolder;

import java.util.List;

/**
 * CreateAt : 2019-08-31
 * Describe :
 *
 * @author chendong
 */
public class LxContext {

    /*hide*/
    public Object       data; // 包装的数据，
    public LxModel      model; // model 数据
    public LxList       list; // 数据源
    public LxViewHolder holder; // holder
    public int          layoutPosition; // 布局中的位置
    public int          dataPosition; // 数据位置
    public int          viewType; // 类型
    public int          bindStrategy; // 绑定类型
    public Context      context;

    @NonNull
    public List<String> payloads; // payloads 更新数据

    public String conditionKey; // 条件更新的 key
    @NonNull
    public Bundle conditionValue; // 条件更新的数据


    public void clear() {
        conditionValue.clear();
        payloads.clear();
        context = null;
        list = null;
    }

}
