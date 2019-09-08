package com.zfy.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.zfy.adapter.function.LxTypeSplit;
import com.zfy.adapter.list.LxList;
import com.zfy.adapter.listener.OnAdapterEventInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateAt : 2018/11/8
 * Describe : 对外支持更新的 List
 *
 * @author chendong
 */
public abstract class LxModelList extends LxList {

    private LxTypeSplit                     typeSplit;
    private List<OnAdapterEventInterceptor> interceptors;
    private LxAdapter                       adapter;

    public LxModelList() {
        typeSplit = new LxTypeSplit();
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
}
