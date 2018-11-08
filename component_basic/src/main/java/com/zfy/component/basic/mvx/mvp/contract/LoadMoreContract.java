package com.zfy.component.basic.mvx.mvp.contract;

/**
 * CreateAt : 2018/9/21
 * Describe : 加载更多功能
 *
 * @author chendong
 */
public interface LoadMoreContract {

    interface V {

        /**
         * 结束加载更多
         *
         * @param success 是否成功
         */
        void finishLoadMore(boolean success);

        /**
         * 开启/禁用加载更多
         * @param enable 是否开启
         */
        void setLoadMoreEnable(boolean enable);

        /**
         * 设置是否没有更多数据
         * @param noMoreData 没有更多
         */
        default void setNoMoreData(boolean noMoreData) {

        }
    }

    interface P {

        void loadMore();
    }
}
