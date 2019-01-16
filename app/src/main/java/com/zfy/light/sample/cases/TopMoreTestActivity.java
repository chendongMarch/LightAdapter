package com.zfy.light.sample.cases;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.march.common.exts.ListX;
import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.collections.LightDiffList;
import com.zfy.adapter.collections.LightList;
import com.zfy.adapter.common.SpanSize;
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
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;

/**
 * CreateAt : 2018/11/13
 * Describe : 顶部加载更多测试
 *
 * @author chendong
 */
@MvpV(layout = R.layout.topmore_activity)
public class TopMoreTestActivity extends MvpActivity {

    @BindView(R.id.content_rv) RecyclerView mContentRv;

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
        SampleUtils.addLoadingView(mAdapter);
        // loadMore
        mAdapter.loadMore().setLoadMoreListener(adapter -> {
            post(() -> {
                appendData(false);
                mAdapter.loadMore().finishLoadMore();
            }, 2000);
        });
        // topMore
        mAdapter.topMore().setTopMoreListener(adapter -> {
            post(() -> {
                appendData(true);
                mAdapter.topMore().finishTopMore();
            }, 2000);
        });
        // animator
        mAdapter.animator().setItemAnimator(new ScaleInAnimator());
        mContentRv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mContentRv.setAdapter(mAdapter);

        appendData(false);

        mContentRv.scrollToPosition(mAdapter.getDatas().size() / 2);
    }

    private void appendData(boolean top) {
        List<Data> list = ListX.range(10, index -> {
            Data entity = new Data(index % 3 == 0 ? Data.TYPE_DELEGATE : Data.TYPE_PROJECT);
            entity.title = "Title " + index;
            entity.desc = "Desc " + index;
            entity.subTitle = "SubTitle " + index;
            return entity;
        });
        if (top) {
            mEntities.updateAddAll(0, list);
        } else {
            mEntities.updateAddAll(list);
        }
    }
}
