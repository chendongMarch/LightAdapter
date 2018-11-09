package com.zfy.light.sample;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zfy.adapter.LightHolder;

/**
 * CreateAt : 2018/11/9
 * Describe :
 *
 * @author chendong
 */
public class GlideCallback implements LightHolder.Callback<ImageView> {

    RequestOptions options = new RequestOptions();

    String url;

    public GlideCallback(RequestOptions options, String url) {
        this.options = this.options.apply(options);
        this.url = url;
    }

    public GlideCallback(String url) {
        this.url = url;
    }

    @Override
    public void bind(ImageView view) {
        Glide.with(view.getContext()).load(url).apply(options).into(view);
    }
}