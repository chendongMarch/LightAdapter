package com.zfy.lxadapter;

import android.widget.ImageView;

import com.zfy.lxadapter.helper.LxSpan;
import com.zfy.lxadapter.listener.EventSubscriber;

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

    static Map<String, EventSubscriber> subscribers;
    static ImgUrlLoader                 imgUrlLoader;

    public static void setImgUrlLoader(ImgUrlLoader imgUrlLoader) {
        LxGlobal.imgUrlLoader = imgUrlLoader;
    }

    public static void subscribe(String event, EventSubscriber handler) {
        if (subscribers == null) {
            subscribers = new HashMap<>();
        }
        subscribers.put(event, handler);
    }

    public static void setSpanSizeAdapter(LxSpan.SpanSizeAdapter spanSizeAdapter) {
        LxSpan.spanSizeAdapter = spanSizeAdapter;
    }
}
