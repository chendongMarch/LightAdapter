package com.zfy.lxadapter;

import android.widget.ImageView;

import com.zfy.lxadapter.listener.AdapterEventDispatcher;

import java.util.HashMap;
import java.util.Map;

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

    static Map<String, AdapterEventDispatcher> dispatchers;
    static ImgUrlLoader                        imgUrlLoader;

    public static void setImgUrlLoader(ImgUrlLoader imgUrlLoader) {
        LxGlobal.imgUrlLoader = imgUrlLoader;
    }

    public static void addAdapterEventDispatcher(String event, AdapterEventDispatcher handler) {
        if (dispatchers == null) {
            dispatchers = new HashMap<>();
        }
        dispatchers.put(event, handler);
    }
}
