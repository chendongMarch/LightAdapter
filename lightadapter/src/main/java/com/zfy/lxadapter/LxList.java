package com.zfy.lxadapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.zfy.lxadapter.data.LxModel;
import com.zfy.lxadapter.diff.DiffableList;
import com.zfy.lxadapter.helper.LxSource;
import com.zfy.lxadapter.helper.LxTypedHelper;
import com.zfy.lxadapter.listener.EventSubscriber;

import java.util.HashMap;
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
    protected LxTypedHelper                helper;

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
     * @param event      事件
     * @param subscriber 观察者
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
     *
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

    public void update(LxSource source) {
        update(source.asModels());
    }

    public LxTypedHelper getHelper() {
        if (helper == null) {
            helper = new LxTypedHelper(this);
        }
        return helper;
    }
}
