package com.zfy.lxadapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.zfy.lxadapter.data.LxModel;
import com.zfy.lxadapter.diff.DiffableList;
import com.zfy.lxadapter.function._Function;
import com.zfy.lxadapter.function._Predicate;
import com.zfy.lxadapter.listener.AdapterEventDispatcher;

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

    protected LxAdapter                           adapter;
    private   Map<String, AdapterEventDispatcher> interceptors;
    protected int                                 dataStartPosition;

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

    public void addAdapterEventDispatcher(String event, AdapterEventDispatcher interceptor) {
        if (interceptors == null) {
            interceptors = new HashMap<>(4);
        }
        interceptors.put(event, interceptor);
    }

    public void publishEvent(String event) {
        this.publishEvent(event, null);
    }

    public void publishEvent(String event, Object extra) {
        if (interceptors == null || interceptors.isEmpty()) {
            return;
        }
        AdapterEventDispatcher eventInterceptor = interceptors.get(event);
        if (eventInterceptor != null) {
            eventInterceptor.dispatch(event, adapter, extra);
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

    /*default*/ int getDataStartPosition() {
        return dataStartPosition;
    }


    @NonNull
    public LxList getContentTypeData() {
        return this;
    }

    @NonNull
    public LxList getExtTypeData(int viewType) {
        return new LxList();
    }

    public boolean hasType(int viewType) {
        return getExtTypeData(viewType).isEmpty();
    }


    protected boolean adapterHasExtType() {
        return adapter.hasExtType;
    }
}
