package com.zfy.adapter.delegate.impl;

import android.view.View;
import android.view.ViewGroup;

import com.zfy.adapter.LightHolder;
import com.zfy.adapter.common.ItemType;
import com.zfy.adapter.delegate.refs.FakeRef;
import com.zfy.adapter.callback.BindCallback;
import com.zfy.adapter.model.Extra;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateAt : 2018/11/5
 * Describe :
 * <p>
 * 当 Empty 显示时，LoadMore 会自动停止
 *
 * @author chendong
 */
public class FakeDelegate<D> extends BaseViewDelegate implements FakeRef<D> {


    private BindCallback<D> mBindCallback; // 绑定回调
    private int             mLayoutId;
    private boolean         mEnable;
    private List            mTempList;

    @Override
    public int getKey() {
        return FAKE;
    }

    @Override
    public LightHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ItemType.TYPE_FAKE) {
            View inflate = mAdapter.getLayoutInflater().inflate(mLayoutId, parent, false);
            return new LightHolder(mAdapter, viewType, inflate);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public boolean onBindViewHolder(LightHolder holder, int layoutIndex) {
        if (mAdapter.getItemViewType(layoutIndex) == ItemType.TYPE_FAKE) {
            Extra extra = mAdapter.obtainExtraByLayoutIndex(layoutIndex);
            mBindCallback.bind(holder, null, extra);
            return true;
        }
        return super.onBindViewHolder(holder, layoutIndex);
    }


    @Override
    public int getItemViewType(int position) {
        if (mEnable) {
            return ItemType.TYPE_FAKE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (mEnable) {
            return mAdapter.getDatas().size();
        }
        return super.getItemCount();
    }

    @Override
    public int getAboveItemCount(int level) {
        if (mEnable) {
            return 0;
        }
        return super.getAboveItemCount(level);
    }

    @Override
    public void showFake(int count, int layoutId, BindCallback<D> callback) {
        mLayoutId = layoutId;
        mBindCallback = callback;
        mTempList = mAdapter.getDatas();
        List list = new ArrayList();
        for (int i = 0; i < count; i++) {
            list.add(null);
        }
        mAdapter.setDatas(list);
        mEnable = true;
        mAdapter.notifyItem().change();
    }

    @Override
    public void hideFake() {
        if (mTempList != null) {
            mAdapter.setDatas(mTempList);
        } else {
            List datas = mAdapter.getDatas();
            datas.clear();
        }
        mEnable = false;
        mAdapter.notifyItem().change();

    }
}
