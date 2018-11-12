package com.zfy.adapter.able;

/**
 * CreateAt : 2018/11/10
 * Describe :
 * 实现该接口表明该数据可被选择，实现接口需要使用一个变量持有选中状态
 *
 * @see com.zfy.adapter.delegate.impl.SelectorDelegate
 *
 * @author chendong
 */
public interface Selectable {

    void setSelected(boolean selected);

    boolean isSelected();
}
