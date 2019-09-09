package com.zfy.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.zfy.adapter.data.LxContext;
import com.zfy.adapter.data.LxModel;
import com.zfy.adapter.data.TypeOpts;
import com.zfy.adapter.data.Typeable;
import com.zfy.adapter.helper.LxEvent;
import com.zfy.adapter.helper.LxUtil;

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
        context.model = data;
        context.viewType = data.getItemType();
        context.payloads = LxUtil.parsePayloads(payloads);
        holder.setLxContext(context);

        onBindView(context, holder, unpack);
    }

    private void onBindEvent(LxVh holder, int viewType) {
        if (typeOpts.enableClick || typeOpts.enableLongPress || typeOpts.enableDbClick) {
            LxEvent.setEvent(holder, typeOpts.enableClick, typeOpts.enableLongPress, typeOpts.enableDbClick, (context, eventType) -> {
                onEvent(context, (D) context.data, eventType);
            });
        }
        if (typeOpts.enableFocusChange) {
            LxEvent.setFocusEvent(holder, (context, eventType) -> {
                LxItemBind.this.onEvent(context, (D) context.data, eventType);
            });
        }
    }

    public abstract void onBindView(LxContext context, LxVh holder, D listItem);

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
        void onBindView(LxContext context, LxVh holder, D data);
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

        public LxItemBind<DType> build() {
            return new LxItemBindImpl(opts);
        }

        class LxItemBindImpl extends LxItemBind<DType> {

            LxItemBindImpl(TypeOpts opts) {
                super(opts);
            }

            @Override
            public void onBindView(LxContext context, LxVh holder, DType data) {
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
