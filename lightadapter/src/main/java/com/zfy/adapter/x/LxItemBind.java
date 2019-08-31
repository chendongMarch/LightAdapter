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

    private LxAdapter       adapter;
    private TypeOpts        typeOpts;
    private EventListener[] listeners;

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
        context.setHolder(lxVh);
        view.setTag(R.id.item_context, context);
        onBindEvent(lxVh, viewType);
        return lxVh;
    }

    public void onBindViewHolder(@NonNull LxVh holder, int position, LxModel data, @NonNull List<Object> payloads) {
        LxContext context = (LxContext) holder.itemView.getTag(R.id.item_context);
        context.setHolder(holder);
        context.setPos(position);
        context.setData(data.unpack());
        holder.itemView.setTag(R.id.item_context, context);

        onBindView(holder, position, data.unpack(), payloads);
    }

    public abstract void onBindView(LxVh holder, int position, D data, @NonNull List<Object> payloads);

    public abstract void onBindEvent(LxVh holder, int viewType);

    @Override
    public int getItemType() {
        return typeOpts.viewType;
    }
}
