package com.march.lightadapter;

import android.view.View;

import java.util.List;

/**
 * CreateAt : 2018/10/26
 * Describe : LightAdapter Options
 *
 * @author chendong
 */
public class Options {

    static class ModelType {
        int type; // 类型
        int layout; // 布局资源
        int spanSize; // 跨越行数
        boolean enableClick; // 是否允许点击事件
        boolean enableDbClick; // 是否允许双击事件，双击事件使用 gesture 实现，将会丧失一些效果
    }

    List<ModelType> modelTypes;

    private Options() {
    }

    public static Options create() {

    }

    public interface Action {
        void run();
    }

    private int footerLayoutId;
    private int headerLayoutId;

    List<View> headerViews; // header
    List<View> footerViews; // footer

    int topPreloadNum;
    Action topLoadmoreAction;

    int preloadNum;
    Action loadmoreAction;

}
