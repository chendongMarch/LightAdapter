package com.zfy.adapter.model;

/**
 * CreateAt : 2018/11/6
 * Describe : 空白页状态，可扩展
 *
 * @author chendong
 */
public class EmptyState {

    public static final int NONE = 0; // 初始化
    public static final int ERROR = 1; // 错误
    public static final int SUCCESS = 2; // 结束加载
    public static final int NO_DATA = 3; // 没有数据

    public int state;

    public static EmptyState from(int state) {
        EmptyState loadingState = new EmptyState();
        loadingState.state = state;
        return loadingState;
    }

    private EmptyState() {
    }
}
