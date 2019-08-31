package com.zfy.light.sample.cases;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.march.common.exts.ListX;
import com.march.common.exts.ToastX;
import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.collections.LightDiffList;
import com.zfy.adapter.collections.LightList;
import com.zfy.adapter.common.SpanSize;
import com.zfy.adapter.model.LightView;
import com.zfy.adapter.ModelTypeRegistry;
import com.zfy.component.basic.mvx.mvp.app.MvpActivity;
import com.zfy.component.basic.mvx.mvp.app.MvpV;
import com.zfy.light.sample.GlideCallback;
import com.zfy.light.sample.R;
import com.zfy.light.sample.Utils;
import com.zfy.light.sample.Values;
import com.zfy.light.sample.entity.Data;

import java.util.List;

import butterknife.BindView;

/**
 * CreateAt : 2018/11/13
 * Describe : 使用 payload 更新数据
 *
 * @author chendong
 */
@MvpV(layout = R.layout.recycler_activity)
public class PayloadTestActivity extends MvpActivity {

    public int ID = 100;

    @BindView(R.id.content_rv) RecyclerView mContentRv;

    private LightAdapter<Data> mAdapter;
    private LightList<Data>    mEntities;

    @Override
    public void init() {
        mEntities = new LightDiffList<>();
        // viewType callback
        ModelTypeRegistry registry = ModelTypeRegistry.create();
        registry.add(Data.TYPE_PAYLOAD1, R.layout.item_event, SpanSize.SPAN_SIZE_ALL);
        registry.add(Data.TYPE_PAYLOAD2, R.layout.item_swipe, SpanSize.SPAN_SIZE_ALL);
        // adapter
        mAdapter = new LightAdapter<>(mEntities, registry);
        mAdapter.setBindCallback((holder, data, extra) -> {
            // 局部绑定
            if (extra.byPayload) {
                switch (extra.payloadMsg) {
                    case Data.DESC_CHANGED:
                        holder.setText(R.id.desc_tv, data.desc + " 局部");
                        break;
                    case Data.TITLE_CHANGED:
                        holder.setText(R.id.title_tv, data.title + " 局部");
                        break;
                }
            } else {
                // 全局绑定
                holder.setText(R.id.title_tv, data.title + " 全局")
                        .setText(R.id.desc_tv, data.desc + " 全局");
            }
        });
        mAdapter.setClickEvent((holder, data, extra) -> {
            switch (data.getItemType()) {
                case Data.TYPE_PAYLOAD1:
                    ToastX.show("点击类型1，更新所有的 title");
                    mEntities.updateForEach(item -> {
                        item.title = "Title " + (ID++);
                    });
                    break;
                case Data.TYPE_PAYLOAD2:
                    ToastX.show("点击类型2，更新所有的 desc");
                    mEntities.updateForEach(item -> {
                        item.desc = "Desc " + (ID++);
                    });
                    break;
            }
        });
        // header
        mAdapter.header().addHeaderView(LightView.from(R.layout.desc_header), (holder) -> {
            holder.setText(R.id.desc_tv, Values.getPayloadDesc())
                    .setCallback(R.id.cover_iv, new GlideCallback(Utils.randomImage()))
                    .setClick(R.id.action_fab, v -> {
                    });
        });
        // animator
        mAdapter.animator().setAnimatorEnable(true);
        mContentRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentRv.setAdapter(mAdapter);
        appendData();
    }


    private void appendData() {
        List<Data> list = ListX.range(10, index -> {
            Data entity = new Data(index % 3 == 0 ? Data.TYPE_PAYLOAD1 : Data.TYPE_PAYLOAD2);
            entity.id = ID++;
            entity.title = "Title " + (entity.id);
            entity.desc = "Desc " + (entity.id);
            entity.subTitle = "SubTitle " + (entity.id);
            return entity;
        });
        mEntities.updateAddAll(list);
    }
}
