package com.zfy.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.zfy.adapter.data.Typeable;
import com.zfy.adapter.data.LxContext;
import com.zfy.adapter.data.LxModel;
import com.zfy.adapter.data.TypeOpts;
import com.zfy.adapter.function.LxEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * CreateAt : 2019-08-30
 * Describe :
 *
 * @author chendong
 */
public abstract class LxItemBind<D> implements Typeable {

    protected LxAdapter adapter;
    private   TypeOpts  typeOpts;

    public LxItemBind(TypeOpts opts) {
        this.typeOpts = opts;
    }

    public void onAdapterAttached(LxAdapter adapter) {
        this.adapter = adapter;
    }

    public LxVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = adapter.inflater.inflate(typeOpts.layoutId, parent, false);
        LxContext context = (LxContext) view.getTag(R.id.item_context);
        if (context == null) {
            context = new LxContext();
        }
        LxVh lxVh = new LxVh(view);
        context.holder = lxVh;
        lxVh.setLxContext(context);
        view.setTag(R.id.item_context, context);
        onBindEvent(lxVh, viewType);
        return lxVh;
    }

    public void onBindViewHolder(@NonNull LxVh holder, int position, LxModel data, @NonNull List<Object> payloads) {
        D unpack = data.unpack();

        LxContext context = (LxContext) holder.itemView.getTag(R.id.item_context);
        context.holder = holder;
        context.position = position;
        context.data = unpack;
        context.model = data;
        holder.setLxContext(context);

        onBindView(holder, unpack, data, position, parsePayloads(payloads));
    }

    public abstract void onBindView(LxVh holder, D data, LxModel model, int position, @NonNull List<String> payloads);

    private void onBindEvent(LxVh holder, int viewType) {
        LxEvent.setEvent(holder, typeOpts.enableClick, typeOpts.enableLongPress, typeOpts.enableDbClick, (context, eventType) -> {
            onEvent(context, context == null ? null : (D) context.data, context.model, eventType);
        });
    }


    public abstract void onEvent(LxContext context, D data, LxModel model, @Lx.EventType int eventType);

    public LxAdapter getAdapter() {
        return adapter;
    }

    public TypeOpts getTypeOpts() {
        return typeOpts;
    }


    private List<String> parsePayloads(List<Object> payloads) {
        List<String> list = new ArrayList<>();
        for (Object payload : payloads) {
            if (!(payload instanceof Set) || ((Set) payload).isEmpty()) {
                continue;
            }
            Set msgSet = (Set) payload;
            for (Object o : msgSet) {
                if (o instanceof String) {
                    list.add((String) o);
                }
            }
        }
        return list;
    }

    @Override
    public int getItemType() {
        return typeOpts.viewType;
    }
}
