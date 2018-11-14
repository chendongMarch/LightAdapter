package com.zfy.adapter;

import android.support.annotation.LayoutRes;

import com.zfy.adapter.common.ItemType;
import com.zfy.adapter.listener.BindCallback;
import com.zfy.adapter.listener.EventCallback;
import com.zfy.adapter.listener.ModelTypeConfigCallback;
import com.zfy.adapter.listener.PayloadBindCallback;
import com.zfy.adapter.model.Position;
import com.zfy.adapter.model.SingleTypeConfigCallback;

import java.util.List;

/**
 * CreateAt : 2018/11/13
 * Describe :
 *
 * @author chendong
 */
public class LightAdapterBuilder<D> {

    private BindCallback<D>         mBindCallback;
    private PayloadBindCallback<D>  mPayloadBindCallback;
    private EventCallback<D>        mClickEvent;
    private EventCallback<D>        mLongPressEvent;
    private EventCallback<D>        mDbClickEvent;
    private ModelTypeConfigCallback mCallback;
    private List<D>                 mList;

    public LightAdapterBuilder(List<D> datas, @LayoutRes int layoutId) {
        mCallback = new SingleTypeConfigCallback(data -> {
            data.layoutId = layoutId;
        }).setSingleType(ItemType.TYPE_CONTENT);
        mList = datas;
    }

    public LightAdapterBuilder(List<D> datas, ModelTypeConfigCallback callback) {
        mCallback = callback;
        mList = datas;
    }


    public LightAdapterBuilder<D> onBindView(BindCallback<D> bindCallback) {
        mBindCallback = bindCallback;
        return this;
    }

    public LightAdapterBuilder<D> onBindViewUsePayload(PayloadBindCallback<D> bindCallback) {
        mPayloadBindCallback = bindCallback;
        return this;
    }

    public LightAdapterBuilder<D> onClickEvent(EventCallback<D> clickEvent) {
        mClickEvent = clickEvent;
        return this;
    }

    public LightAdapterBuilder<D> onLongPressEvent(EventCallback<D> clickEvent) {
        mLongPressEvent = clickEvent;
        return this;
    }

    public LightAdapterBuilder<D> onDbClickEvent(EventCallback<D> clickEvent) {
        mDbClickEvent = clickEvent;
        return this;
    }

    public LightAdapter<D> build() {
        LightAdapter<D> lightAdapter = new LightAdapter<D>(mList, mCallback) {
            @Override
            public void onBindView(LightHolder holder, D data, Position pos) {
                if (mBindCallback != null) {
                    mBindCallback.bind(holder, pos, data);
                }
            }

            @Override
            public void onBindViewUsePayload(LightHolder holder, D data, Position pos, String msg) {
                if (mPayloadBindCallback != null) {
                    mPayloadBindCallback.bind(holder, pos, data, msg);
                }
            }
        };
        if (mClickEvent != null) {
            lightAdapter.setClickEvent(mClickEvent);
        }
        if (mLongPressEvent != null) {
            lightAdapter.setLongPressEvent(mLongPressEvent);
        }
        if (mDbClickEvent != null) {
            lightAdapter.setDbClickEvent(mDbClickEvent);
        }
        return lightAdapter;
    }

}
