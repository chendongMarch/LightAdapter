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

    public static final int BIND_TYPE_DEFAULT = 0;

    protected LxAdapter adapter;
    private   TypeOpts  typeOpts;
    protected int       bindType; // 绑定类型

    public LxItemBinder(int bindType) {
        this.bindType = bindType;
        this.typeOpts = getTypeOpts();
    }

    public LxItemBinder() {
        this(BIND_TYPE_DEFAULT);
    }

    void onAdapterAttached(LxAdapter adapter) {
        this.adapter = adapter;
    }


    LxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TypeOpts typeOpts = getTypeOpts();
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
        context.layoutPosition = position;
        context.dataPosition = position - adapter.getData().getDataStartPosition();
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
        TypeOpts typeOpts = getTypeOpts();

        if (typeOpts.enableClick || typeOpts.enableLongPress || typeOpts.enableDbClick) {
            LxEvent.setEvent(holder, typeOpts.enableClick, typeOpts.enableLongPress, typeOpts.enableDbClick, (context, eventType) -> {
                onItemEvent(context, (D) context.data, eventType);
            });
        }
        if (typeOpts.enableFocusChange) {
            LxEvent.setFocusEvent(holder, (context, eventType) -> {
                LxItemBinder.this.onItemEvent(context, (D) context.data, eventType);
            });
        }
    }

    public TypeOpts getTypeOpts() {
        if (typeOpts == null) {
            typeOpts = newTypeOpts();
        }
        return typeOpts;
    }

    protected abstract TypeOpts newTypeOpts();

    protected abstract void onBindView(LxContext context, LxViewHolder holder, D listItem);

    protected void onItemEvent(LxContext context, D listItem, @Lx.EventType int eventType) {
    }

    public LxList getData() {
        return adapter.getData();
    }

    public LxAdapter getAdapter() {
        return adapter;
    }

    @Override
    public int getItemType() {
        return getTypeOpts().viewType;
    }


    public static <DType> Builder<DType> of(Class<DType> clazz) {
        return new Builder<>();
    }

    public interface OnViewBind<D> {
        void onBindView(LxItemBinder binder, LxContext context, LxViewHolder holder, D data);
    }

    public interface OnEventBind<D> {
        void onEvent(LxItemBinder binder, LxContext context, D data, int eventType);
    }


    public static class Builder<DType> {

        private TypeOpts           opts;
        private int                bindType = BIND_TYPE_DEFAULT;
        private OnViewBind<DType>  viewBind;
        private OnEventBind<DType> eventBind;

        private Builder() {
        }

        public Builder<DType> opts(TypeOpts opts) {
            this.opts = opts;
            return this;
        }

        public Builder<DType> bindType(int bindType) {
            this.bindType = bindType;
            return this;
        }

        public Builder<DType> onViewBind(OnViewBind<DType> onViewBind) {
            this.viewBind = onViewBind;
            return this;
        }

        public Builder<DType> onItemEvent(OnEventBind<DType> onEvent) {
            this.eventBind = onEvent;
            return this;
        }

        public LxItemBinder<DType> build() {
            return new LxItemBindImpl(bindType, opts);
        }

        class LxItemBindImpl extends LxItemBinder<DType> {

            private TypeOpts opts;

            LxItemBindImpl(int bindType, TypeOpts opts) {
                super(bindType);
                this.opts = opts;
            }

            @Override
            protected TypeOpts newTypeOpts() {
                return opts;
            }

            @Override
            public void onBindView(LxContext context, LxViewHolder holder, DType data) {
                if (viewBind != null) {
                    viewBind.onBindView(this, context, holder, data);
                }
            }

            @Override
            public void onItemEvent(LxContext context, DType data, int eventType) {
                if (eventBind != null) {
                    eventBind.onEvent(this, context, data, eventType);
                }
            }
        }
    }

}
