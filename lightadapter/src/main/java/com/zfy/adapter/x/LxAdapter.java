package com.zfy.adapter.x;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zfy.adapter.x.list.LxList;

import java.util.List;

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

    private SparseArray<LxItemBind> lxItemBindArray;

    public static class Builder {

        private LxList<LxModel>            data;
        private SparseArray<LxItemBind>    lxItemBindArray;
        private RecyclerView.LayoutManager layoutManager;
        private RecyclerView               view;

        private Builder() {
            lxItemBindArray = new SparseArray<>();
        }

        public Builder bind(LxItemBind... binds) {
            for (LxItemBind bind : binds) {
                lxItemBindArray.append(bind.getItemType(), bind);
            }
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

        this.lxItemBindArray = builder.lxItemBindArray;
        for (int i = 0; i < lxItemBindArray.size(); i++) {
            LxItemBind lxItemBind = lxItemBindArray.valueAt(i);
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
    }

    @NonNull
    @Override
    public LxVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LxItemBind lxItemBind = lxItemBindArray.get(viewType);
        return lxItemBind.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull LxVh holder, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull LxVh holder, int position, @NonNull List<Object> payloads) {
        LxModel lxModel = data.get(position);
        int viewType = getItemViewType(position);
        LxItemBind lxItemBind = lxItemBindArray.get(viewType);
        lxItemBind.onBindViewHolder(holder, position, lxModel, payloads);
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
        LxItemBind bind = lxItemBindArray.get(viewType);
        if (bind == null) {
            throw new IllegalStateException("ItemBind Is Null");
        }
        return bind.getTypeOpts();
    }
}
