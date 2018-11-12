package com.zfy.adapter.items;

import com.zfy.adapter.LightHolder;
import com.zfy.adapter.common.SpanSize;

/**
 * CreateAt : 2018/11/12
 * Describe :
 *
 * @author chendong
 */
@TypeOption(type = 100, spanSize = SpanSize.SPAN_SIZE_ALL, enableClick = true,
        enableDrag = true, enableSwipe = false)
public class DataItemAdapter<D> extends SimpleItemAdapter<D> {
    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void onBindView(LightHolder holder, D data, int pos) {

    }
}
