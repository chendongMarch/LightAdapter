package com.zfy.adapter.x;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zfy.adapter.x.component.LxComponent;
import com.zfy.adapter.x.function.LxSpan;
import com.zfy.adapter.x.list.LxList;

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

    private LxList<LxModel> data;
    // 布局加载
    /*default*/ LayoutInflater inflater;
    //
    private Context      context;
    //
    private RecyclerView view;

    private SparseArray<LxItemBind> binders;
    private Set<LxComponent>        components;

    public static class Builder {

        private LxList<LxModel>            data;
        private SparseArray<LxItemBind>    binders;
        private RecyclerView.LayoutManager layoutManager;
        private RecyclerView               view;
        private Set<LxComponent>           components;

        private Builder() {
            binders = new SparseArray<>();
            components = new HashSet<>();
        }

        public Builder binder(LxItemBind... binders) {
            for (LxItemBind bind : binders) {
                this.binders.append(bind.getItemType(), bind);
            }
            return this;
        }

        public Builder component(LxComponent component) {
            this.components.add(component);
            return this;
        }

        public Builder component(String name, LxComponent component) {
            component.setName(name);
            this.components.add(component);
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

    public static Builder of(LxList<LxModel> data) {
        Builder builder = new Builder();
        builder.data = data;
        return builder;
    }

    private LxAdapter(Builder builder) {
        this.data = builder.data;

        this.binders = builder.binders;
        this.components = builder.components;
        for (int i = 0; i < binders.size(); i++) {
            LxItemBind lxItemBind = binders.valueAt(i);
            lxItemBind.onAdapterAttached(this);
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
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
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
        return lxItemBind.onCreateViewHolder(parent, viewType);
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
            component.onAttachedToRecyclerView(this, view);
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

    public LxList<LxModel> getData() {
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
    LxComponent getComponent(String name) {
        LxComponent component = null;
        for (LxComponent lxComponent : components) {
            if (name.equals(lxComponent.getName())) {
                component = lxComponent;
                break;
            }
        }
        return null;
    }
}
