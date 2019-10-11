package com.zfy.lxadapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zfy.lxadapter.component.LxComponent;
import com.zfy.lxadapter.data.LxModel;
import com.zfy.lxadapter.data.TypeOpts;
import com.zfy.lxadapter.function._Function;
import com.zfy.lxadapter.helper.LxSpan;
import com.zfy.lxadapter.listener.AdapterEventDispatcher;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * CreateAt : 2019-08-30
 * Describe :
 *
 * @author chendong
 */
public class LxAdapter extends RecyclerView.Adapter<LxViewHolder> {

    @NonNull private LxList data;
    /*default*/ LayoutInflater inflater;
    private Context      context;
    private RecyclerView view;

    private SparseArray<LxItemBinder> binders;
    private Set<LxComponent>          components;
    /*default*/ boolean hasExtType;
    /*default*/ boolean isInfinite;

    public static class Builder {

        private LxList                              data;
        private SparseArray<LxItemBinder>           binders;
        private RecyclerView.LayoutManager          layoutManager;
        private RecyclerView                        view;
        private Set<LxComponent>                    components;
        private Map<String, AdapterEventDispatcher> dispatchers;
        private boolean                             isInfinite;

        private Builder() {
            binders = new SparseArray<>();
            components = new HashSet<>();
            dispatchers = new HashMap<>();
        }

        public Builder infinite() {
            isInfinite = true;
            return this;
        }

        public Builder compose(_Function<Builder, Builder> mapper) {
            return mapper.map(this);
        }

        public Builder bindItem(LxItemBinder... binders) {
            for (LxItemBinder bind : binders) {
                this.binders.append(bind.getItemType(), bind);
            }
            return this;
        }


        public Builder component(LxComponent component) {
            this.components.add(component);
            return this;
        }

        public Builder onAdapterEvent(String event, AdapterEventDispatcher dispatcher) {
            this.dispatchers.put(event, dispatcher);
            return this;
        }

        public Builder layoutManager(RecyclerView.LayoutManager manager) {
            this.layoutManager = manager;
            return this;
        }

        public LxAdapter build() {
            return new LxAdapter(this);
        }

        public LxAdapter attachTo(RecyclerView view, RecyclerView.LayoutManager layoutManager) {
            this.view = view;
            this.layoutManager = layoutManager;
            return new LxAdapter(this);
        }
    }

    public static Builder of(@NonNull LxList data) {
        Builder builder = new Builder();
        builder.data = data;
        return builder;
    }

    private LxAdapter(Builder builder) {
        this.data = builder.data;
        this.isInfinite = builder.isInfinite;
        this.binders = builder.binders;
        this.components = builder.components;
        this.hasExtType = false;
        for (int i = 0; i < binders.size(); i++) {
            LxItemBinder lxItemBind = binders.valueAt(i);
            lxItemBind.onAdapterAttached(this);
            if (!Lx.isContentType(lxItemBind.getTypeOpts().viewType)) {
                this.hasExtType = true;
            }
        }

        // set view layout
        if (builder.view != null) {
            this.context = builder.view.getContext();
            this.view = builder.view;
            if (builder.layoutManager != null) {
                this.view.setLayoutManager(builder.layoutManager);
            }
            this.view.setAdapter(this);
        }
        this.data.setAdapter(this);
        if (LxGlobal.dispatchers != null) {
            builder.dispatchers.putAll(LxGlobal.dispatchers);
        }
        for (Map.Entry<String, AdapterEventDispatcher> entry : builder.dispatchers.entrySet()) {
            this.data.addAdapterEventDispatcher(entry.getKey(), entry.getValue());
        }
        for (LxComponent component : components) {
            component.onAttachedToAdapter(this);
        }

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);


        RecyclerView.ItemAnimator itemAnimator = view.getItemAnimator();
        if (itemAnimator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
            itemAnimator.setChangeDuration(0);
        }


        this.context = recyclerView.getContext();
        this.view = recyclerView;
        this.inflater = LayoutInflater.from(context);

        LxSpan.onAttachedToRecyclerView(this);

        for (LxComponent component : components) {
            component.onAttachedToRecyclerView(this, recyclerView);
        }
    }

    @NonNull
    @Override
    public LxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LxItemBinder lxItemBind = binders.get(viewType);
        LxViewHolder holder = lxItemBind.onCreateViewHolder(parent, viewType);
        holder.setItemViewType(viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LxViewHolder holder, int position) {
        // ignore
    }

    @Override
    public void onBindViewHolder(@NonNull LxViewHolder holder, int position, @NonNull List<Object> payloads) {
        LxModel lxModel = data.get(position);
        int viewType = getItemViewType(position);
        LxItemBinder lxItemBind = binders.get(viewType);
        lxItemBind.onBindViewHolder(holder, position, lxModel, payloads);
        for (LxComponent component : components) {
            component.onBindViewHolder(this, holder, position, payloads);
        }
    }

    @Override
    public int getItemCount() {
        return data.size() == 0 ? 0 : (isInfinite ? Integer.MAX_VALUE : data.size());
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getItemType();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull LxViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        LxSpan.onViewAttachedToWindow(this, holder);
    }

    public @NonNull
    LxList getData() {
        return data;
    }

    public Context getContext() {
        return context;
    }

    public RecyclerView getView() {
        return view;
    }

    public TypeOpts getTypeOpts(int viewType) {
        LxItemBinder bind = binders.get(viewType);
        if (bind == null) {
            throw new IllegalStateException("ItemBind Is Null");
        }
        return bind.getTypeOpts();
    }

    public @Nullable
    <C extends LxComponent> C getComponent(@NonNull Class<C> clazz) {
        LxComponent component = null;
        for (LxComponent lxComponent : components) {
            if (clazz.equals(lxComponent.getClass())) {
                component = lxComponent;
                break;
            }
        }
        return (C) component;
    }
}
