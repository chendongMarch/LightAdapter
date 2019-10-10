package com.zfy.lxadapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.zfy.lxadapter.data.LxModel;
import com.zfy.lxadapter.diff.DiffableList;
import com.zfy.lxadapter.function._Function;
import com.zfy.lxadapter.function._Predicate;
import com.zfy.lxadapter.helper.LxTypeSplit;
import com.zfy.lxadapter.listener.AdapterEventDispatcher;

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

    private LxTypeSplit                         typeSplit;
    private LxAdapter                           adapter;
    private Map<String, AdapterEventDispatcher> interceptors;
    private int                                 dataStartPosition;

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
        calcDataStartPosition();
    }

    @Override
    public LxModel get(int i) {
        int size = size();
        if (size == 0) {
            return null;
        }
        return super.get(adapter.isInfinite ? i % size : i);
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

    /*default*/ int getDataStartPosition() {
        return dataStartPosition;
    }

    private void calcDataStartPosition() {
        if (adapter == null) {
            return;
        }
        boolean hasExtType = adapter.hasExtType;
        if (hasExtType) {
            LxModel lxModel;
            int count = size();
            for (int i = 0; i < count; i++) {
                lxModel = get(i);
                if (Lx.isContentType(lxModel.getItemType())) {
                    dataStartPosition = i;
                    break;
                }
            }
        } else {
            dataStartPosition = 0;
        }
    }


    @Override
    public void update(@NonNull List<LxModel> newItems) {
        super.update(newItems);
        calcDataStartPosition();
    }

    public void addEventHandler(String event, AdapterEventDispatcher interceptor) {
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


}
