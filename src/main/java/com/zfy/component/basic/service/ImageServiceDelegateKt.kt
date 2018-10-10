package com.zfy.component.basic.service

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * CreateAt : 2018/9/8
 * Describe :
 *
 * @author chendong
 */
open class ImageServiceDelegateKt : IImageService {

    override fun init(context: Context?) {

    }


    override fun load(context: Context?, url: String?, imageView: ImageView?) {
        if (context == null || url == null || imageView == null) {
            return
        }
        Glide.with(context).load(url).into(imageView)
    }

    override fun load(context: Context?, url: String?, imageView: ImageView?, width: Int, height: Int) {
        if (context == null || url == null || imageView == null) {
            return
        }
        val requestOptions = RequestOptions().apply {
            if (width > 0 && height > 0) {
                override(width, height)
            }
        }
        Glide.with(context).load(url).apply(requestOptions).into(imageView)
    }

    override fun loadGlide(context: Context?, url: String?, imageView: ImageView?, opts: RequestOptions?) {
        if (context == null || url == null || imageView == null) {
            return
        }
        Glide.with(context).load(url).apply {
            if (opts != null) {
                apply(opts)
            }
            into(imageView)
        }
    }


}