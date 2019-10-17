package com.zfy.lxadapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.zfy.lxadapter.data.LxModel;
import com.zfy.lxadapter.diff.DiffableList;
import com.zfy.lxadapter.function._Function;
import com.zfy.lxadapter.function._Predicate;
import com.zfy.lxadapter.listener.EventSubscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CreateAt : 2019-10-11
 * Describe :
 *
 * @author chendong
 */
public class LxList extends DiffableList<LxModel> {

    protected LxAdapter                    adapter;
    private   Map<String, EventSubscriber> subscribers;
    protected int                          contentStartPosition;

    // 是否是异步更新
    protected boolean async;

    public LxList(boolean async) {
        super(async);
        this.async = async;
    }

    public LxList() {
        this(false);
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        super.setAdapter(adapter);
        this.adapter = (LxAdapter) adapter;
    }

    @Override
    public LxModel get(int i) {
        int size = size();
        if (size == 0) {
            return null;
        }
        return super.get(adapter.isInfinite ? i % size : i);
    }

    /**
     * 订阅事件
     *
     * @param event
     * @param subscriber
     */
    public void subscribe(String event, EventSubscriber subscriber) {
        if (subscribers == null) {
            subscribers = new HashMap<>(4);
        }
        subscribers.put(event, subscriber);
    }

    /**
     * 发送事件
     *
     * @param event 事件名
     */
    public void postEvent(String event) {
        this.postEvent(event, null);
    }

    /**
     * 发送事件
     * @param event 事件名
     * @param extra 事件参数
     */
    public void postEvent(String event, Object extra) {
        if (subscribers == null || subscribers.isEmpty()) {
            return;
        }
        EventSubscriber eventInterceptor = subscribers.get(event);
        if (eventInterceptor != null) {
            eventInterceptor.subscribe(event, adapter, extra);
        }
    }

    public <ReturnType> List<ReturnType> filterTo(_Predicate<LxModel> test, _Function<LxModel, ReturnType> function) {
        List<ReturnType> l = new ArrayList<>();
        for (LxModel t : this) {
            if (test.test(t)) {
                l.add(function.map(t));
            }
        }
        return l;
    }

    public <ReturnType> List<ReturnType> filterTo(_Predicate<LxModel> test) {
        List<ReturnType> l = new ArrayList<>();
        for (LxModel t : this) {
            if (test.test(t)) {
                l.add(t.unpack());
            }
        }
        return l;
    }

    /*default*/ int getContentStartPosition() {
        return contentStartPosition;
    }


    @NonNull
    public LxList getContentTypeData() {
        return this;
    }

    @NonNull
    public LxList getExtTypeData(int viewType) {
        return new LxList();
    }

    protected boolean adapterHasExtType() {
        return adapter.hasExtType;
    }
}
