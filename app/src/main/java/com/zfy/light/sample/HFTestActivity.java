package com.zfy.light.sample;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.march.common.exts.ListX;
import com.march.common.exts.ToastX;
import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.LightHolder;
import com.zfy.adapter.collections.LightDiffList;
import com.zfy.adapter.model.LightView;
import com.zfy.adapter.model.Position;
import com.zfy.component.basic.mvx.mvp.app.MvpActivity;
import com.zfy.component.basic.mvx.mvp.app.MvpV;
import com.zfy.light.sample.entity.SingleTypeEntity;

import butterknife.BindView;

/**
 * CreateAt : 2018/11/9
 * Describe :
 *
 * @author chendong
 */
@MvpV(layout = R.layout.recycler_activity)
public class HFTestActivity extends MvpActivity {

    private String testStr = String.valueOf(System.currentTimeMillis());

    @BindView(R.id.content_rv) RecyclerView mRecyclerView;

    private LightDiffList<SingleTypeEntity> mData;

    @Override
    public void init() {
        mData = new LightDiffList<>();

        LightAdapter<SingleTypeEntity> adapter = new LightAdapter<SingleTypeEntity>( mData, R.layout.item_selector) {

            @Override
            public void onBindView(LightHolder holder, SingleTypeEntity data, Position pos) {
                holder.setText(R.id.desc_tv, data.title);
            }
        };
        addHeader(adapter);
        addFooter(adapter);

        adapter.setClickCallback((holder, pos, data) -> {
            testStr = String.valueOf(System.currentTimeMillis());
            adapter.header().notifyHeaderUpdate();
            adapter.footer().notifyFooterUpdate();
            ToastX.show("已为您更新 Header/Footer 时间戳显示");
        });

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRecyclerView.setAdapter(adapter);
        mData.update(ListX.range(10, index -> new SingleTypeEntity("title " + index, "desc " + index)));

    }

    private void addHeader(LightAdapter<SingleTypeEntity> adapter) {
        LightView view = LightView.from(R.layout.desc_header);
        adapter.header().addHeaderView(view, holder -> {
            holder.setText(R.id.desc_tv, testStr + "\n" + Values.getHFDesc())
                    .setCallback(R.id.cover_iv, new GlideCallback(Utils.randomImage()))
                    .setClick(R.id.cover_iv, v -> {
                        adapter.header().removeHeaderView(view);
                    })
                    .setClick(R.id.action_fab, v -> {
                        addHeader(adapter);
                    });
        });
    }

    private void addFooter(LightAdapter<SingleTypeEntity> adapter) {
        LightView lightView = LightView.from(R.layout.item_footer);
        adapter.footer().addFooterView(lightView, holder -> {
            holder.setText(R.id.desc_tv, "I AM FOOTER，点击我会再增加一个 Footer / 长按我会删除自己" + testStr)
                    .setLongClick(R.id.item_view, v -> {
                        adapter.footer().removeFooterView(lightView);
                        return true;
                    })
                    .setClick(v -> {
                        addFooter(adapter);
                    });
        });
    }

}
