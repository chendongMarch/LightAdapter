package com.zfy.component.basic.arch.mvvm.binding.adapters;

import android.databinding.BindingAdapter;
import android.databinding.ObservableList;
import android.view.View;
import android.view.ViewGroup;


/**
 * CreateAt : 2018/9/10
 * Describe : ViewGroup
 *
 * @author chendong
 */
public final class ViewGroupAdapters {

    @BindingAdapter({"bindItemView", "bindViewModels"})
    public static void addViews(
            final ViewGroup viewGroup,
            final View itemView,
            final ObservableList<Object> viewModelList) {
//        if (viewModelList != null && !viewModelList.isEmpty()) {
//            viewGroup.removeAllViews();
//            for (Object vm : viewModelList) {
//                ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
//                        itemView.layoutRes(), viewGroup, true);
//                binding.setVariable(itemView.bindingVariable(), vm);
//            }
//        }
    }
}

