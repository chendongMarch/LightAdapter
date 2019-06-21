package com.zfy.adapter.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.zfy.adapter.common.LightUtil;

/**
 * CreateAt : 2018/11/6
 * Describe : 使用 layout 和 View 添加子控件
 *
 * @author chendong
 */
public class LightView {

    public int layoutId;
    public View view;
    public int index = -1;
    public ViewGroup.LayoutParams params;

    public static LightView from(int layoutId) {
        LightView lightView = new LightView();
        lightView.layoutId = layoutId;
        return lightView;
    }

    public static LightView from(View view) {
        LightView lightView = new LightView();
        lightView.view = view;
        return lightView;
    }


    public static LightView from(int layoutId, int index) {
        LightView lightView = new LightView();
        lightView.layoutId = layoutId;
        lightView.index = index;
        return lightView;
    }

    public static LightView from(View view, int index) {
        LightView lightView = new LightView();
        lightView.view = view;
        lightView.index = index;
        return lightView;
    }

    private LightView() {

    }

    public void inflate(Context context) {
        if (this.view == null && this.layoutId != 0) {
            this.view = LightUtil.inflateView(context, this.layoutId);
        }
    }
}
