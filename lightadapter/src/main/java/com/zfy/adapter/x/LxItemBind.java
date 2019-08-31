package com.zfy.adapter.x;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.zfy.adapter.R;
import com.zfy.adapter.data.Typeable;

import java.util.List;

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
        holder.setLxContext(context);

        onBindView(holder, position, unpack, payloads);
    }

    public abstract void onBindView(LxVh holder, int position, D data, @NonNull List<Object> payloads);

    private void onBindEvent(LxVh holder, int viewType) {
        LxEvent.setEvent(holder, typeOpts.enableClick, typeOpts.enableLongPress, typeOpts.enableDbClick, (context, eventType) -> {
            // 上下文中数据不变，但是 pos 变了，重新更新一下
            context.position = holder.getAdapterPosition();
            holder.setLxContext(context);
            onEvent(context, (D) context.data, eventType);
        });
    }

    public abstract void onEvent(LxContext context, D data, int eventType);

    public LxAdapter getAdapter() {
        return adapter;
    }

    public TypeOpts getTypeOpts() {
        return typeOpts;
    }

    @Override
    public int getItemType() {
        return typeOpts.viewType;
    }
}
