package com.zfy.component.basic.arch.base;

import com.zfy.component.basic.arch.base.app.AppDelegate;

/**
 * CreateAt : 2018/10/11
 * Describe :
 *
 * @author chendong
 */
public interface IViewInit {

    ViewConfig getViewConfig();

    AppDelegate getAppDelegate();
}
