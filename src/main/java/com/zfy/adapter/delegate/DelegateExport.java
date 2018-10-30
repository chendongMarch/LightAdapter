package com.zfy.adapter.delegate;

import android.view.View;

import com.zfy.adapter.ViewHolderBinder;

/**
 * CreateAt : 2018/10/30
 * Describe :
 *
 * @author chendong
 */
public interface DelegateExport {

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

    interface LoadMore {
        void finishLoadMore();
    }

    interface TopMore {
        void finishTopMore();
    }
}
