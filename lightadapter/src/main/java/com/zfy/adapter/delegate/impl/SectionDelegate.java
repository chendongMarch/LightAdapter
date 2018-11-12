package com.zfy.adapter.delegate.impl;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zfy.adapter.LightHolder;
import com.zfy.adapter.assistant.decoration.PinItemDecoration;
import com.zfy.adapter.common.ItemType;
import com.zfy.adapter.common.LightUtils;
import com.zfy.adapter.common.SpanSize;
import com.zfy.adapter.delegate.refs.SectionRef;
import com.zfy.adapter.listener.BindCallback;
import com.zfy.adapter.model.ModelType;
import com.zfy.adapter.model.Position;
import com.zfy.adapter.model.SingleTypeConfigCallback;

/**
 * CreateAt : 2018/10/30
 * Describe : 底部加载更多功能
 *
 * @author chendong
 */
public class SectionDelegate<D> extends BaseDelegate implements SectionRef<D> {

    private BindCallback<D> mBindCallback; // section 绑定
    private boolean mPinEnable; // 是否支持悬停
    private PinItemDecoration mPinItemDecoration;

    @Override
    public int getKey() {
        return SECTION;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (mPinEnable && mPinItemDecoration == null) {
            mPinItemDecoration = new PinItemDecoration();
            recyclerView.addItemDecoration(mPinItemDecoration);
        }
    }


    @Override
    public LightHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ItemType.TYPE_SECTION) {
            ModelType type = mAdapter.getModelType(viewType);
            View view = LightUtils.inflateView(parent.getContext(), parent, type.layoutId);
            return new LightHolder(mAdapter, viewType, view);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean onBindViewHolder(LightHolder holder, int layoutIndex) {
        if (mBindCallback != null && mAdapter.getItemViewType(layoutIndex) == ItemType.TYPE_SECTION) {
            Position position = mAdapter.obtainPositionByLayoutIndex(layoutIndex);
            D data = (D) mAdapter.getItem(position.modelIndex);
            if (data != null) {
                mBindCallback.bind(holder, position, data);
            }
            return true;
        }
        return super.onBindViewHolder(holder, layoutIndex);
    }


    @Override
    public void setPinEnable(boolean pinEnable) {
        if (isAttached() && mPinItemDecoration == null && pinEnable) {
            mPinItemDecoration = new PinItemDecoration();
            mView.addItemDecoration(mPinItemDecoration);
        }
        mPinEnable = pinEnable;
    }

    @Override
    public void setOptions(ModelType type, BindCallback<D> bindCallback) {
        SingleTypeConfigCallback callback = new SingleTypeConfigCallback(type);
        mAdapter.addModelUpdater(callback);
        mBindCallback = bindCallback;
        setPinEnable(mAdapter.getModelType(ItemType.TYPE_SECTION).enablePin);
    }

    @Override
    public void setOptions(int layoutId, boolean supportPin, BindCallback<D> callback) {
        mAdapter.addModelUpdater(new SingleTypeConfigCallback(data -> {
            data.layoutId = layoutId;
            data.enablePin = supportPin;
            data.spanSize = SpanSize.SPAN_SIZE_ALL;
        }).setSingleType(ItemType.TYPE_SECTION));
        setPinEnable(supportPin);
        mBindCallback = callback;
    }

}
