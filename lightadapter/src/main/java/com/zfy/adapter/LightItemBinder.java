package com.zfy.adapter;

import com.zfy.adapter.contract.ItemBinder;
import com.zfy.adapter.callback.BindCallback;
import com.zfy.adapter.callback.EventCallback;
import com.zfy.adapter.model.Extra;
import com.zfy.adapter.model.ModelType;

/**
 * CreateAt : 2018/11/12
 * Describe :
 *
 * @author chendong
 */
public abstract class LightItemBinder<D> implements ItemBinder<D> {

    private ModelType mModelType;

    protected BindCallback<D>  mBindCallback;
    protected EventCallback<D> mClickCallback;
    protected EventCallback<D> mLongPressCallback;
    protected EventCallback<D> mDbClickCallback;
    protected EventCallback<D> mChildViewClickCallback;
    protected EventCallback<D> mChildViewLongPressCallback;

    @Override
    public int getItemType() {
        return getModelType().type;
    }

    @Override
    public ModelType getModelType() {
        if (mModelType == null) {
            mModelType = newModelType();
        }
        return mModelType;
    }

    @Override
    public void onEventDispatch(int eventType, LightHolder holder, Extra extra, D data) {
        switch (eventType) {
            case LightEvent.TYPE_ITEM_CLICK:
                if (mClickCallback != null) {
                    mClickCallback.call(holder, data, extra);
                }
                break;
            case LightEvent.TYPE_ITEM_LONG_PRESS:
                if (mLongPressCallback != null) {
                    mLongPressCallback.call(holder, data, extra);
                }
                break;
            case LightEvent.TYPE_ITEM_DB_CLICK:
                if (mDbClickCallback != null) {
                    mDbClickCallback.call(holder, data, extra);
                }
                break;
            case LightEvent.TYPE_CHILD_CLICK:
                if (mChildViewClickCallback != null) {
                    mChildViewClickCallback.call(holder, data, extra);
                }
                break;
            case LightEvent.TYPE_CHILD_LONG_PRESS:
                if (mChildViewLongPressCallback != null) {
                    mChildViewLongPressCallback.call(holder, data, extra);
                }
                break;
            default:
        }
    }

    @Override
    public void onBindViewUseItemBinder(LightHolder holder, D data, Extra extra) {
        onBindView(holder, data, extra);
        if (mBindCallback != null) {
            mBindCallback.bind(holder, data, extra);
        }
    }

    @Override
    public void setBindCallback(BindCallback<D> bindCallback) {
        mBindCallback = bindCallback;
    }

    @Override
    public void setClickEvent(EventCallback<D> clickCallback) {
        mClickCallback = clickCallback;
    }

    @Override
    public void setLongPressEvent(EventCallback<D> longPressCallback) {
        mLongPressCallback = longPressCallback;
    }

    @Override
    public void setDbClickEvent(EventCallback<D> dbClickCallback) {
        mDbClickCallback = dbClickCallback;
    }

    @Override
    public void setChildViewClickEvent(EventCallback<D> childViewClickEvent) {
        mChildViewClickCallback = childViewClickEvent;
    }

    @Override
    public void setChildViewLongPressEvent(EventCallback<D> childViewLongPressEvent) {
        mChildViewLongPressCallback = childViewLongPressEvent;
    }

}
