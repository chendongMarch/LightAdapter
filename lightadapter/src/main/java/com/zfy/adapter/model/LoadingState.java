package com.zfy.adapter.model;

/**
 * CreateAt : 2018/11/6
 * Describe : 加载中状态，可扩展
 *
 * @author chendong
 */
public class LoadingState {

    public static final int INIT = 0; // 初始化
    public static final int LOADING = 1; // 开始加载
    public static final int FINISH = 2; // 结束加载
    public static final int NO_DATA = 3; // 没有数据

    public int state;

    public static LoadingState from(int state) {
        LoadingState loadingState = new LoadingState();
        loadingState.state = state;
        return loadingState;
    }

    private LoadingState() {
    }
}
