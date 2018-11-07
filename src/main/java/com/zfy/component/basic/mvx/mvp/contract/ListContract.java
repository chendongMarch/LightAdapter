package com.zfy.component.basic.mvx.mvp.contract;

import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * CreateAt : 2018/9/21
 * Describe : 列表加载功能
 *
 * @author chendong
 */
public interface ListContract {

    interface V {

        RecyclerView.Adapter getContentAdapter();
    }

    interface P<D> {

        List<D> getListDatas();
    }
}
