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
import com.zfy.adapter.x.function.LxUtil;
import com.zfy.adapter.x.list.LxList;

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

    private LxList<LxModel> data;
    private LxBlockedList   blockedList;
    // 布局加载
    /*default*/ LayoutInflater inflater;
    //
    private Context      context;
    //
    private RecyclerView view;

    private SparseArray<LxItemBind> binders;
    private Set<LxComponent>        components;
    private Set<Integer>            contentTypes;

    public static class Builder {

        private LxList<LxModel>            data;
        private SparseArray<LxItemBind>    binders;
        private RecyclerView.LayoutManager layoutManager;
        private RecyclerView               view;
        private Set<LxComponent>           components;
        private Set<Integer>               contentTypes;

        private Builder() {
            binders = new SparseArray<>();
            components = new HashSet<>();
            contentTypes = new HashSet<>();
        }

        public Builder binder(LxItemBind... binders) {
            for (LxItemBind bind : binders) {
                this.binders.append(bind.getItemType(), bind);
            }
            return this;
        }


        public Builder contentType(int... contentTypes) {
            for (int contentType : contentTypes) {
                this.contentTypes.add(contentType);
            }
            return this;
        }
        public Builder component(LxComponent component) {
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
        this.contentTypes = builder.contentTypes;
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
        this.blockedList = new LxBlockedList(this);
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

    public LxList<LxModel> getData() {
        return data;
    }

    public LxList<LxModel> getContentTypeData() {
        if (this.contentTypes == null || this.contentTypes.isEmpty()) {
            return getData();
        }
        return blockedList.getContentList();
    }

    public LxList<LxModel> getCustomTypeData(int viewType) {
        if (this.contentTypes == null || this.contentTypes.isEmpty()) {
            return null;
        }
        return blockedList.getTypedList(viewType);
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

    static class LxBlockedList extends LxList<LxModel> {

        private LxList<LxModel>                 list;
        private Set<Integer>                    contentTypes;

        private SparseArray<HandleUpdateLxList> array;
        private List<Integer>                   blockIds;


        private LxBlockedList(LxAdapter adapter) {
            super();
            this.array = new SparseArray<>();
            this.blockIds = new ArrayList<>();

            this.list = adapter.data;
            this.contentTypes = adapter.contentTypes;
            this.trySplitList();
        }

        private boolean isContentType(int viewType) {
            if (this.contentTypes == null || this.contentTypes.isEmpty()) {
                // 没有多类型，没有自定义类型的时候均为 内容类型
                // throw new IllegalStateException("期望获取类型是否是内容类型，但是没有设置 contentType");
                LxUtil.log("期望获取类型是否是内容类型，但是没有设置 contentType");
                return true;
            }
            return this.contentTypes.contains(viewType);
        }

        private List<LxModel> mergeList() {
            List<LxModel> list = new ArrayList<>();
            for (Integer id : blockIds) {
                List<LxModel> es = array.get(id);
                list.addAll(es);
            }
            return list;
        }

        private void trySplitList() {
            array.clear();
            blockIds.clear();
            for (LxModel lxModel : list) {
                boolean isContentType = isContentType(lxModel.getItemType());
                int blockId = isContentType ? Lx.DEFAULT_BLOCK_ID : lxModel.getItemType();
                HandleUpdateLxList handleUpdateLxList = array.get(blockId);
                if (handleUpdateLxList == null) {
                    handleUpdateLxList = new HandleUpdateLxList();
                    array.append(blockId, handleUpdateLxList);
                    blockIds.add(blockId);
                }
                handleUpdateLxList.models.add(lxModel);
            }
        }

        @Override
        public List<LxModel> list() {
            return mergeList();
        }

        @Override
        public void update(@NonNull List<LxModel> newItems) {
            list.update(mergeList());
        }

        @Nullable
        LxList<LxModel> getTypedList(int blockId) {
            trySplitList();
            return array.get(blockId);
        }

        LxList<LxModel> getContentList() {
            trySplitList();
            return getTypedList(Lx.DEFAULT_BLOCK_ID);
        }

        class HandleUpdateLxList extends LxList<LxModel> {

            private List<LxModel> models = new ArrayList<>();

            @Override
            public List<LxModel> list() {
                return models;
            }

            @Override
            public void update(@NonNull List<LxModel> newItems) {
                models.clear();
                models.addAll(newItems);
                LxBlockedList.this.update(mergeList());
            }
        }
    }
}
