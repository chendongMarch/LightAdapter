package com.zfy.adapter.delegate;

import android.view.View;

import com.zfy.adapter.listener.AdapterCallback;
import com.zfy.adapter.listener.ViewHolderBinder;

/**
 * CreateAt : 2018/10/30
 * Describe :
 *
 * @author chendong
 */
public interface DelegateExport {

    /**
     * Header&Footer
     */
    interface HF {

        // 添加 Header
        void addHeaderView(int layoutId, ViewHolderBinder binder);

        // 添加 Header
        void addHeaderView(View view, ViewHolderBinder binder);

        // 添加 Header
        void addHeaderView(int layoutId, int index, ViewHolderBinder binder);

        // 添加 Header
        void addHeaderView(View view, int index, ViewHolderBinder binder);

        // 添加 Footer
        void addFooterView(int layoutId, ViewHolderBinder binder);

        // 添加 Footer
        void addFooterView(View view, ViewHolderBinder binder);

        // 添加 Footer
        void addFooterView(int layoutId, int index, ViewHolderBinder binder);

        // 添加 Footer
        void addFooterView(View view, int index, ViewHolderBinder binder);

        // 清空 Header
        void clearHeaderView();

        // 清空 Footer
        void clearFooterView();

        // 更新 Header
        void notifyHeaderUpdate();

        // 更新 Footer
        void notifyFooterUpdate();

    }

    /**
     * 底部加载更多
     */
    interface LoadMore {
        /**
         * 结束加载，才能开启下次加载
         */
        void finishLoadMore();

        /**
         * 设置加载更多监听
         *
         * @param callback
         */
        void setLoadMoreListener(AdapterCallback callback);


        /**
         * 设置提前预加载的个数
         *
         * @param startTryLoadMoreItemCount 预加载的个数
         */
        void setStartTryLoadMoreItemCount(int startTryLoadMoreItemCount);
    }

    /**
     * 顶部加载更多
     */
    interface TopMore {
        /**
         * 结束加载才能开启下次加载
         */
        void finishTopMore();

        /**
         * @param callback 回调
         */
        void setTopMoreListener(AdapterCallback callback);

        /**
         * @param count 预加载个数
         */
        void setStartTryTopMoreItemCount(int count);
    }


    /**
     * 数据更新
     */
    interface Notify {

        /**
         * 在UI线程更新
         *
         * @param runnable run
         */
        void notifyInUIThread(Runnable runnable);

        /**
         * 更新
         */
        void update();

        /**
         * 更新某一项
         *
         * @param position 位置
         */
        void update(final int position);

        /**
         * 范围更新元素
         *
         * @param positionStart 开始位置
         * @param itemCount     数量
         */
        void update(final int positionStart, final int itemCount);

        /**
         * 插入元素
         *
         * @param position 插入位置
         */
        void insert(final int position);

        /**
         * 范围插入元素
         *
         * @param positionStart 开始位置
         * @param itemCount     元素个数
         */
        void insert(final int positionStart, final int itemCount);

        /**
         * 删除元素
         *
         * @param position 位置
         */
        void remove(final int position);

        /**
         * 范围删除元素
         *
         * @param positionStart 开始位置
         * @param itemCount     元素个数
         */
        void remove(final int positionStart, final int itemCount);

        /**
         * 移动元素
         *
         * @param fromPosition 开始位置
         * @param toPosition   目标位置
         */
        void move(final int fromPosition, final int toPosition);
    }
}
