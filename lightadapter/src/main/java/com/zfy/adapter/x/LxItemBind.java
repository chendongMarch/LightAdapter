package com.zfy.adapter.x;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.zfy.adapter.data.Typeable;

import java.util.List;

/**
 * CreateAt : 2019-08-30
 * Describe :
 *
 * @author chendong
 */
public abstract class LxItemBind<D> implements Typeable {

    private LxAdapter adapter;
    private TypeOpts  typeOpts;

    public LxItemBind(TypeOpts opts) {
        this.typeOpts = opts;
    }

    public void onAdapterAttached(LxAdapter adapter) {
        this.adapter = adapter;
    }

    public LxVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = adapter.inflater.inflate(typeOpts.layoutId, parent, false);
        return new LxVh(view);
    }

    public void onBindViewHolder(@NonNull LxVh holder, int position, LxModel data, @NonNull List<Object> payloads) {
        onBindView(holder, position, data.data(), payloads);
    }

    public abstract void onBindView(LxVh holder, int position, D data, @NonNull List<Object> payloads);

    @Override
    public int getItemType() {
        return typeOpts.viewType;
    }
}
