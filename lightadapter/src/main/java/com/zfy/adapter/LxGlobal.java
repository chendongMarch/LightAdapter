package com.zfy.adapter;

import android.widget.ImageView;

import com.zfy.adapter.listener.EventHandler;

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

    static Map<String, EventHandler> handlers;
    static ImgUrlLoader              imgUrlLoader;

    public static void setImgUrlLoader(ImgUrlLoader imgUrlLoader) {
        LxGlobal.imgUrlLoader = imgUrlLoader;
    }

    public static void addEventHandler(String event, EventHandler handler) {
        if (handlers == null) {
            handlers = new HashMap<>();
        }
        handlers.put(event, handler);
    }
}
