package com.zfy.adapter.delegate;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.LightHolder;

/**
 * CreateAt : 2018/10/28
 * Describe : 委托通用接口
 *
 * @author chendong
 */
public interface IDelegate {

    int HF         = 1;
    int SPAN       = 2;
    int LOAD_MORE  = 3;
    int TOP_MORE   = 4;
    int SELECTOR   = 5;
    int NOTIFY     = 6;
    int EMPTY      = 7;
    int LOADING    = 8;
    int DRAG_SWIPE = 9;
    int SECTION    = 10;
    int ANIMATOR   = 11;
    int FAKE       = 12;

    /**
     * 获取本代理的 key
     *
     * @return 当前代理的唯一标示
     */
    int getKey();

    /**
     * 创建 ViewHolder
     *
     * @param parent   容器 View
     * @param viewType 类型
     * @return Holder
     */
    LightHolder onCreateViewHolder(ViewGroup parent, int viewType);

    /**
     * 当 ViewHolder 被创建后调用
     *
     * @param holder ViewHolder
     */
    void onViewAttachedToWindow(@NonNull LightHolder holder);

    /**
     * 绑定 ViewHolder
     *
     * @param holder   LightHolder
     * @param layoutIndex pos
     * @return 是否承担绑定 ViewHolder 的任务
     */
    boolean onBindViewHolder(LightHolder holder, int layoutIndex);

    /**
     * 绑定到 RecyclerView
     *
     * @param recyclerView recycler view
     */
    void onAttachedToRecyclerView(RecyclerView recyclerView);

    /**
     * 绑定到 Adapter
     *
     * @param adapter adapter
     */
    void onAttachAdapter(LightAdapter adapter);


    /**
     * 获取个数
     *
     * @return 本代理的个数
     */
    int getItemCount();

    /**
     * @return content 底部固定的个数
     */
    int getTopItemCount();


    /**
     * 获取顶部占据的 count
     *
     * @param level 级别
     * @return 底部 item count
     */
    int getAboveItemCount(int level);

    /**
     * 获取指定位置的 type
     *
     * @param position pos
     * @return type
     */
    int getItemViewType(int position);
}
