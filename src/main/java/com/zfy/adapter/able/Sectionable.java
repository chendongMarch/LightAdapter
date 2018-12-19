package com.zfy.adapter.able;

/**
 * CreateAt : 2018/11/10
 * Describe :
 * 使用该接口表明该数据是一个 Section 类型的数据，
 * 当 isSection() 返回 true 时表示该数据是一个 Section
 *
 * @see com.zfy.adapter.delegate.impl.SectionDelegate
 *
 * @author chendong
 */
public interface Sectionable {

    boolean isSection();
}
