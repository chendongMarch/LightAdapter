package com.zfy.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.zfy.adapter.data.LxModel;
import com.zfy.adapter.diff.DiffableList;
import com.zfy.adapter.function._Consumer;
import com.zfy.adapter.function._Function;
import com.zfy.adapter.function._Predicate;
import com.zfy.adapter.helper.LxTypeSplit;
import com.zfy.adapter.listener.EventHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CreateAt : 2018/11/8
 * Describe : 对外支持更新的 List
 *
 * @author chendong
 */
public class LxList extends DiffableList<LxModel> {

    private LxTypeSplit               typeSplit;
    private Map<String, EventHandler> interceptors;
    private LxAdapter                 adapter;

    public LxList(boolean async) {
        super(async);
        typeSplit = new LxTypeSplit();
    }

    public LxList() {
        this(false);
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        super.setAdapter(adapter);
        this.adapter = (LxAdapter) adapter;
        typeSplit.setAdapter(this.adapter, this.adapter.hasExtType);
    }

    public @NonNull
    LxList getContentTypeData() {
        return typeSplit.getContentTypeData();
    }

    public @NonNull
    LxList getExtTypeData(int viewType) {
        return typeSplit.getExtTypeData(viewType);
    }

    public boolean hasType(int viewType) {
        return !typeSplit.getExtTypeData(viewType).isEmpty();
    }

    public void addEventHandler(String event, EventHandler interceptor) {
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
        EventHandler eventInterceptor = interceptors.get(event);
        if (eventInterceptor != null) {
            eventInterceptor.intercept(event, adapter, extra);
        }
    }

    public <R> List<R> filterTo(_Predicate<LxModel> test, _Function<LxModel, R> function) {
        List<R> l = new ArrayList<>();
        for (LxModel t : this) {
            if (test.test(t)) {
                l.add(function.map(t));
            }
        }
        return l;
    }


    public static abstract class UnpackConsumer<T> implements _Consumer<LxModel> {

        @Override
        public void accept(LxModel model) {
            onAccept(model.unpack());
        }

        protected abstract void onAccept(T t);
    }
}
