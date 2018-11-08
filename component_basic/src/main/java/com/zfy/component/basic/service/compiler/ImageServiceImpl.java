package com.zfy.component.basic.service.compiler;


import android.content.Context;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.request.RequestOptions;
import com.zfy.component.basic.service.BuildInService;
import com.zfy.component.basic.service.IImageService;

import org.jetbrains.annotations.Nullable;


/**
 * CreateAt : 2018/9/10
 * Describe :
 *
 * @author chendong
 */
@Route(path = BuildInService.IMAGE, name = "图片服务")
public class ImageServiceImpl implements IImageService {

    private com.zfy.component.basic.service.ImageServiceDelegateKt mImageService = new com.zfy.component.basic.service.ImageServiceDelegateKt();

    @Override
    public void load(@Nullable Context context, @Nullable String url, @Nullable ImageView imageView) {
        mImageService.load(context, url, imageView);
    }

    @Override
    public void load(@Nullable Context context, @Nullable String url, @Nullable ImageView imageView, int width, int height) {
        mImageService.load(context, url, imageView, width, height);
    }

    @Override
    public void loadGlide(@Nullable Context context, @Nullable String url, @Nullable ImageView imageView, @Nullable RequestOptions opts) {
        mImageService.loadGlide(context, url, imageView, opts);
    }

    @Override
    public void init(Context context) {

    }
}
