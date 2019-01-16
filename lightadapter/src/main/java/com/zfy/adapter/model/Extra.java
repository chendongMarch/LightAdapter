package com.zfy.adapter.model;

import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.callback.EventCallback;

/**
 * CreateAt : 2018/11/12
 * Describe :

 * @author chendong
 */
public class Extra {

    private static final ThreadLocal<Extra> sExtra = new ThreadLocal<Extra>() {
        @Override
        protected Extra initialValue() {
            return new Extra();
        }
    };

    // 数据集索引和布局索引的差异是因为内置了很多自定义的类型，比如 Header 等，因此他们并不一致
    // 原则就是操作数据，则使用 modelIndex, 操作布局则使用 layoutIndex

    /**
     * 数据集索引，使用他从集合中获取数据
     * adapter.getDatas().get(modelIndex)
     */
    public int modelIndex;

    /**
     * 布局索引，使用它来更新界面显示
     * adapter.notifyItem.change(layoutIndex)
     */
    public int layoutIndex;

    /**
     * 用来标记当前数据是否处于绑定状态，配合选择器使用
     */
    public boolean selected;
    /**
     * 子控件 id
     * 配合 {@link LightAdapter#setChildViewClickEvent(EventCallback)} 使用
     */
    public int     viewId;
    /**
     * 使用 payload 绑定时的 msg
     */
    public String  payloadMsg;
    /**
     * 当前是不是 payload 更新
     */
    public boolean byPayload;


    private Extra() {
    }

    public static Extra extra() {
        return new Extra();
    }
}
