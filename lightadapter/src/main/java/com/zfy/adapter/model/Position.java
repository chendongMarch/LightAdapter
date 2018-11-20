package com.zfy.adapter.model;

import com.zfy.adapter.LightAdapter;

/**
 * CreateAt : 2018/11/12
 * Describe :
 * 因为自定义类型的存在，拿到的 pos 和数据中的 pos 并不完全对应，需要做一下转化，
 * 所有使用 pos 的地方都用 {@link Position} 代替方便使用
 * 当从数据源中取数据时，使用 {@link Position#modelIndex}，比如 {@link LightAdapter#getDatas().get(int)}
 * 当更新布局时，使用 {@link Position#layoutIndex}，比如 {@link LightAdapter#notifyItem().change(int)}
 *
 * @author chendong
 */
public class Position {

    public int modelIndex;
    public int layoutIndex;

    public boolean isSelect;

    public Position(int modelIndex, int layoutIndex) {
        this.modelIndex = modelIndex;
        this.layoutIndex = layoutIndex;
    }
}
