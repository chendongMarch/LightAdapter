package com.zfy.component.basic.mvvm.binding.adapters;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.LayoutManagers;

/**
 * CreateAt : 2018/9/10
 * Describe : RecyclerView
 * bindScroll
 * @author chendong
 */
public class RecyclerViewAdapters {

    @BindingAdapter(value = {"bindScroll"}, requireAll = false)
    public static void bindScroll(
            final RecyclerView recyclerView,
            final RecyclerView.OnScrollListener onScrollListener) {
        recyclerView.addOnScrollListener(onScrollListener);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter(value = {
            "bindItemBinding", // 绑定
            "bindItems", // 数据源
            "bindAdapter", // adapter
            "bindItemIds",
            "bindViewHolder"
    }, requireAll = false)
    public static <T> void setAdapter(RecyclerView recyclerView,
            ItemBinding<T> itemBinding,
            List<T> items,
            BindingRecyclerViewAdapter<T> adapter,
            BindingRecyclerViewAdapter.ItemIds<? super T> itemIds,
            BindingRecyclerViewAdapter.ViewHolderFactory viewHolderFactory) {
        if (itemBinding == null) {
            throw new IllegalArgumentException("itemBinding must not be null");
        }
        BindingRecyclerViewAdapter oldAdapter = (BindingRecyclerViewAdapter) recyclerView.getAdapter();
        if (adapter == null) {
            if (oldAdapter == null) {
                adapter = new BindingRecyclerViewAdapter<>();
            } else {
                adapter = oldAdapter;
            }
        }
        adapter.setItemBinding(itemBinding);
        adapter.setItems(items);
        adapter.setItemIds(itemIds);
        adapter.setViewHolderFactory(viewHolderFactory);

        if (oldAdapter != adapter) {
            recyclerView.setAdapter(adapter);
        }
    }

    @BindingAdapter("bindLayoutManager")
    public static void setLayoutManager(RecyclerView recyclerView, LayoutManagers.LayoutManagerFactory layoutManagerFactory) {
        recyclerView.setLayoutManager(layoutManagerFactory.create(recyclerView));
    }


}
