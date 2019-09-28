package com.zfy.lxadapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.zfy.lxadapter.data.LxContext;
import com.zfy.lxadapter.data.LxModel;
import com.zfy.lxadapter.data.TypeOpts;
import com.zfy.lxadapter.data.Typeable;
import com.zfy.lxadapter.helper.LxEvent;
import com.zfy.lxadapter.helper.LxUtil;

import java.util.List;

/**
 * CreateAt : 2019-08-30
 * Describe :
 *
 * @author chendong
 */
public abstract class LxItemBinder<D> implements Typeable {

    protected          LxAdapter adapter;
    private            TypeOpts  typeOpts;
    protected @NonNull Bundle    params;

    public LxItemBinder(TypeOpts.TypeOptsSetter setter) {
        this(TypeOpts.make(setter));
    }

    public LxItemBinder(TypeOpts opts) {
        this.typeOpts = opts;
    }

    void onAdapterAttached(LxAdapter adapter, @NonNull Bundle bundle) {
        this.adapter = adapter;
        this.params = bundle;
    }

    LxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = adapter.inflater.inflate(typeOpts.layoutId, parent, false);
        LxContext context = (LxContext) view.getTag(R.id.item_context);
        if (context == null) {
            context = new LxContext();
        }
        LxViewHolder lxVh = new LxViewHolder(view);
        context.holder = lxVh;
        lxVh.setLxContext(context);
        view.setTag(R.id.item_context, context);
        onBindEvent(lxVh, viewType);
        return lxVh;
    }

    void onBindViewHolder(@NonNull LxViewHolder holder, int position, LxModel data, @NonNull List<Object> payloads) {
        D unpack = data.unpack();

        LxContext context = (LxContext) holder.itemView.getTag(R.id.item_context);
        context.holder = holder;
        context.position = position;
        context.data = unpack;
        context.model = data;
        context.viewType = data.getItemType();
        context.payloads = LxUtil.parsePayloads(payloads);
        holder.setLxContext(context);

        Bundle condition = data.getCondition();
        context.condition = condition;

        onBindView(context, holder, unpack);

        condition.clear();
        context.clear();
    }

    private void onBindEvent(LxViewHolder holder, int viewType) {
        if (typeOpts.enableClick || typeOpts.enableLongPress || typeOpts.enableDbClick) {
            LxEvent.setEvent(holder, typeOpts.enableClick, typeOpts.enableLongPress, typeOpts.enableDbClick, (context, eventType) -> {
                onEvent(context, (D) context.data, eventType);
            });
        }
        if (typeOpts.enableFocusChange) {
            LxEvent.setFocusEvent(holder, (context, eventType) -> {
                LxItemBinder.this.onEvent(context, (D) context.data, eventType);
            });
        }
    }

    public abstract void onBindView(LxContext context, LxViewHolder holder, D listItem);

    public void onEvent(LxContext context, D listItem, @Lx.EventType int eventType) {
    }

    public TypeOpts getTypeOpts() {
        return typeOpts;
    }

    public LxList getData() {
        return adapter.getData();
    }


    @Override
    public int getItemType() {
        return typeOpts.viewType;
    }


    public static <DType> Builder<DType> of(Class<DType> clazz) {
        return new Builder<>();
    }

    public interface OnViewBind<D> {
        void onBindView(LxContext context, LxViewHolder holder, D data);
    }

    public interface OnEventBind<D> {
        void onEvent(LxContext context, D data, int eventType);
    }


    public static class Builder<DType> {

        private TypeOpts           opts;
        private OnViewBind<DType>  viewBind;
        private OnEventBind<DType> eventBind;

        private Builder() {
        }

        public Builder<DType> opts(TypeOpts opts) {
            this.opts = opts;
            return this;
        }

        public Builder<DType> onViewBind(OnViewBind<DType> onViewBind) {
            this.viewBind = onViewBind;
            return this;
        }

        public Builder<DType> onEventBind(OnEventBind<DType> onEvent) {
            this.eventBind = onEvent;
            return this;
        }

        public LxItemBinder<DType> build() {
            return new LxItemBindImpl(opts);
        }

        class LxItemBindImpl extends LxItemBinder<DType> {

            LxItemBindImpl(TypeOpts opts) {
                super(opts);
            }

            @Override
            public void onBindView(LxContext context, LxViewHolder holder, DType data) {
                if (viewBind != null) {
                    viewBind.onBindView(context, holder, data);
                }
            }

            @Override
            public void onEvent(LxContext context, DType data, int eventType) {
                if (eventBind != null) {
                    eventBind.onEvent(context, data, eventType);
                }
            }
        }
    }

}
