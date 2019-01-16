package com.zfy.light.sample.cases;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.march.common.exts.ListX;
import com.march.common.exts.ToastX;
import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.animation.ScaleAnimator;
import com.zfy.adapter.collections.LightDiffList;
import com.zfy.adapter.collections.LightList;
import com.zfy.adapter.common.SpanSize;
import com.zfy.adapter.model.EmptyState;
import com.zfy.adapter.ModelTypeRegistry;
import com.zfy.component.basic.mvx.mvp.app.MvpActivity;
import com.zfy.component.basic.mvx.mvp.app.MvpV;
import com.zfy.light.sample.GlideCallback;
import com.zfy.light.sample.R;
import com.zfy.light.sample.SampleUtils;
import com.zfy.light.sample.Utils;
import com.zfy.light.sample.entity.Data;

import java.util.List;

import butterknife.BindView;

/**
 * CreateAt : 2018/11/13
 * Describe : 空白页面测试
 *
 * @author chendong
 */
@MvpV(layout = R.layout.sample_activity)
public class EmptyTestActivity extends MvpActivity {

    @BindView(R.id.content_rv) RecyclerView mContentRv;

    private int mLoadCount = 0;

    private LightAdapter<Data> mAdapter;
    private LightList<Data>    mEntities;

    @Override
    public void init() {
        mEntities = new LightDiffList<>();

        ModelTypeRegistry registry = ModelTypeRegistry.create();
        registry.add(Data.TYPE_DELEGATE, R.layout.item_deleate, SpanSize.SPAN_SIZE_HALF);
        registry.add(Data.TYPE_PROJECT, R.layout.item_cover, SpanSize.SPAN_SIZE_HALF);
        // adapter
        mAdapter = new LightAdapter<>(mEntities, registry);
        mAdapter.setBindCallback((holder, data, extra) -> {
            holder.setText(R.id.title_tv, data.title)
                    .setText(R.id.desc_tv, data.desc);
            switch (data.type) {
                case Data.TYPE_DELEGATE:
                    holder.setText(R.id.subtitle_tv, "子标题");
                    break;
                case Data.TYPE_PROJECT:
                    holder.setCallback(R.id.cover_iv, new GlideCallback(Utils.randomImage()));
                    break;
            }
        });
        mAdapter.setClickEvent((holder, data, extra) -> {
            ToastX.show("click item");
        });
        // header
        // SampleUtils.addHeader(mAdapter, Values.getEmptyDesc(), null);
        SampleUtils.addEmpty(mAdapter, v -> {
            setEmpty(false);
            appendData();
        });
        SampleUtils.addLoadingView(mAdapter);
        // loadMore
        mAdapter.loadMore().setLoadMoreListener(adapter -> {
            post(() -> {
                appendData();
                mAdapter.loadMore().finishLoadMore();
            }, 2000);
        });
        // animator
        mAdapter.animator().setBindAnimator(new ScaleAnimator(.1f));
        mContentRv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mContentRv.setAdapter(mAdapter);

        // 模拟失败
        setEmpty(true);
    }


    public void setEmpty(boolean empty) {
        if (empty) {
            mLoadCount = 0;
            mAdapter.emptyView().setEmptyState(EmptyState.ERROR);
        } else {
            mAdapter.emptyView().setEmptyState(EmptyState.NONE);
        }
    }

    private void appendData() {
        mLoadCount++;
        if (mLoadCount > 2) {
            mEntities.updateClear();
            setEmpty(true);
            return;
        }
        List<Data> list = ListX.range(10, index -> {
            Data entity = new Data(index % 3 == 0 ? Data.TYPE_DELEGATE : Data.TYPE_PROJECT);
            entity.title = "Title " + index;
            entity.desc = "Desc " + index;
            entity.subTitle = "SubTitle " + index;
            return entity;
        });
        mEntities.updateAddAll(list);
    }
}
