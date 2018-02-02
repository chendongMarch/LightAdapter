package com.march.lightadapter;

import android.view.ViewGroup;

/**
 * CreateAt : 2017.09.14
 * Describe : adapter 基础接口
 *
 * @author chendong
 */
public interface ILightAdapter {

    int MATCH = ViewGroup.LayoutParams.MATCH_PARENT;
    int WRAP  = ViewGroup.LayoutParams.WRAP_CONTENT;
    int UNSET = -100;

    int TYPE_HEADER  = -1;
    int TYPE_FOOTER  = -2;
    int TYPE_DEFAULT = 0x123;
}
