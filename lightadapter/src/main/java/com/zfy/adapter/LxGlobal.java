package com.zfy.adapter;

import android.widget.ImageView;

import com.zfy.adapter.component.LxEndEdgeLoadMoreComponent;
import com.zfy.adapter.component.LxStartEdgeLoadMoreComponent;
import com.zfy.adapter.listener.OnAdapterEventInterceptor;

import java.util.ArrayList;
import java.util.List;

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

    static List<OnAdapterEventInterceptor> interceptors;
    static ImgUrlLoader                    imgUrlLoader;

    public static void setImgUrlLoader(ImgUrlLoader imgUrlLoader) {
        LxGlobal.imgUrlLoader = imgUrlLoader;
    }

    public static void addOnAdapterEventInterceptor(OnAdapterEventInterceptor interceptor) {
        if (interceptors == null) {
            interceptors = new ArrayList<>();
        }
        interceptors.add(interceptor);
    }

    static {
        addOnAdapterEventInterceptor((event, adapter, extra) -> {
            switch (event) {
                case Lx.EVENT_FINISH_END_EDGE_LOAD_MORE:
                    LxEndEdgeLoadMoreComponent endEdgeLoadMoreComponent = adapter.getComponent(LxEndEdgeLoadMoreComponent.class);
                    if (endEdgeLoadMoreComponent != null) {
                        endEdgeLoadMoreComponent.finishLoadMore();
                    }
                    break;
                case Lx.EVENT_FINISH_START_EDGE_LOAD_MORE:
                    LxStartEdgeLoadMoreComponent startEdgeLoadMoreComponent = adapter.getComponent(LxStartEdgeLoadMoreComponent.class);
                    if (startEdgeLoadMoreComponent != null) {
                        startEdgeLoadMoreComponent.finishLoadMore();
                    }
                    break;
            }
            return false;
        });
    }

}
