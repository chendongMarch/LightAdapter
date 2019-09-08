package com.zfy.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zfy.adapter.component.LxComponent;
import com.zfy.adapter.data.LxModel;
import com.zfy.adapter.data.TypeOpts;
import com.zfy.adapter.helper.LxSpan;
import com.zfy.adapter.listener.OnAdapterEventInterceptor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * CreateAt : 2019-08-30
 * Describe :
 *
 * @author chendong
 */
public class LxAdapter extends RecyclerView.Adapter<LxVh> {

    @NonNull private LxList data;
    /*default*/ LayoutInflater inflater;
    private Context      context;
    private RecyclerView view;

    private SparseArray<LxItemBind> binders;
    private Set<LxComponent>        components;
    /*default*/ boolean      hasExtType;


    public static class Builder {

        private LxList                          data;
        private SparseArray<LxItemBind>         binders;
        private RecyclerView.LayoutManager      layoutManager;
        private RecyclerView                    view;
        private Set<LxComponent>                components;
        private List<OnAdapterEventInterceptor> interceptors;

        private Builder() {
            binders = new SparseArray<>();
            components = new HashSet<>();
            interceptors = new ArrayList<>();
        }

        public Builder bindItem(LxItemBind... binders) {
            for (LxItemBind bind : binders) {
                this.binders.append(bind.getItemType(), bind);
            }
            return this;
        }


        public Builder component(LxComponent component) {
            this.components.add(component);
            return this;
        }

        public Builder onEvent(OnAdapterEventInterceptor interceptor) {
            this.interceptors.add(interceptor);
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

        this.binders = builder.binders;
        this.components = builder.components;
        this.hasExtType = false;
        for (int i = 0; i < binders.size(); i++) {
            LxItemBind lxItemBind = binders.valueAt(i);
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
        if (LxGlobal.interceptors != null) {
            builder.interceptors.addAll(LxGlobal.interceptors);
        }
        for (OnAdapterEventInterceptor interceptor : builder.interceptors) {
            this.data.addInterceptor(interceptor);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.ItemAnimator itemAnimator = recyclerView.getItemAnimator();
        if (itemAnimator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
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
    public LxVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LxItemBind lxItemBind = binders.get(viewType);
        LxVh holder = lxItemBind.onCreateViewHolder(parent, viewType);
        holder.setItemViewType(viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LxVh holder, int position) {
        // ignore
    }

    @Override
    public void onBindViewHolder(@NonNull LxVh holder, int position, @NonNull List<Object> payloads) {
        LxModel lxModel = data.get(position);
        int viewType = getItemViewType(position);
        LxItemBind lxItemBind = binders.get(viewType);
        lxItemBind.onBindViewHolder(holder, position, lxModel, payloads);
        for (LxComponent component : components) {
            component.onBindViewHolder(this, holder, position, payloads);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getItemType();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull LxVh holder) {
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
        LxItemBind bind = binders.get(viewType);
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
