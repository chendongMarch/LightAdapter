package com.zfy.light.sample;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.march.common.exts.ListX;
import com.march.common.exts.ToastX;
import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.LightHolder;
import com.zfy.adapter.assistant.SlidingSelectLayout;
import com.zfy.adapter.collections.LightDiffList;
import com.zfy.adapter.common.LightValues;
import com.zfy.adapter.delegate.impl.SelectorDelegate;
import com.zfy.adapter.listener.ViewHolderCallback;
import com.zfy.adapter.model.LightView;
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
@MvpV(layout = R.layout.selector_activity)
public class SelectorActivity extends MvpActivity {

    @BindView(R.id.content_rv) RecyclerView        mRecyclerView;
    @BindView(R.id.ssl)        SlidingSelectLayout mSlidingSelectLayout;

    private LightDiffList<SingleTypeEntity> mData;

    @Override
    public void init() {
        mData = new LightDiffList<>();

        LightAdapter<SingleTypeEntity> adapter = new LightAdapter<SingleTypeEntity>(getContext(), mData, R.layout.item_selector) {
            @Override
            public void onBindView(LightHolder holder, SingleTypeEntity data, int pos) {
                holder.setText(R.id.desc_tv, data.title);
            }
        };
        adapter.header().addHeaderView(LightView.from(R.layout.desc_header), new ViewHolderCallback() {
            @Override
            public void bind(LightHolder holder, int position) {
                holder.setText(R.id.desc_tv, Values.getSelectorDesc())
                        .setCallback(R.id.cover_iv, new GlideCallback(Utils.randomImage()))
                        .setClick(R.id.action_fab, v -> {
                            ToastX.show("选中了 " + adapter.selector().getResults().size() + " 个");
                        });
            }
        });
        adapter.selector().setSelectType(LightValues.MULTI);
        adapter.selector().setSelectorBinder(new SelectorDelegate.SelectorBinder<SingleTypeEntity>() {
            @Override
            public void onBindSelectableViewHolder(LightHolder holder, int position, SingleTypeEntity data, boolean isSelect) {
                holder.setChecked(R.id.checkbox, isSelect);
            }
        });
        adapter.setClickCallback((holder, pos, data) -> {
            adapter.selector().toggleItem(data);
        });

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRecyclerView.setAdapter(adapter);
        mData.update(ListX.range(100, index -> new SingleTypeEntity("title " + index, "desc " + index)));

    }

}
