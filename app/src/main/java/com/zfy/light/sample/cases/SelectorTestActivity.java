package com.zfy.light.sample.cases;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.march.common.exts.ListX;
import com.march.common.exts.ToastX;
import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.extend.SlidingSelectLayout;
import com.zfy.adapter.collections.LightDiffList;
import com.zfy.adapter.collections.LightList;
import com.zfy.adapter.model.LightView;
import com.zfy.component.basic.mvx.mvp.app.MvpActivity;
import com.zfy.component.basic.mvx.mvp.app.MvpV;
import com.zfy.light.sample.GlideCallback;
import com.zfy.light.sample.R;
import com.zfy.light.sample.Utils;
import com.zfy.light.sample.Values;
import com.zfy.light.sample.entity.SingleTypeEntity;

import butterknife.BindView;

/**
 * CreateAt : 2018/11/9
 * Describe : 选择器效果策四
 *
 * @author chendong
 */
@MvpV(layout = R.layout.selector_activity)
public class SelectorTestActivity extends MvpActivity {

    @BindView(R.id.content_rv) RecyclerView        mRecyclerView;
    @BindView(R.id.ssl)        SlidingSelectLayout mSlidingSelectLayout;

    private LightList<SingleTypeEntity>    mData;
    private LightAdapter<SingleTypeEntity> mAdapter;

    @Override
    public void init() {
        mData = new LightDiffList<>();
        mAdapter = new LightAdapter<>(mData, R.layout.item_selector);
        mAdapter.setBindCallback((holder, data, extra) -> {
            holder.setText(R.id.desc_tv, (data.id % 4 == 0) ? "不允许选中" : data.title);
        });
        mAdapter.setClickEvent((holder, data, extra) -> {
            mAdapter.selector().toggleItem(data);
        });
        mAdapter.header().addHeaderView(LightView.from(R.layout.desc_header), (holder) -> {
            holder.setText(R.id.desc_tv, Values.getSelectorDesc())
                    .setCallback(R.id.cover_iv, new GlideCallback(Utils.randomImage()))
                    .setClick(R.id.action_fab, v -> {
                        ToastX.show("选中了 " + mAdapter.selector().getResults().size() + " 个");
                    });
        });
        mAdapter.selector().setMultiSelector((holder, data, extra) -> {
            holder.setChecked(R.id.checkbox, extra.selected);
        });
        mAdapter.selector().setOnSelectListener((data, toSelect) -> {
            if (toSelect) {
                if (data.id % 4 == 0) {
                    ToastX.show("此项不允许选中");
                    return false;
                }
                if (mAdapter.selector().getResults().size() > 10) {
                    ToastX.show("最多可以选中10个");
                    return false;
                }
            }
            return true;
        });
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRecyclerView.setAdapter(mAdapter);
        mData.update(ListX.range(100, index -> new SingleTypeEntity(index, "title " + index, "desc " + index)));

    }

}
