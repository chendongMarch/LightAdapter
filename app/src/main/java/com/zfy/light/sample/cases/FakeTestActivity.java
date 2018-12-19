package com.zfy.light.sample.cases;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.march.common.exts.ListX;
import com.march.common.exts.ToastX;
import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.collections.LightDiffList;
import com.zfy.adapter.collections.LightList;
import com.zfy.component.basic.mvx.mvp.app.MvpActivity;
import com.zfy.component.basic.mvx.mvp.app.MvpV;
import com.zfy.light.sample.R;
import com.zfy.light.sample.entity.Data;

import java.util.List;

import butterknife.BindView;

/**
 * CreateAt : 2018/11/13
 * Describe : 测试假数据展示
 *
 * @author chendong
 */
@MvpV(layout = R.layout.recycler_activity)
public class FakeTestActivity extends MvpActivity {

    public int ID = 100;

    @BindView(R.id.content_rv) RecyclerView mContentRv;

    private LightAdapter<Data> mAdapter;
    private LightList<Data>    mEntities;

    @Override
    public void init() {
        mEntities = new LightDiffList<>();
        // adapter
        mAdapter = new LightAdapter<>(mEntities, R.layout.item_content);
        mAdapter.setBindCallback((holder, data, extra) -> {
            holder.setText(R.id.title_tv, data.title)
                    .setText(R.id.desc_tv, data.desc);
        });
        mAdapter.setClickEvent((holder, data, extra) -> {

        });
        // header
//        mAdapter.header().addHeaderView(LightView.from(R.layout.desc_header), (holder) -> {
//            holder.setText(R.id.desc_tv, Values.getPayloadDesc())
//                    .setCallback(R.id.cover_iv, new GlideCallback(Utils.randomImage()))
//                    .setClick(R.id.action_fab, v -> {
//                    });
//        });
        mContentRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentRv.setAdapter(mAdapter);
        mAdapter.fake().showFake(10, R.layout.item_fake, (holder, data, extra) -> {

        });
        ToastX.show("5s 后加载真实数据");
        post(() -> {
            mAdapter.fake().hideFake();
            appendData();
        }, 5000);
    }


    private void appendData() {
        List<Data> list = ListX.range(10, index -> {
            Data entity = new Data(Data.TYPE_CONTENT);
            entity.id = ID++;
            entity.title = "Title " + (entity.id);
            entity.desc = "Desc " + (entity.id);
            entity.subTitle = "SubTitle " + (entity.id);
            return entity;
        });
        mEntities.updateAddAll(list);
    }
}
