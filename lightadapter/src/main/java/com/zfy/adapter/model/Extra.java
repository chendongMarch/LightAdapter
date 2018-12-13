package com.zfy.adapter.model;

import com.zfy.adapter.LightAdapter;

/**
 * CreateAt : 2018/11/12
 * Describe :
 *
 *
 *
 *
 *
 *
 * @author chendong
 */
public class Extra {

    /**
     * 因为自定义类型的存在，布局的的 pos 和数据中的 pos 并不完全对应，需要做一下转化
     * 当从数据源中取数据时，使用 modelIndex，比如 {@link LightAdapter#getDatas().get(int)}
     */
    public int     modelIndex;
    /**
     * 因为自定义类型的存在，布局的 pos 和数据中的 pos 并不完全对应，需要做一下转化
     * 当更新布局时，使用 layoutIndex，比如 {@link LightAdapter#notifyItem().change(int)}
     */
    public int     layoutIndex;
    /**
     * 用来标记当前数据是否处于绑定状态
     *
     * @see com.zfy.adapter.delegate.impl.SelectorDelegate
     */
    public boolean selected;
    /**
     * 为子控件设置监听事件的id
     */
    public int     viewId;
    /**
     * 使用 payload 绑定时的 msg
     */
    public String  payloadMsg;


    public Extra(int modelIndex, int layoutIndex) {
        this.modelIndex = modelIndex;
        this.layoutIndex = layoutIndex;
    }

    public Extra() {
    }
}
