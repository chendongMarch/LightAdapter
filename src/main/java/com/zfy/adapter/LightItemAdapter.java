package com.zfy.adapter;

import com.zfy.adapter.common.ItemType;
import com.zfy.adapter.contract.ItemAdapter;
import com.zfy.adapter.model.Extra;
import com.zfy.adapter.model.ModelType;

/**
 * CreateAt : 2018/11/12
 * Describe :
 *
 * @author chendong
 */
public abstract class LightItemAdapter<D> implements ItemAdapter<D> {

    private ModelType mModelType;

    public LightItemAdapter() {
        mModelType = newModelType();
    }

    @Override
    public int getItemType() {
        if (mModelType != null) {
            return mModelType.type;
        }
        return ItemType.TYPE_NONE;
    }

    @Override
    public ModelType getModelType() {
        return mModelType;
    }

    @Override
    public void onEventDispatch(int eventType, LightHolder holder, Extra extra, D data) {
        switch (eventType) {
            case LightEvent.TYPE_ITEM_CLICK:
                onClickEvent(holder, data, extra);
                break;
            case LightEvent.TYPE_ITEM_LONG_PRESS:
                onLongPressEvent(holder, data, extra);
                break;
            case LightEvent.TYPE_ITEM_DB_CLICK:
                onDbClickEvent(holder, data, extra);
                break;
            case LightEvent.TYPE_CHILD_CLICK:
                onChildViewClickEvent(holder, data, extra);
                break;
            case LightEvent.TYPE_CHILD_LONG_PRESS:
                onChildViewLongPressEvent(holder, data, extra);
                break;
            default:
        }
    }

    @Override
    public void onClickEvent(LightHolder holder, D data, Extra extra) {

    }

    @Override
    public void onLongPressEvent(LightHolder holder, D data, Extra extra) {

    }

    @Override
    public void onDbClickEvent(LightHolder holder, D data, Extra extra) {

    }

    @Override
    public void onChildViewClickEvent(LightHolder holder, D data, Extra extra) {

    }

    @Override
    public void onChildViewLongPressEvent(LightHolder holder, D data, Extra extra) {

    }


}
