package com.zfy.component.basic

import android.app.Application
import android.net.Uri
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter
import com.march.common.Common
import com.zfy.component.basic.exception.ServiceNotFoundException

/**
 * CreateAt : 2018/9/8
 * Describe : 组件化暴露
 *
 * @author chendong
 */
object ComponentX {
    /**
     * 初始化
     */
    @JvmStatic
    fun init(app: Application, debug: Boolean) {
        if (debug) {
            ARouter.openDebug()
            ARouter.openLog()
        }
        ARouter.init(app)
    }

    /**
     * 依赖注入
     */
    @JvmStatic
    fun inject(thiz: Any) {
        ARouter.getInstance().inject(thiz)
    }


    /**
     * 发现服务1
     */
    @JvmStatic
    fun <T> discover(clazz: Class<T>): T {
        return ARouter.getInstance().navigation(clazz)
    }

    /**
     * 发现服务2
     */
    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun <T> discover(path: String): T {
        return ARouter.getInstance().build(path).navigation() as? T ?: throw ServiceNotFoundException()
    }


    /**
     * 路由1
     */
    @JvmStatic
    fun route(url: String): Postcard {
        return ARouter.getInstance().build(url)
    }

    /**
     * 路由2
     */
    @JvmStatic
    fun route(uri: Uri): Postcard {
        return ARouter.getInstance().build(uri)
    }
}