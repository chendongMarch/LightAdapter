package com.zfy.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.zfy.adapter.data.LxModel;
import com.zfy.adapter.diff.DiffableList;
import com.zfy.adapter.function._Consumer;
import com.zfy.adapter.function._Function;
import com.zfy.adapter.function._Predicate;
import com.zfy.adapter.helper.LxTypeSplit;
import com.zfy.adapter.listener.OnAdapterEventInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateAt : 2018/11/8
 * Describe : 对外支持更新的 List
 *
 * @author chendong
 */
public class LxList extends DiffableList<LxModel> {

    private LxTypeSplit                     typeSplit;
    private List<OnAdapterEventInterceptor> interceptors;
    private LxAdapter                       adapter;

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

    public void addInterceptor(OnAdapterEventInterceptor interceptor) {
        if (interceptors == null) {
            interceptors = new ArrayList<>();
        }
        interceptors.add(interceptor);
    }

    public void publishEvent(String event) {
        this.publishEvent(event, null);
    }

    public void publishEvent(String event, Object extra) {
        if (interceptors == null || interceptors.isEmpty()) {
            return;
        }
        for (OnAdapterEventInterceptor interceptor : interceptors) {
            if (interceptor.intercept(event, this.adapter, extra)) {
                break;
            }
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
