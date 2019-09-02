package com.zfy.adapter.function;

import android.widget.ImageView;

/**
 * CreateAt : 2019-09-02
 * Describe :
 *
 * @author chendong
 */
public class LxGlobal {

    public interface ImgUrlLoader {
        void load(ImageView view, String url, Object extra);
    }

    private static ImgUrlLoader imgUrlLoader;

    public static ImgUrlLoader getImgUrlLoader() {
        return imgUrlLoader;
    }


    public static void setImgUrlLoader(ImgUrlLoader imgUrlLoader) {
        LxGlobal.imgUrlLoader = imgUrlLoader;
    }

}
