package com.zfy.adapter.delegate.impl;

import com.zfy.adapter.LightHolder;
import com.zfy.adapter.model.Selectable;

/**
 * CreateAt : 2018/11/1
 * Describe :
 *
 * @author chendong
 */
public class SelectorDelegate extends BaseDelegate {

    @Override
    public int getKey() {
        return SELECTOR;
    }

    @Override
    public boolean onBindViewHolder(LightHolder holder, int position) {
        int pos = mAdapter.toModelIndex(position);
        Object data = mAdapter.getItem(pos);
        if (data instanceof Selectable) {
            onBindSelectableViewHolder(holder, position, (Selectable) data);
        }
        return super.onBindViewHolder(holder, position);
    }

    public void onBindSelectableViewHolder(LightHolder holder, int position, Selectable obj) {

    }

    /**
     * 选中某一项
     *
     * @param selectable 继承 Selectable 接口
     */
    public void selectItem(Selectable selectable) {
        if (selectable.isSelected()) {
            return;
        }
        selectable.setSelected(true);
        int pos = mAdapter.getDatas().indexOf(selectable);
        if (pos > 0) {
            mAdapter.notifyItem().change(mAdapter.toLayoutIndex(pos));
        }
    }

    /**
     * 释放某一项
     *
     * @param selectable 继承 Selectable 接口
     */
    public void releaseItem(Selectable selectable) {
        if (!selectable.isSelected()) {
            return;
        }
        selectable.setSelected(false);
        int pos = mAdapter.getDatas().indexOf(selectable);
        if (pos > 0) {
            mAdapter.notifyItem().change(mAdapter.toLayoutIndex(pos));
        }
    }

    /**
     * 选中改为不选中，不选中改为选中
     *
     * @param selectable 继承 Selectable 接口
     */
    public void toggleItem(Selectable selectable) {
        if (selectable.isSelected()) {
            releaseItem(selectable);
        } else {
            selectItem(selectable);
        }
    }
}
