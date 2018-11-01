package com.zfy.adapter.model;

/**
 * CreateAt : 2018/11/1
 * Describe :
 *
 * @author chendong
 */
public interface Selectable {

    boolean selected[] = {false};

    default boolean isSelected() {
        return selected[0];
    }

    default void setSelected(boolean isSelected) {
        selected[0] = isSelected;
    }
}
